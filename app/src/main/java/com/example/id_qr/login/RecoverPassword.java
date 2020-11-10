package com.example.id_qr.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.id_qr.R;

public class RecoverPassword extends AppCompatActivity {
    private final String recoverUrl = "https://eiadigital.eia.edu.co/sao/recuperarContrasena.do";
    private Button btn;
    private EditText correoEditText;
    private Intent webRecoverIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        btn = (Button) findViewById(R.id.recover_btn_layout_recoverP);
        correoEditText = (EditText) findViewById(R.id.email_editText_Layout_recoverP);

        webRecoverIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(recoverUrl));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(correoEditText.getText().toString().endsWith("@eia.edu.co")) {
                    startActivity(webRecoverIntent);
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}