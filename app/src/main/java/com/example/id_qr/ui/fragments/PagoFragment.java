package com.example.id_qr.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.id_qr.R;
import com.example.id_qr.data_models.Pago;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PagoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PagoFragment extends Fragment {
    private static final String TAG = "PagoFragment";

    private static final String TIME = "time";
    private static final String DATE = "date";

    private static final int VALOR_NORMAL = 4000;
    private static final int VALOR_DIA = 6000;
    private static final int VALOR_TRANSPORTE = 2000;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private final DatabaseReference rootReference = database.getReference();
    private final DatabaseReference userReference;
    private final DatabaseReference parkingReference;
    private final DatabaseReference historialReference;
    private final DatabaseReference saldoReference;

    {
        assert firebaseUser != null;
        userReference = rootReference.child("Pruebas").child("users").child(firebaseUser.getUid());
        parkingReference = userReference.child("Parqueadero");
        historialReference = parkingReference.child("Historial");
        saldoReference = userReference.child("Saldo");
    }

    private int saldo;

    private Map<String, Integer> valueMap;

    private Button btn_PagoNormal;
    private Button btn_PagoDia;
    private Button btn_PagoTransporte;

    private ImageButton ib_pagoNormal;
    private ImageButton ib_pagoDia;
    private ImageButton ib_pagoTransporte;


    public PagoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PagoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PagoFragment newInstance(String param1, String param2) {
        PagoFragment fragment = new PagoFragment();
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
//        DatabaseReference conditionReference = mRootRef.child("condition");
        saldoReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "saldoReference");
                String s = snapshot.getValue(String.class);
                if (s != null && s.matches("[0-9]+")) {
                    Log.d(TAG, "SaldoReference: Format Valid");
//                    saldoString = snapshot.getValue(String.class);
//                    saldoString = s;
                    try {
                        saldo = Integer.parseInt(s);
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Failed to Convert \"Saldo\" to Integer", e.getCause());
                    }
                } else {
                    Log.w(TAG, "SaldoReference: FORMAT WARNING");
                    Log.w(TAG, "SaldoReference: Setting Value = \"0\"");
                    saldoReference.setValue("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //TODO
            }
        });

        valueMap = new HashMap<>();
        valueMap.put("NORMAL", VALOR_NORMAL);
        valueMap.put("DIA", VALOR_DIA);
        valueMap.put("TRANSPORTE", VALOR_TRANSPORTE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pago, container, false);

//        Log.d(TAG, "ATTEMPTING TO ASSIGN R.id.btn_pago_dia_normal");
        btn_PagoNormal = (Button) view.findViewById(R.id.btn_pago_dia_normal);
//        if (btn_PagoNormal == null) {
//            Log.e(TAG, "FAILED TO ASSIGN R.id.btn_pago_dia_normal");
//        } else {
//            Log.d(TAG, "R.id.btn_pago_dia_normal Assigned");
//        }
        btn_PagoDia = (Button) view.findViewById(R.id.btn_pago_dia_completo);
        btn_PagoTransporte = (Button) view.findViewById(R.id.btn_pago_transporte_eia);

        ib_pagoNormal = (ImageButton) view.findViewById(R.id.ib_pago_dia_normal);
        ib_pagoDia = (ImageButton) view.findViewById(R.id.ib_pago_dia_completo);
        ib_pagoTransporte = (ImageButton) view.findViewById(R.id.ib_pago_transporte);

        startActions();

        //
//        mUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (mUser != null) {
//            Log.e(TAG, "User is signed in");
//        }
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_pago, container, false);
        return view;
    }


//    private void pagoNormal(View v) {
//        Log.d(TAG, "pago Normal");
//        new MaterialAlertDialogBuilder(v.getContext()).setTitle("Pago Normal")
//                .setMessage("¿Pagar Parqueadero?").setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Log.d(TAG, "Pago Normal: \"CANCEL\"");
//            }
//        }).setPositiveButton("Pagar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Log.d(TAG, "Pago Normal: \"ACCEPT\"");
//                storeEvent("NORMAL");
//            }
//        });
////        if (mUser != null) {
////            // Agregar a fecha
////            // [fecha hoy] :
////        }
//    }
//
//    private void pagoDia(View v) {
//        Log.d(TAG, "pago Dia");
//
//        new MaterialAlertDialogBuilder(v.getContext()).setTitle("Pago Dia")
//                .setMessage("¿Pagar Dia?").setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Log.d(TAG, "Pago Dia: \"CANCEL\"");
//            }
//        }).setPositiveButton("Pagar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Log.d(TAG, "Pago Dia: \"ACCEPT\"");
//                storeEvent("DIA");
//
//            }
//        });
//
////        userReference.child("3").setValue("ANNDREs").addOnCompleteListener(new OnCompleteListener<Void>() {
////            @Override
////            public void onComplete(@NonNull Task<Void> task) {
////                Log.e(TAG, "COMPLETE");
////            }
////        });
////        userReference.child("3").setValue(new User("Pato", "Hoyos", 27), new DatabaseReference.CompletionListener() {
////            @Override
////            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
////
////            }
////        });
//    }
//
//    private void pagoTransporte(View v) {
//        Log.d(TAG, "pago Transporte");
//
//        new MaterialAlertDialogBuilder(v.getContext()).setTitle("Pago Transporte")
//                .setMessage("¿Pagar Transporte?").setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Log.d(TAG, "Pago Transporte: \"Cancel\"");
//            }
//        }).setPositiveButton("Pagar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Log.d(TAG, "Pago Transporte: \"Accept\"");
//                storeEvent("Transporte");
//
//            }
//        });
//    }


    private void startActions() {

        btn_PagoNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pagoNormal(v);
                pagoEvent(v, "Normal");
            }
        });

        ib_pagoNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pagoNormal(v);
                pagoEvent(v, "Normal");

            }
        });

        btn_PagoDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pagoDia(v);
                pagoEvent(v, "Dia");

            }
        });

        ib_pagoDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pagoDia(v);
                pagoEvent(v, "Dia");
            }
        });

        btn_PagoTransporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pagoTransporte(v);
                pagoEvent(v, "Transporte");
            }
        });

        ib_pagoTransporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pagoTransporte(v);
                pagoEvent(v, "Transporte");

            }
        });
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

        map.put(DATE, date);
        map.put(TIME, time);

        return map;
    }

    private void storeEvent(String tipoEvento) {
        Map<String, String> dateTimeMap = getDateTime();
        final DatabaseReference dateReference = historialReference.child(dateTimeMap.get(DATE));
        final DatabaseReference timeReference = dateReference.child(dateTimeMap.get(TIME));

//        int newSaldo;
//        if((newSaldo = saldo - valueMap.get(tipoEvento))<0) {
        Map<String, Object> data = new HashMap<>();
        data.put("Event", new Pago(tipoEvento));
        data.put("Timestamp", ServerValue.TIMESTAMP);

        timeReference.setValue(data);
        int newSaldo = saldo - valueMap.get(tipoEvento);
        Log.d(TAG, "new Saldo : " + newSaldo);
        saldoReference.setValue(String.valueOf(newSaldo));

    }

//    public void pagoNormal(View v){
//        Log.e(TAG, "HERE");
//    }
//
//    public void pagoDia(View v){
//
//    }
//    public void pagoTransporte(View v){
//
//    }

    private void pagoEvent(View v, String tipoPago) {
        Log.d(TAG, "Pago " + tipoPago);

        String p = tipoPago.equalsIgnoreCase("Normal") ? "Parqueadero" : tipoPago;

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(v.getContext());
        dialogBuilder.setTitle("Pago " + p);

        dialogBuilder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "pago " + tipoPago + ": \"CANCEL\"");
            }
        });
        //TODO verificar Saldo
        if (checkSaldoTransaction(tipoPago)) {
//            dialogBuilder.setMessage("¿Desea hacer el pago del " + p + "?" + "\nSaldo actual: " + saldo + "\n");
            dialogBuilder.setMessage("Saldo actual: " + saldo + "\nValor de la Transaccion: " + valueMap.get(tipoPago.toUpperCase()) + "\nSaldo Restante: " + (saldo - valueMap.get(tipoPago.toUpperCase())));
            dialogBuilder.setPositiveButton("Pagar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "pago " + tipoPago + ": \"ACCEPT\"");
                    storeEvent(tipoPago.toUpperCase());
                }
            });
        } else {
            dialogBuilder.setMessage("Saldo insuficiente\nSaldo actual: " + saldo + "\nEl saldo Necesario: " + valueMap.get(tipoPago.toUpperCase()));
        }
        dialogBuilder.show();
    }

    private boolean checkSaldoTransaction(String tipoPago) {
        int valorOperacion;
        switch (tipoPago.toUpperCase()) {
            case "NORMAL":
                valorOperacion = VALOR_NORMAL;
                break;
            case "DIA":
                valorOperacion = VALOR_DIA;
                break;
            case "TRANSPORTE":
                valorOperacion = VALOR_TRANSPORTE;
                break;
            default:
                valorOperacion = 0;
                break;
        }
        try {
            if (saldo >= valorOperacion) {
                return true;
            }
        } catch (NumberFormatException e) {
            Log.e(TAG, "Failed to Convert Saldo to Integer", e.getCause());
        }
        return false;
    }
}