package com.example.id_qr.ui.secundary;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.id_qr.R;
import com.example.id_qr.data_models.MedioDePago;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AgregarPagos extends AppCompatActivity {
    private static final String TAG = AgregarPagos.class.getSimpleName();

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private final DatabaseReference rootReference = database.getReference();
    private final DatabaseReference userReference = rootReference.child("Pruebas").child("users").child(firebaseUser.getUid());
    private final DatabaseReference metodosPagoReference = userReference.child("opciones_pago");

    private TextInputEditText cardNumber, nombreTitular, expirationDate, cardCode;
    private AutoCompleteTextView spinnerBancos;
    private ConstraintLayout backgroundLayout;
    private TextInputLayout spinnerBancosLayout, cardNumberInputLayout, nombreTitularInputLayout, expirationDateInputLayout, cardCodeInputLayout;

    private Toolbar toolbar;
    private ActionBar actionBar;

    private ArrayAdapter<CharSequence> arrayAdapter;

    private int numberCount = 0;
    private int dateCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_pagos);

        cardNumber = findViewById(R.id.numero_tarjeta);
        nombreTitular = findViewById(R.id.nombre_titular);
        expirationDate = findViewById(R.id.fecha_expiracion);
        cardCode = findViewById(R.id.codigo_tarjeta);
        spinnerBancos = findViewById(R.id.dropdown_bancos);

        backgroundLayout = findViewById(R.id.background_layout);
        spinnerBancosLayout = findViewById(R.id.textInputLayout_dropdown_bancos);
        cardNumberInputLayout = findViewById(R.id.textInputLayout_numero_tarjeta);
        nombreTitularInputLayout = findViewById(R.id.textInputLayout_nombre_titular);
        expirationDateInputLayout = findViewById(R.id.textInputLayout_fecha_expiracion);
        cardCodeInputLayout = findViewById(R.id.textInputLayout_codigo_tarjeta);

        toolbar = (Toolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        spinnerBancos.setOnDismissListener(dismissListener);

        cardNumber.addTextChangedListener(cardNumberWatcher);
        expirationDate.addTextChangedListener(cardExpirationWatcher);

//        cardNumber.setOnFocusChangeListener(changeListener);
//        nombreTitular.setOnFocusChangeListener(changeListener);
//        expirationDate.setOnFocusChangeListener(changeListener);
//        cardCode.setOnFocusChangeListener(changeListener);

        arrayAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.bancos, R.layout.drop_menu_format);
        spinnerBancos.setAdapter(arrayAdapter);

        backgroundLayout.setOnFocusChangeListener(changeListener);
        spinnerBancos.setOnFocusChangeListener(changeListener);


    }

    private final TextWatcher cardNumberWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //EMPTY
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //EMPTY
        }

        @Override
        public void afterTextChanged(Editable s) {

            int inputlength = cardNumber.getText().toString().length();

            if (numberCount <= inputlength && (inputlength == 4 || inputlength == 9 || inputlength == 14)) {

                cardNumber.setText(cardNumber.getText().toString().concat(" "));

                int pos = cardNumber.getText().length();
                cardNumber.setSelection(pos);

            } else if (numberCount >= inputlength && (inputlength == 4 || inputlength == 9 || inputlength == 14)) {
                cardNumber.setText(cardNumber.getText().toString().substring(0, cardNumber.getText().toString().length() - 1));

                int pos = cardNumber.getText().length();
                cardNumber.setSelection(pos);
            }
            numberCount = cardNumber.getText().toString().length();
        }
    };

    private final TextWatcher cardExpirationWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //EMPTY
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //EMPTY
        }

        @Override
        public void afterTextChanged(Editable s) {

            int inputLength = expirationDate.getText().toString().length();

            if (dateCount <= inputLength && (inputLength == 2)) {

                String temp = (String) expirationDate.getText().toString();
                expirationDate.setText(temp.concat("/"));

                int pos = expirationDate.getText().length();
                expirationDate.setSelection(pos);

            } else if (dateCount >= inputLength && (inputLength == 2)) {

                String temp = expirationDate.getText().toString().substring(0, inputLength - 1);
                expirationDate.setText(temp);

                int pos = expirationDate.getText().length();
                expirationDate.setSelection(pos);
            }
            dateCount = expirationDate.getText().toString().length();
        }
    };

    private final AutoCompleteTextView.OnDismissListener dismissListener = new AutoCompleteTextView.OnDismissListener() {
        @Override
        public void onDismiss() {
            spinnerBancos.clearFocus();
        }
    };
    private View.OnFocusChangeListener changeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                hideKeyBoard(v);
            }
        }
    };

    public void agregar(View v) {
        if (check()) {
            new MaterialAlertDialogBuilder(v.getContext())
            .setTitle(getString(R.string.confirmar))
            .setMessage(getString(R.string.agregar_metodo_pago))
            .setNeutralButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "AgregarMetodo, Dialogbuilde, CANCELAR");
                    //EMPTY
                }
            }).setPositiveButton(getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "AgregarMetodo, Dialogbuilde, PositiveOption");
                    sendPago(v);
                }
            }).show();

        } else {
            Toast.makeText(v.getContext(), "Revise los Campos", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelar(View v) {
        finish();
    }

    private boolean check() {
        boolean t = true;
        Log.d(TAG, "Checking Format State");
        //Spinner Bancos
        if (spinnerBancos.getText().toString().equalsIgnoreCase("")) {
            Log.w(TAG, "spinnerBancos: EMPTY");
            spinnerBancosLayout.setError("Seleccione un Banco");
            t = false;
        } else {
            spinnerBancosLayout.setError(null);
        }
        //Numero Tarjeta
        if (((cardNumber.getText().toString()).replaceAll(" ", "").length() != 16)) {
            Log.w(TAG, "numeroTarjeta: EMPTY");
            cardNumberInputLayout.setError("Ingrese un numero Valido");
            t = false;
        } else {
            cardNumberInputLayout.setError(null);
        }
        // Nombre Titular
        if ((nombreTitular.getText().toString()).replaceAll(" ", "").length() < 4) {
            Log.w(TAG, "nombreTitular: NO VALIDO");
            nombreTitularInputLayout.setError("Ingrese un nombre Valido");
            t = false;
        } else {
            nombreTitularInputLayout.setError(null);
        }
        //Fecha Expiracion
        String date = (expirationDate.getText().toString()).replaceAll(" ", "");
        if (date.length() < 4) {
            Log.w(TAG, "ExpirationDate is too SHORT");
            expirationDateInputLayout.setError("Fecha no valida");
            t = false;
        } else if (date.charAt(0) == '0'
                && (Character.getNumericValue(date.charAt(1)) > 9 || Character.getNumericValue(date.charAt(1)) < 1)) {
            Log.w(TAG, "Formato Fecha no Valido");
            expirationDateInputLayout.setError("Fecha no valida");

            t = false;
        } else if (date.charAt(0) == '1'
                && (Character.getNumericValue(date.charAt(1)) < 0 || Character.getNumericValue(date.charAt(1)) > 2)) {
            Log.w(TAG, "Formato Fecha no Valido");
            expirationDateInputLayout.setError("Fecha no valida");

            t = false;
        } else {
            expirationDateInputLayout.setError(null);
        }
        //Codigo
        if (cardCode.getText().toString().length() < 3) {
            Log.w(TAG, "Codigo Tarjeta Corto");
            cardCodeInputLayout.setError("Codigo no Valido");
            t = false;
        } else {
            cardCodeInputLayout.setError(null);
        }
        if (t) {
            Log.d(TAG, "Checking Completed. ACCEPTED");
        }
        return t;
    }


    private void sendPago(View v) {
        Log.d(TAG, "Sending Pago");
        Map<String, Object> data = new HashMap<>();
        String key = cardNumber.getText().toString().substring(15, 19);
        Log.e(TAG, "KEY" + key);
//        String key = cardNumber.getText().toString().substring(16, cardNumber.getText().toString().length());
        String banco = spinnerBancos.getText().toString();
        String numero = cardNumber.getText().toString();
        String nombre = nombreTitular.getText().toString();
        String fecha = expirationDate.getText().toString();
        String codigo = cardCode.getText().toString();
        data.put(key, new MedioDePago(banco, numero, nombre, fecha, codigo));
//        data.put("Timestamp", ServerValue.TIMESTAMP);
        metodosPagoReference.child(key + "0").setValue(data, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Log.d(TAG, "Metodo de pago Guardado EXITOSAMENTE");
                    Toast.makeText(v.getContext(), "Guardado Exitosamente", Toast.LENGTH_LONG).show();
                    clear(v);
                } else {
                    Log.w(TAG, "ERROR AL GUARDAR METODO DE PAGO::");
                    Toast.makeText(v.getContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void clear(View v){
        spinnerBancos.setText("");
        cardNumber.setText("");
        nombreTitular.setText("");
        expirationDate.setText("");
        cardCode.setText("");
    }

    private void hideKeyBoard(View view) {
        // Hide the keyboard when the button is pushed.
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}