package com.example.id_qr.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.id_qr.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


///HOLA

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistorialFragment_2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistorialFragment_2 extends Fragment {
    private static final String TAG = "HistorialFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView textView;
    private Button sunny;
    private Button foggy;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mConditionRef = mRootRef.child("votes/" + user.getUid());


    public HistorialFragment_2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistorialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistorialFragment_2 newInstance(String param1, String param2) {
        HistorialFragment_2 fragment = new HistorialFragment_2();
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

//        mConditionRef.keepSynced(true);
        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String text = snapshot.getValue(String.class);
                textView.setText(text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_historial, container, false);
        View view = inflater.inflate(R.layout.fragment_historial_2, container, false);

        textView = view.findViewById(R.id.textView_fragmentoHistorial_prueba);

        TextView ttt = view.findViewById(R.id.textView_fragmentoHistorial_prueba_2);
        DatabaseReference dd = FirebaseDatabase.getInstance().getReference().child("option").child("0");
        dd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ttt.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        sunny = view.findViewById(R.id.sunny_btn);
        sunny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConditionRef.setValue("Yes");
            }
        });
        foggy = view.findViewById(R.id.foggy_btn);
        foggy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mConditionRef.setValue("No");
                DatabaseReference d = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("A");
                d.setValue("hola");
            }
        });


        return view;
    }
}