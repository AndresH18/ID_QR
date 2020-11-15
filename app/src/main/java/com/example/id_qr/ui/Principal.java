package com.example.id_qr.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.id_qr.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Principal extends AppCompatActivity {
    private static final String TAG = "Principal";

    private Button btn_PagoNormal;

    private BottomNavigationView navView;
    private long backPressedTime;
    private Toast backToast;
    private Handler mHandler = new Handler();


    private final Fragment fragmentQR = new QRFragment();
    private final Fragment fragmentPago = new PagoFragment();
    private final Fragment fragmentRecarga = new RecargaFragment();
    private final Fragment fragmentHistorial = new HistorialFragment();
    private final FragmentManager fm = getSupportFragmentManager();
    private Fragment active = fragmentQR;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        navView = findViewById(R.id.bottomNavigationView);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //repeater();
        Log.e(TAG, "mRunnableQR start");
        mRunnableQR.run();
        Log.e(TAG, "mRunnableQR passed");

        /*usar esto en vez de Navcontroller y NavigationUI*/
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fm.beginTransaction().add(R.id.nav_host_fragment, fragmentHistorial, "4").hide(fragmentHistorial).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragmentRecarga, "3").hide(fragmentRecarga).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragmentPago, "2").hide(fragmentPago).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragmentQR, "1").hide(fragmentQR).commit();
        fm.beginTransaction().show(active);
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

//        repeater();



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

   /* private void repeater() {
        Log.i(TAG, "REPEATER");
        mHandler.postDelayed(mRunnableQR, 10);
    }*/

    private Runnable mRunnableQR = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "RUNNABLE: REFERESCAR QR");
//            QRFragment.refrescarQR();

            //TODO refrescar qr

            final int segundos = 5;

            mHandler.postDelayed(this, 1000 * segundos);
        }
    };

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.fragmentQR:
                    fm.beginTransaction().hide(active).show(fragmentQR).commit();
                    active = fragmentQR;
                    return true;

                case R.id.fragmentPago:
                    fm.beginTransaction().hide(active).show(fragmentPago).commit();
                    active = fragmentPago;
                    return true;

                case R.id.fragmentRecarga:
                    fm.beginTransaction().hide(active).show(fragmentRecarga).commit();
                    active = fragmentRecarga;
                    return true;
                case R.id.fragmentHistorial:
                    fm.beginTransaction().hide(active).show(fragmentHistorial).commit();
                    active = fragmentHistorial;
                    return true;
            }
            return false;
        }
    };




}