package com.example.id_qr.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.example.id_qr.R;
import com.example.id_qr.login.LoginMain;
import com.example.id_qr.ui.back.SimpleAsyncTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Principal extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Bitmap> {
    private static final String TAG = "Principal";

    private static final String EMAIL_KEY = "USER_EMAIL";

    private FirebaseAuth mAuth;
    private FirebaseUser user;
//    private FirebaseDatabase firebaseDatabase;

    private Button btn_PagoNormal;

    private MaterialAlertDialogBuilder dialogBuilder;

    private Toolbar mToolbar;
    private BottomNavigationView bottomNavigationView;

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

        mAuth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        mToolbar = (Toolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(mToolbar);


//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //repeater();
        Log.d(TAG, "mRunnableQR start");
        mRunnableQR.run();
        Log.d(TAG, "mRunnableQR passed");

        /*usar esto en vez de Navcontroller y NavigationUI*/

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

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.account:
                        Log.d(TAG, "Item Account");
                        //TODO: Open account Preview
                        return true;
                    case R.id.ayuda:
                        Log.d(TAG, "Item Ayuda");
                        //TODO: Open Ayuda
                        return true;
                    case R.id.contactar_soporte:
                        Log.d(TAG, "Item contactar soporte");
                        //TODO: Open Contacto a soporte
                        return true;
                    case R.id.cerrar_sesion:
                        Log.d(TAG, "Item Cerrar Sesion");
                        //IMPROVE: Style
                        new MaterialAlertDialogBuilder(Principal.this).setTitle(getString(R.string.cerrar_sesion))
                                .setMessage(getString(R.string.desea_cerrar_sesion))
                                .setNeutralButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d(TAG, "Item Menu : Cerrar Sesion : DialogInterface : NegativeOption," + which);
                                    }
                                }).setPositiveButton(getString(R.string.confirmar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "Item Menu : Cerrar Sesion : DialogInterface : PositiveOption," + which);
                                //TODO: Cerrar Sesion
                                cerrarSesion();
                            }
                        }).show();
                        return true;

                    default:
                        return false;
                }
            }
        });

//        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.top_app_bar_menu, menu);
        return true;
    }


    @Override
    public void onBackPressed() {

        if (bottomNavigationView.getSelectedItemId() == R.id.fragmentQR) {

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
            bottomNavigationView.setSelectedItemId(R.id.fragmentQR);
        }
    }

    private void cerrarSesion() {
        user = mAuth.getCurrentUser();
        String email = user.getEmail();
//        Log.e(TAG, user.getEmail());
//        FirebaseAuth.getInstance().signOut();
        mAuth.signOut();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Log.i(TAG, "Sign out");
            mHandler.removeCallbacks(mRunnableQR);
            Toast t = Toast.makeText(getApplicationContext(), "Signed Out", Toast.LENGTH_SHORT);
            Intent intent = new Intent(Principal.this, LoginMain.class);
            intent.putExtra(EMAIL_KEY, email);
            t.show();
            startActivity(intent);
            finish();
        } else {
            Log.e(TAG, "FAILED TO LOG OUT");
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

//            final int numMinutos = 10;
//            final int minutos = 1000 * 60 * numMinutos;
//            mHandler.postDelayed(this, minutos);

        }
    };

//    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.fragmentQR:
//                    fm.beginTransaction().hide(active).show(fragmentQR).commit();
//                    active = fragmentQR;
//                    return true;
//
//                case R.id.fragmentPago:
//                    fm.beginTransaction().hide(active).show(fragmentPago).commit();
//                    active = fragmentPago;
//                    return true;
//
//                case R.id.fragmentRecarga:
//                    fm.beginTransaction().hide(active).show(fragmentRecarga).commit();
//                    active = fragmentRecarga;
//                    return true;
//                case R.id.fragmentHistorial:
//                    fm.beginTransaction().hide(active).show(fragmentHistorial).commit();
//                    active = fragmentHistorial;
//                    return true;
//            }
//            return false;
//        }
//    };


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