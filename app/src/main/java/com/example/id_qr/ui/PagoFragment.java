package com.example.id_qr.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.id_qr.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PagoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PagoFragment extends Fragment {
    private static final String TAG = "PagoFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


//    private FirebaseAuth mAuth;
//    private FirebaseUser mUser;
//    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    private Button btn_PagoNormal;
    private Button btn_PagoDia;
    private Button btn_PagoTransporte;

    private ImageButton ib_pagoNormal;
    private ImageButton ib_pagoDia;
    private ImageButton ib_pagoTransporte;


    public PagoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PagoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PagoFragment newInstance(String param1, String param2) {
        PagoFragment fragment = new PagoFragment();
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
//        DatabaseReference conditionReference = mRootRef.child("condition");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pago_fragment, container, false);

        Log.d(TAG, "ATTEMPTING TO ASSIGN R.id.btn_pago_dia_normal");
        btn_PagoNormal = (Button) view.findViewById(R.id.btn_pago_dia_normal);
        if (btn_PagoNormal == null) {
            Log.e(TAG, "FAILED TO ASSIGN R.id.btn_pago_dia_normal");
        } else {
            Log.d(TAG, "R.id.btn_pago_dia_normal Assigned");
        }

        btn_PagoDia = (Button) view.findViewById(R.id.btn_pago_dia_completo);
        btn_PagoTransporte = (Button) view.findViewById(R.id.btn_pago_transporte_eia);

        ib_pagoNormal = (ImageButton) view.findViewById(R.id.ib_pago_dia_normal);
        ib_pagoDia = (ImageButton) view.findViewById(R.id.ib_pago_dia_completo);
        ib_pagoTransporte = (ImageButton) view.findViewById(R.id.ib_pago_transporte);

        startActions();


        //
//        mUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (mUser != null) {
//            Log.e(TAG, "User is signed in");
//        }
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_pago, container, false);
        return view;
    }

    private void pagoNormal() {
        System.out.println("Pago Dia Normal");

//        if (mUser != null) {
//            // Agregar a fecha
//            // [fecha hoy] :
//        }
    }

    private void pagoDia() {
        System.out.println("Pago Dia Completo");
    }

    private void pagoTransporte() {
        System.out.println("Pago Transporte");
    }


    private void startActions() {

        btn_PagoNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagoNormal();
            }
        });

        ib_pagoNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagoNormal();
            }
        });

        btn_PagoDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagoDia();
            }
        });

        ib_pagoDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagoDia();
            }
        });

        btn_PagoTransporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagoTransporte();
            }
        });

        ib_pagoTransporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagoTransporte();
            }
        });
    }

}