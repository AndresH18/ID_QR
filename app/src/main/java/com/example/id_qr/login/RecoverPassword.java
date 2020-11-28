package com.example.id_qr.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.id_qr.R;

public class RecoverPassword extends AppCompatActivity {
    private final String recoverUrl = "https://eiadigital.eia.edu.co/sao/recuperarContrasena.do";


    private Button btn;
    private EditText correoEditText;
    private Intent webRecoverIntent;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recover_password_activity);

        btn = (Button) findViewById(R.id.recover_btn_layout_recoverP);
        correoEditText = (EditText) findViewById(R.id.email_editText_Layout_recoverP);

//        Intent recoverIntent = getIntent();
//        if((email = recoverIntent.getStringExtra("email_user")) != null){
//            correoEditText.setText(email);
//        }
        if((email = getIntent().getStringExtra("email_user")) != null){
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