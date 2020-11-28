package com.example.id_qr.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.id_qr.R;
import com.example.id_qr.ui.Principal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginMain extends AppCompatActivity {
    private static final String TAG = "LoginMain";

    private static final String USER_EMAIL = "USER_EMAIL";

    private FirebaseAuth mAuth;

    private boolean result = false;

    private Toast backToast;
    private long backPressedTime = 0;

    private ConstraintLayout background_layout;
    private TextInputLayout user_layout;
    private TextInputLayout password_layout;
    private EditText editTextUser;
    private TextView editTextPass;
    private Button btn_login;
    private Button btn_recuperarPassword;


    private String user;
    private String pass;

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser == null){
//            Toast.makeText(getApplicationContext(), "NO USER", Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(getApplicationContext(), "THERE is a user", Toast.LENGTH_SHORT).show();
//        }
//    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d(TAG, "Current user === existes");
            FirebaseAuth.getInstance().signOut();
            Log.d(TAG, "Current user: logout");
        } else {
            Log.d(TAG, "Current user === null");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        editTextUser = findViewById(R.id.login_username_editText);
        editTextPass = (EditText) findViewById(R.id.login_password_editText);
        btn_login = findViewById(R.id.login_btn_layout_login);
        btn_recuperarPassword = findViewById(R.id.recover_password_btn_layout_login);
        background_layout = findViewById(R.id.scrollView);
        user_layout = findViewById(R.id.textInputLayout_user_field);


        startActionListener();


        //FIXME: delete on completion
        String user = getIntent().getStringExtra(USER_EMAIL);
        if (user != null) {
            editTextUser.setText(user);
        } else {
//            editTextUser.setText("andres@eia.edu.co");
            editTextPass.setText("Andres");
        }

        mAuth = FirebaseAuth.getInstance();

    }

    public void recoverPassword(View view) {
        hideKeyBoard(view);
        // Intent intent = new Intent(this, RecoverPassword.class);
        Intent intent = new Intent(LoginMain.this, RecoverPassword.class);
        String s = editTextUser.getText().toString();
        intent.putExtra("email_user", s);
        startActivity(intent);
    }

    private void startActionListener() {
        background_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                layout.requestFocus();

                hideKeyBoard(v);
            }
        });
        editTextUser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if ((!hasFocus) && !editTextPass.isFocused()) {
                    Log.e(TAG, "editTextUser, hide keyboard");
                    hideKeyBoard(v);

                }
                if (!hasFocus) {
                    if (!editTextUser.getText().toString().endsWith("@eia.edu.co")) {
                        user_layout.setError("Correo no autorizado");
                    } else {
                        user_layout.setError(null);
                    }
                    if (!editTextPass.isFocused()) {

                    }
                }
            }
        });
        editTextPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if ((!hasFocus) && !editTextUser.isFocused()) {
                    Log.e(TAG, "editTextPassword, hide keyboard");
                    hideKeyBoard(v);
                }
            }
        });
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
                        Toast.makeText(v.getContext(), "Connecting...", Toast.LENGTH_SHORT).show();
                        /* StringBuilder sb = new StringBuilder();
                         * sb.append("user: ").append(editTextUser.getText().toString());
                         * sb.append("\n");
                         * sb.append("password: ").append(editTextPass.getText().toString());

                         * sb.append("user: ").append(user);
                         * sb.append("\n");
                         * sb.append("password: ").append(pass);

                         * textViewTemporal.setText(sb.toString());
                         */
                        hideKeyBoard(v);
                        //TODO
                        // if(verificarUsuario(user, pass)){
                        log_in(user, pass, v.getContext());
//                        if (true) {
//                        if (signIn(user, pass, v.getContext())) {
//                            hideKeyBoard(v);
//
//                            // Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
//                            // make intent for main activity
//                            Log.i(TAG, "Declare intent for \"Principal\"");
//
//                            Intent intent = new Intent(LoginMain.this, Principal.class);
//                            // start main Activity
//                            Log.i(TAG, "Start Intent for\"Principal\"");
//                            startActivity(intent);
//                            //finish login
//                            finish();
//                        }
                    } else {
                        //Correo no Termina en "@eia.edu.co"
                        Toast toast = Toast.makeText(v.getContext(), " Correo No Autorizado", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    // "user: " + editTextUser.getText().toString() + "\npassword: " + editTextPass.getText().toString()
                }
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
        } else {
            backToast = Toast.makeText(LoginMain.this, "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    private void log_in(String user, String pass, Context context) {
        Log.i(TAG, "signing in...");

        if (user.isEmpty()) {
            user = " ";
        }
        if (pass.isEmpty()) {
            pass = " ";
        }

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
//            Toast.makeText(context, "Conecting...", Toast.LENGTH_SHORT).show();
            // Create background thread to connect and get data
            mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
//                        FirebaseUser user = mAuth.getCurrentUser();

                        Log.i(TAG, "Declare intent for \"Principal\"");

                        Toast.makeText(context, "Redirecting...", Toast.LENGTH_LONG).show();
                        //
                        Intent intent = new Intent(LoginMain.this, Principal.class);
                        // start main Activity
                        Log.d(TAG, "Start Intent for\"Principal\"");
                        startActivity(intent);
                        //finish login
                        finish();
                    } else {
                        // If log_in in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(context, "Failed to log in", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } else {
            Log.w(TAG, "NO INTERNET CONECTION");
            Toast.makeText(context, "No se detecto Conexi" + (char) 243 + "n", Toast.LENGTH_LONG).show();

        }

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