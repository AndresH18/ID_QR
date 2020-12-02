package com.example.id_qr.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.id_qr.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


///**
// * A simple {@link Fragment} subclass.
// * Use the {@link HistorialFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class HistorialFragment extends Fragment {
    private static final String TAG = "HistorialFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView textView;


    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private final DatabaseReference rootReference = database.getReference();
    private final DatabaseReference userReference = rootReference.child("Pruebas").child("users").child(firebaseUser.getUid());
    private final DatabaseReference parkingReference = userReference.child("Parqueadero");
    private final DatabaseReference historialReference = parkingReference.child("Historial");

    private SwipeRefreshLayout refreshLayout;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private final List<String> history = new ArrayList<>();


    public HistorialFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment HistorialFragment.
//     */
//    public static HistorialFragment newInstance(String param1, String param2) {
//        HistorialFragment fragment = new HistorialFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

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
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_historial, container, false);
        View view = inflater.inflate(R.layout.fragment_historial, container, false);

        refreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh() called from SwipeRefreshlayout");
                refreshLayout.setRefreshing(false);
            }
        });
        listView = view.findViewById(android.R.id.list);
        adapter = new ArrayAdapter(view.getContext(), R.layout.list_items, history);
        listView.setAdapter(adapter);

        LocalDateTime dateTime = LocalDateTime.now();
        final int date = (dateTime.getYear() * 10000) + (dateTime.getMonthValue() * 100) + (dateTime.getDayOfMonth());
//        final int time = (dateTime.getHour() * 10000) + (dateTime.getMinute() * 100) + (dateTime.getSecond());
        Log.d(TAG, String.valueOf(date));
//        Log.d(TAG, String.valueOf(time));
        final DatabaseReference dateReference = historialReference.child(String.valueOf(date));

        dateReference.orderByChild("Timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e(TAG, "DATACHANGED");
                history.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String time = dataSnapshot.getKey();
                    Object eventoObject = dataSnapshot.child("Event").child("tipo").getValue();
                    String evento = "Evento Vacio";
                    if(time == null){
                        time = "Tiempo Vacio";
                    }else{
                        String ss = time;
                        time = ss.substring(0,2).concat(":").concat(ss.substring(2,4));
                    }
                    if(eventoObject != null){
                        evento = eventoObject.toString();
                    }
                    history.add(time.concat("\t\t").concat(evento));
                }
                Collections.reverse(history);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}