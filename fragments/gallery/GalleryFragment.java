package com.example.kayit.fragments.gallery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kayit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    RecyclerView convoRecycler, tatilRecycler,dersRecycler,genelRecycler;
    GalleryAdapter adapter;

    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_gallery, container, false);
        convoRecycler = view.findViewById(R.id.convoRecycler);
        tatilRecycler = view.findViewById(R.id.tatilRecycler);
        dersRecycler = view.findViewById(R.id.dersRecycler);
        genelRecycler = view.findViewById(R.id.genelRecycler);

        reference = FirebaseDatabase.getInstance().getReference().child("gallery");

        //fonksiyonlar çağırıldı
        getConvoImage();
        getTatilImage();
        getDersImage();
        getGenelImage();
        return view;
    }

    //genel adı altında resimler veritabanından çağırıldı
    private void getGenelImage() {
        reference.child("Genel").addValueEventListener(new ValueEventListener() {

            List<String> imageList = new ArrayList<>();
            //listeye verilen eklendi

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String data = (String) dataSnapshot.getValue();
                    imageList.add(data);
                }


                adapter = new GalleryAdapter(getContext(),imageList);
                genelRecycler.setLayoutManager(new GridLayoutManager(getContext(),3));
                genelRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //ders adı altında resimler veritabanından çağırıldı
    private void getDersImage() {
        reference.child("Ders").addValueEventListener(new ValueEventListener() {

            List<String> imageList = new ArrayList<>();
            //listeye veriler eklendi
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String data = (String) dataSnapshot.getValue();
                    imageList.add(data);
                }

                adapter = new GalleryAdapter(getContext(),imageList);
                dersRecycler.setLayoutManager(new GridLayoutManager(getContext(),3));
                dersRecycler.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //tatil adı altında resimler veritabanından çağırıldı
    private void getTatilImage() {
        reference.child("Tatil Günü").addValueEventListener(new ValueEventListener() {

            List<String> imageList = new ArrayList<>();
            //listeye veriler eklendi
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String data = (String) dataSnapshot.getValue();
                    imageList.add(data);
                }


                adapter = new GalleryAdapter(getContext(),imageList);
                tatilRecycler.setLayoutManager(new GridLayoutManager(getContext(),3));
                tatilRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //tolantı adı altında resimler veritabanından çağırıldı
    private void getConvoImage() {
        reference.child("Toplantı").addValueEventListener(new ValueEventListener() {

            List<String> imageList = new ArrayList<>();
            //listeye veriler eklendi
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String data = (String) dataSnapshot.getValue();
                    imageList.add(data);
                }


                adapter = new GalleryAdapter(getContext(),imageList);
                convoRecycler.setLayoutManager(new GridLayoutManager(getContext(),3));
                convoRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}