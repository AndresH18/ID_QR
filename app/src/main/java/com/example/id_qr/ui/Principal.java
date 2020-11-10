package com.example.id_qr.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.id_qr.R;
import com.example.id_qr.login.LoginMain;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Principal extends AppCompatActivity {

    private BottomNavigationView navView;
    private long backPressedTime;
    private Toast backToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        navView = findViewById(R.id.bottomNavigationView);
        /*
         * Usar esto si en el manifesto hay soporte para la actionBar
         */
        //AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
        //        R.id.fragmentQR, R.id.fragmentPago, R.id.fragmentRecarga, R.id.fragmentHistorial)
        //        .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        /*
         * Usar esto si en el manifesto hay soporte para la actionBar
         */
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

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

        if (navView.getSelectedItemId() == R.id.fragmentQR) {

            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                backToast.cancel();
                super.onBackPressed();
                System.exit(0);
                return;
            } else {
                backToast = Toast.makeText(Principal.this, "Press back again to exit", Toast.LENGTH_SHORT);
                backToast.show();
            }
            backPressedTime = System.currentTimeMillis();
        } else {
            navView.setSelectedItemId(R.id.fragmentQR);
        }
    }
}