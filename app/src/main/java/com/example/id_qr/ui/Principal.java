package com.example.id_qr.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.example.id_qr.R;
import com.example.id_qr.ui.back.SimpleAsyncTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Principal extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Bitmap> {

    private static final String TAG = "Principal";

//    private FirebaseAuth mAuth;
//    private FirebaseUser user;

    private Button btn_PagoNormal;

    private BottomNavigationView navView;
    private long backPressedTime;
    private Toast backToast;
    private Handler mHandler = new Handler();


    private Fragment fragmentQR = new QRFragment();
    private Fragment fragmentPago = new PagoFragment();
    private Fragment fragmentRecarga = new RecargaFragment();
    private Fragment fragmentHistorial = new HistorialFragment();
    private FragmentManager fm = getSupportFragmentManager();
    private Fragment active = fragmentQR;

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        FirebaseAuth.getInstance().signOut();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
////        if(currentUser == null) {
//        Log.i(TAG, "Sign out");
//        Toast.makeText(getApplicationContext(), "Sign out", Toast.LENGTH_SHORT).show();
////        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        navView = findViewById(R.id.bottomNavigationView);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //repeater();
        Log.d(TAG, "mRunnableQR start");
        mRunnableQR.run();
        Log.d(TAG, "mRunnableQR passed");

        /*usar esto en vez de Navcontroller y NavigationUI*/
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        fm.beginTransaction().add(R.id.nav_host_fragment, fragmentHistorial, "4").hide(fragmentHistorial).commit();
//        fm.beginTransaction().add(R.id.nav_host_fragment, fragmentRecarga, "3").hide(fragmentRecarga).commit();
//        fm.beginTransaction().add(R.id.nav_host_fragment, fragmentPago, "2").hide(fragmentPago).commit();
//        fm.beginTransaction().add(R.id.nav_host_fragment, fragmentQR, "1").hide(fragmentQR).commit();
//        fm.beginTransaction().show(active).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragmentHistorial, "4").hide(fragmentHistorial)
                .add(R.id.nav_host_fragment, fragmentRecarga, "3").hide(fragmentRecarga)
                .add(R.id.nav_host_fragment, fragmentPago, "2").hide(fragmentPago)
                .add(R.id.nav_host_fragment, fragmentQR, "1").hide(fragmentQR).show(active).commit();
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

//        if(getSupportLoaderManager().getLoader(0)!=null){
//            getSupportLoaderManager().initLoader(0,null,this);
//        }
        if (LoaderManager.getInstance(this).getLoader(0) != null) {
            LoaderManager.getInstance(this).initLoader(0, null, this);
        }
        refrescarQR();

//        user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            Log.e(TAG, "User is signed in");
//        }


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
            Log.d(TAG, "RUNNABLE: REFERESCAR QR");
//            QRFragment.refrescarQR();

            //TODO refrescar qr
            refrescarQR();

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


    private void refrescarQR() {
//        LoaderManager.getInstance(this);
//        getSupportLoaderManager().restartLoader(0, null, this);
        LoaderManager.getInstance(this).restartLoader(0, null, this);

    }

    @NonNull
    @Override
    public Loader<Bitmap> onCreateLoader(int id, @Nullable Bundle args) {

        if (id == 0) {
//            return new SimpleAsyncTask(this, QRFragment.getQrImageView());
            return new SimpleAsyncTask(this);
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Bitmap> loader, Bitmap data) {
        if (QRFragment.getQrImageView() != null) {
            Log.d(TAG, "onLoadFinished()");
            QRFragment.getQrImageView().setImageBitmap(data);

        } else {
            Log.e(TAG, "onLoadFinished(): QR_IMAGE_VIEW IS NULL");
        }
//        ((ImageView) findViewById(R.id.qr_imageView)).setImageBitmap(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Bitmap> loader) {
        // For now leave empty
    }


}