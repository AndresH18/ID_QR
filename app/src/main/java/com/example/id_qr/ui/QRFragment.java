package com.example.id_qr.ui;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.id_qr.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QRFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QRFragment extends Fragment {

    private static final String TAG = "QR_Fragment";

    private static ImageView qrImageView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QRFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QRFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QRFragment newInstance(String param1, String param2) {
        QRFragment fragment = new QRFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_r, container, false);

        qrImageView = (ImageView) view.findViewById(R.id.qr_imageView);

        refrescarQR();


        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_q_r, container, false);
        return view;
    }

    private static String generateCode() {
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 30; i++) {
            sb.append(r.nextInt(101));
        }
        return sb.toString();
    }
    Color a = new Color();
    public static void refrescarQR() {
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
            qrImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.e(TAG, e.getLocalizedMessage());

        }

    }


}