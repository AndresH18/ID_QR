package com.example.id_qr.ui.back;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Random;


public class QR_Generator extends AsyncTaskLoader<Bitmap> {
    private static final String TAG = "QR_AsyncTask";

    //TODO: Actualizar para que se consiga la hora de la base de datos para la generacion del QR

//    private ImageView imageView;
//    public SimpleAsyncTask(@NonNull Context context, ImageView imageView) {
    public QR_Generator(@NonNull Context context) {
        super(context);
//        this.imageView = imageView;
    }

    @Nullable
    @Override
    public Bitmap loadInBackground() {
//        Bitmap bitmap = generarQR();
//        return bitmap;
        return generarQR();
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    private String generateCode() {
        Log.d(TAG, "Generate Code");

        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 30; i++) {
            sb.append(r.nextInt(101));
        }

//        DatabaseReference a = FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
//        a.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                double offset = snapshot.getValue(Double.class);
//                double estimated = System.currentTimeMillis() - offset;
//
//                Log.e(TAG, String.valueOf(estimated));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        return sb.toString();
    }

    public Bitmap generarQR() {
        Log.d(TAG, "Refrescar QR()");

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        String s = generateCode();

        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(s, BarcodeFormat.QR_CODE, 450, 450);
            Bitmap bitmap = Bitmap.createBitmap(450, 450, Bitmap.Config.RGB_565);
            for (int x = 0; x < 450; x++) {
                for (int y = 0; y < 450; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.parseColor("#E5F1FF"));
                }
            }
            Log.d(TAG, "generar QR: SUCCESFULL");
            return bitmap;
        } catch (WriterException e) {
            Log.e(TAG, "generar QR: FAILED", e);
            Log.e(TAG, e.getLocalizedMessage());
        }
        return null;
    }
}
