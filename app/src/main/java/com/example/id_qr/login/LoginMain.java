package com.example.id_qr.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.id_qr.R;
import com.example.id_qr.ui.Principal;

public class LoginMain extends AppCompatActivity {
    private static final String TAG = "LoginMain";



    private Toast backToast;
    private long backPressedTime = 0;

    private ImageView logoU;
    private TextView textViewTemporal;
    private EditText editTextUser;
    private TextView editTextPass;
    private Button btn_login;
    private Button btn_recuperarPassword;

    private String user;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_2);

        textViewTemporal = (TextView) findViewById(R.id.temporal_layout_login);
        editTextUser = (EditText) findViewById(R.id.login_username_editText);
        editTextPass = (EditText) findViewById(R.id.login_password_editText);
        logoU = (ImageView) findViewById(R.id.logo_U_layout_login);

        btn_login = (Button) findViewById(R.id.login_btn_layout_login);
        btn_recuperarPassword = (Button) findViewById(R.id.recover_password_btn_layout_login);

        startActionListener();

        editTextUser.setText("@eia.edu.co");
    }

    public void recoverPassword(View view) {
        hideKeyBoard(view);
        // Intent intent = new Intent(this, RecoverPassword.class);
        Intent intent = new Intent(LoginMain.this, RecoverPassword.class);
        startActivity(intent);
    }

    private void startActionListener() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                if (editTextUser != null && editTextPass != null) {
                    user = editTextUser.getText().toString();
                    if (user.endsWith("@eia.edu.co")) {
                        pass = editTextPass.getText().toString();
                        /* StringBuilder sb = new StringBuilder();
                         * sb.append("user: ").append(editTextUser.getText().toString());
                         * sb.append("\n");
                         * sb.append("password: ").append(editTextPass.getText().toString());

                         * sb.append("user: ").append(user);
                         * sb.append("\n");
                         * sb.append("password: ").append(pass);

                         * textViewTemporal.setText(sb.toString());
                         */
                        //TODO
                        // if(verificarUsuario(user, pass)){
                        if (true) {

                            hideKeyBoard(v);

                            // Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
                            // make intent for main activity
                            Log.i(TAG, "Declare intent for \"Principal\"");
                            Intent intent = new Intent(LoginMain.this, Principal.class);
                            // start main Activity
                            Log.i(TAG, "Start Intent for\"Principal\"");
                            startActivity(intent);
                            //finish login
                            finish();
                        }
                    } else {
                        //Correo no Termina en "@eia.edu.co"
                        Toast toast = Toast.makeText(v.getContext(), " Correo No Autorizado", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    // "user: " + editTextUser.getText().toString() + "\npassword: " + editTextPass.getText().toString()
                }
            }
        });

        editTextPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            /**
             * Called when the focus state of a view has changed.
             *
             * @param v        The view whose state has changed.
             * @param hasFocus The new focus state of v.
             */
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        editTextUser.setOnKeyListener(new View.OnKeyListener() {

            /**
             * Called when a hardware key is dispatched to a view. This allows listeners to
             * get a chance to respond before the target view.
             * <p>Key presses in software keyboards will generally NOT trigger this method,
             * although some may elect to do so in some situations. Do not assume a
             * software input method has to be key-based; even if it is, it may use key presses
             * in a different way than you expect, so there is no way to reliably catch soft
             * input key presses.
             *
             * @param v       The view the key has been dispatched to.
             * @param keyCode The code for the physical key that was pressed
             * @param event   The KeyEvent object containing full information about
             *                the event.
             * @return True if the listener has consumed the event, false otherwise.
             */
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }

        });
    }

    /**
     * Called when the activity has detected the user's press of the back
     * key. The {@link #getOnBackPressedDispatcher() OnBackPressedDispatcher} will be given a
     * chance to handle the back button before the default behavior of
     * {@link Activity#onBackPressed()} is invoked.
     *
     * @see #getOnBackPressedDispatcher()
     */
    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            System.exit(0);
            return;
        } else{
            backToast = Toast.makeText(LoginMain.this,"Press back again to exit",Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
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