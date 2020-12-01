package com.example.id_qr.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.example.id_qr.R;
import com.example.id_qr.data_models.Recarga;
import com.example.id_qr.ui.AgregarPagos;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecargaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecargaFragment extends Fragment {
    private static final String TAG = RecargaFragment.class.getSimpleName();

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private final DatabaseReference rootReference = database.getReference();
    private final DatabaseReference userReference = rootReference.child("Pruebas").child("users").child(firebaseUser.getUid());
    private final DatabaseReference parkingReference = userReference.child("Parqueadero");
    private final DatabaseReference historialReference = parkingReference.child("Historial");
    private final DatabaseReference saldoReference = userReference.child("Saldo");
    private final DatabaseReference metodosPagoReference = userReference.child("opciones_pago");


    private AutoCompleteTextView spinnerMetodos;
    private TextInputLayout metodosLayout, recargaLayout;
    private ConstraintLayout backgroundLayout;
    private TextInputEditText valorRecarga;
    private Button recargaButton, agregarButton;
    private TextView resumen, saldoActual;

    private ArrayAdapter<String> adapter;
    private List<String> metodos = new ArrayList<>();

    private boolean continuar = false;
    private int saldoIntActual = 0;


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Button btnRecarga, btnAgregar;


    public RecargaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecargaFragment.
     */
    public static RecargaFragment newInstance(String param1, String param2) {
        RecargaFragment fragment = new RecargaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_recarga, container, false);


        spinnerMetodos = view.findViewById(R.id.metodos_pago);
        metodosLayout = view.findViewById(R.id.metodos_pago_layout);
        recargaLayout = view.findViewById(R.id.valor_recargar_layout);
        backgroundLayout = view.findViewById(R.id.background_layout);
        valorRecarga = view.findViewById(R.id.valor_a_recargar);
        recargaButton = view.findViewById(R.id.recarga_button);
        agregarButton = view.findViewById(R.id.guardar_button);
        resumen = view.findViewById(R.id.resumen);
        saldoActual = view.findViewById(R.id.saldo_actual);

        adapter = new ArrayAdapter<>(view.getContext(), R.layout.drop_menu_format, metodos);
        spinnerMetodos.setAdapter(adapter);

        valorRecarga.setOnFocusChangeListener(valorChangeListener);

        spinnerMetodos.setOnDismissListener(dismissListener);


        saldoReference.addValueEventListener(saldoListener);
        metodosPagoReference.addValueEventListener(metodosPagoListener);

        btnRecarga = view.findViewById(R.id.recarga_button);
        btnRecarga.setOnClickListener(recargaClickListener);
        btnAgregar = view.findViewById(R.id.guardar_button);
        btnAgregar.setOnClickListener(agregarPagoClickListener);


        return view;
//        return inflater.inflate(R.layout.fragment_recarga, container, false);
    }

    private final ValueEventListener saldoListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Log.d(TAG, "Saldo Changed");
            String saldo = snapshot.getValue(String.class);
            saldoActual.setText(getString(R.string.saldo_actual_txt).concat(saldo));
            saldoIntActual = Integer.parseInt(saldo);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.w(TAG, "Error en SaldoListener", error.toException().getCause());
        }
    };

    private final ValueEventListener metodosPagoListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            metodos.clear();
//            for (DataSnapshot data : snapshot.getChildren()) {
//                metodos.add(data.getKey());
//            }
            for (DataSnapshot snap : snapshot.getChildren()) {
                for (DataSnapshot snap2 : snap.getChildren()) {
                    metodos.add(snap2.getKey());
                }
//                metodos.add(metodosPagoReference.child(snap.getKey()).getKey());

            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.w(TAG, "Error en metodosPagoListener", error.toException().getCause());
        }
    };


    private View.OnFocusChangeListener valorChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                hideKeyBoard(v);
                if (verificar(v)) {
                    updateResumen();
                }
            }
        }
    };

    private final AutoCompleteTextView.OnDismissListener dismissListener = new AutoCompleteTextView.OnDismissListener() {
        @Override
        public void onDismiss() {
            spinnerMetodos.clearFocus();
        }
    };

    private final View.OnClickListener recargaClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "recargaClickListener");

            if (verificar(v)) {
                try {

                    int a = Integer.parseInt(valorRecarga.getText().toString());
                    int total = saldoIntActual + a;
                    saldoReference.setValue(String.valueOf(total));


                    Map<String, String> dateTimeMap = getDateTime();
                    final DatabaseReference dateReference = historialReference.child(dateTimeMap.get("DATE"));
                    final DatabaseReference timeReference = dateReference.child(dateTimeMap.get("TIME"));
                    Map<String, Object> dat = new HashMap<>();
                    dat.put("Event", new Recarga(String.valueOf(a), spinnerMetodos.getText().toString()));
                    dat.put("Timestamp", ServerValue.TIMESTAMP);
                    timeReference.setValue(dat);

                    valorRecarga.setText("");

                } catch (NumberFormatException e) {
                    Log.e(TAG, "Error Conviertiendo Saldo de String a Integer", e);
                }
            }
        }
    };

    private final View.OnClickListener agregarPagoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getContext(), AgregarPagos.class);
            startActivity(i);

        }
    };

    private boolean verificar(View v) {
        continuar = true;
        Log.d(TAG, "Checking Format and Value of Fields");
        if (spinnerMetodos.getText().toString().length() < 3) {
            Log.w(TAG, "SpinnerMetodos, No se a seleccionado opcion");
            metodosLayout.setError("Seleccione una Opción");
            continuar = false;
        } else {
            metodosLayout.setError(null);
        }
        if (valorRecarga.getText().toString().length() < 3) {
            Log.w(TAG, "ValorRecarga, Formato NO VALIDO");
            recargaLayout.setError("Ingrese un Valor Valido");
            continuar = false;
        } else {
            recargaLayout.setError(null);
        }

        return continuar;
    }

    private void updateResumen() {
        Log.d(TAG, "UpdateResumen");
        StringBuilder sb = new StringBuilder();
        sb.append("Metodo de Pago, Termina en: ").append(spinnerMetodos.getText().toString());
        sb.append("\n");
        sb.append("Valor a Recargar: ").append(Objects.requireNonNull(valorRecarga.getText()).toString());

        resumen.setText(sb.toString());
    }


    private void hideKeyBoard(View view) {
        // Hide the keyboard when the button is pushed.
        InputMethodManager inputManager = (InputMethodManager)
                view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private Map<String, String> getDateTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HHmmss");

        String date = dateTime.format(dateFormat);
        String time = dateTime.format(timeFormat);

        Log.d(TAG, "getDateTime: date: " + date);
        Log.d(TAG, "getDateTime: time: " + time);

        Map<String, String> map = new HashMap<>();

        map.put("DATE", date);
        map.put("TIME", time);

        return map;
    }


}