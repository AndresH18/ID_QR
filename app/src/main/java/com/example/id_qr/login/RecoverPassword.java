package com.example.id_qr.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.id_qr.R;
import com.google.android.material.textfield.TextInputLayout;

public class RecoverPassword extends AppCompatActivity {
    private static final String TAG = "RecoverPassword";

    private final String recoverUrl = "https://eiadigital.eia.edu.co/sao/recuperarContrasena.do";

    private ConstraintLayout background_layout;
    private TextInputLayout user_layout;

    private Button btn;
    private EditText correoEditText;
    private Intent webRecoverIntent;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recover_password_activity);

        background_layout = findViewById(R.id.scrollView);
        user_layout = findViewById(R.id.textInputLayout_recover_field);

        btn = (Button) findViewById(R.id.recover_btn_layout_recoverP);
        correoEditText = findViewById(R.id.email_editText_Layout_recoverP);

//        Intent recoverIntent = getIntent();
//        if((email = recoverIntent.getStringExtra("email_user")) != null){
//            correoEditText.setText(email);
//        }
        if ((email = getIntent().getStringExtra("email_user")) != null) {
            correoEditText.setText(email);
        }
        webRecoverIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(recoverUrl));


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (correoEditText.getText().toString().endsWith("@eia.edu.co")) {
                    hideKeyBoard(v);
                    startActivity(webRecoverIntent);
                } else {
                    Toast toast = Toast.makeText(v.getContext(), "Correo No Autorizado", Toast.LENGTH_LONG);

                    toast.show();
                }
            }
        });

        background_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                layout.requestFocus();

                hideKeyBoard(v);
            }
        });
        correoEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (correoEditText.getText().toString().equals("")) {
                        user_layout.setError(null);
                    } else if (!correoEditText.getText().toString().endsWith("@eia.edu.co")) {
                        user_layout.setError("Correo no autorizado");
                    } else {
                        user_layout.setError(null);
                    }
                    Log.d(TAG, "correoEditText, hide keyboard");
                    hideKeyBoard(v);
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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