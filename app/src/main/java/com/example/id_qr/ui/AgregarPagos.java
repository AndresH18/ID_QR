package com.example.id_qr.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;

import com.example.id_qr.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AgregarPagos extends AppCompatActivity {

    private TextInputEditText cardNumber, nombreTitular, expirationDate, cardCode;
    private AutoCompleteTextView spinnerBancos;
    private ConstraintLayout backgroundLayout;
    private TextInputLayout spinnerBancosLayout, cardNumberInputLayout, nombreTitularInputLayout, expirationDateInputLayout, cardCodeInputLayout;

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

        cardNumber.addTextChangedListener(cardNumberWatcher);
        expirationDate.addTextChangedListener(cardExpirationWatcher);

    }

    private TextWatcher cardNumberWatcher = new TextWatcher() {
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

    private TextWatcher cardExpirationWatcher = new TextWatcher() {
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
}