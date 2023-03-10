package com.example.kayit.fragments.notice;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kayit.R;
import com.example.kayit.faculty.notice.NoticeData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class NoticeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<NoticeData> list;
    private DatabaseReference databaseReference;
    private NoticeFragmentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notice, container, false);
        list = new ArrayList<>();
        //veritabanı initialize edildi
        recyclerView = view.findViewById(R.id.recyclerNoticeFeed);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notice");

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

        getData(view);
        return view;

    }

    //veritabanından veriler alındı
    private void getData(View view) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notice");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    //listeye veriler eklendi
                    NoticeData data = dataSnapshot.getValue(NoticeData.class);
                    list.add(data);
                }

                //layouta bağlandı
                adapter = new NoticeFragmentAdapter(view.getContext(),list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(view.getContext(), "Bir hata oluştu!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}