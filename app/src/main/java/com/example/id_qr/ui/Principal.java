package com.example.id_qr.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.id_qr.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Principal extends AppCompatActivity {

    private BottomNavigationView navView;
    private long backPressedTime;
    private Toast backToast;
    private Handler mHandler;


    private final Fragment fragment1 = new QRFragment();
    private final Fragment fragment2 = new PagoFragment();
    private final Fragment fragment3 = new RecargaFragment();
    private final Fragment fragment4 = new HistorialFragment();
    private final FragmentManager fm = getSupportFragmentManager();
    private Fragment active = fragment1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        navView = findViewById(R.id.bottomNavigationView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mHandler = new Handler();


        /*usar esto en vez de Navcontroller y NavigationUI*/
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment1, "1").commit();
        /*
         * Usar esto si en el manifesto hay soporte para la actionBar
         */
        //AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
        //        R.id.fragmentQR, R.id.fragmentPago, R.id.fragmentRecarga, R.id.fragmentHistorial)
        //        .build();

        //// NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        /*
         * Usar esto si en el manifesto hay soporte para la actionBar
         */
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        ////NavigationUI.setupWithNavController(navView, navController);

        repeater();

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

    public void repeater() {
        Log.i("TAG", "repeater");
        mHandler.postDelayed(runable, 10);

    }

    public Runnable runable = new Runnable() {
        @Override
        public void run() {
            Log.i("TAG", "Runable");
            QRFragment.refrescarQR();
            int segundos = 10;
            mHandler.postDelayed(this, 1000 * segundos);
        }
    };

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.fragmentQR:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return true;

                case R.id.fragmentPago:
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;

                case R.id.fragmentRecarga:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    return true;
                case R.id.fragmentHistorial:
                    fm.beginTransaction().hide(active).show(fragment4).commit();
                    active = fragment4;
                    return true;
            }
            return false;
        }
    };
}