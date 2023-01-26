package com.example.kayit.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kayit.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateFaculty extends AppCompatActivity {

    FloatingActionButton fab;
    private RecyclerView csDepartment,fizyoterapiDepartment,grafikDepartment,muhasebeDepartment;
    private LinearLayout csNoData,fizyoNoData,grafikNoData,muhasebeNoData;
    private List<TeacherData> list1,list2,list3,list4;
    private TeacherAdapter adapter;
    private DatabaseReference reference,dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Fakülte Güncelle");

        //layout elemanları initialize edildi
        csDepartment = findViewById(R.id.csDepartment);
        fizyoterapiDepartment = findViewById(R.id.fizyoterapiDepartment);
        grafikDepartment = findViewById(R.id.grafikDepartment);
        muhasebeDepartment = findViewById(R.id.muhasebeDepartment);


        csNoData = findViewById(R.id.csNoData);
        fizyoNoData = findViewById(R.id.fizyoNoData);
        grafikNoData = findViewById(R.id.grafikNoData);
        muhasebeNoData = findViewById(R.id.muhasebeNoData);

        //veri tabanı initialize edildi
        reference = FirebaseDatabase.getInstance().getReference().child("öğretmen");

        //fonksiyonları çağırdık
        csDepartment();
        fizyoterapiDepartment();
        grafikDepartment();
        muhasebeDepartment();


        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateFaculty.this, AddTeachers.class));
            }
        });
    }

    //cd bölümü veri çekme
    private void csDepartment() {
        dbRef = reference.child("Bilgisayar Programcılığı");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    csNoData.setVisibility(View.VISIBLE);
                    csDepartment.setVisibility(View.GONE);
                }else {

                    csNoData.setVisibility(View.GONE);
                    csDepartment.setVisibility(View.VISIBLE);

                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list1.add(data);
                    }
                    csDepartment.setHasFixedSize(true);
                    csDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list1,UpdateFaculty.this,"Bilgisayar Programcılığı");
                    csDepartment.setAdapter(adapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateFaculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    //fizyoterapi bölümü veri çekme
    private void fizyoterapiDepartment() {
        dbRef = reference.child("Fizyoterapi");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list2 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    fizyoNoData.setVisibility(View.VISIBLE);
                    fizyoterapiDepartment.setVisibility(View.GONE);
                }else {

                    fizyoNoData.setVisibility(View.GONE);
                    fizyoterapiDepartment.setVisibility(View.VISIBLE);

                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list2.add(data);
                    }
                    fizyoterapiDepartment.setHasFixedSize(true);
                    fizyoterapiDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list2,UpdateFaculty.this,"Fizyoterapi");
                    fizyoterapiDepartment.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateFaculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //grafik departmanı veri yazıldı
    private void grafikDepartment() {
        dbRef = reference.child("Grafik Tasarım");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list3 = new ArrayList<>();
                //listeye veriler eklendi
                if (!dataSnapshot.exists()){
                    grafikNoData.setVisibility(View.VISIBLE);
                    grafikDepartment.setVisibility(View.GONE);
                }else {

                    grafikNoData.setVisibility(View.GONE);
                    grafikDepartment.setVisibility(View.VISIBLE);

                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list3.add(data);
                    }
                    grafikDepartment.setHasFixedSize(true);
                    grafikDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list3,UpdateFaculty.this,"Grafik Tasarım");
                    grafikDepartment.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateFaculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //muhasebe bölümüne veri çekildi
    private void muhasebeDepartment() {
        dbRef = reference.child("Muhasebe");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list4 = new ArrayList<>();
                //veriler listeye eklendi
                if (!dataSnapshot.exists()){
                    muhasebeNoData.setVisibility(View.VISIBLE);
                    muhasebeDepartment.setVisibility(View.GONE);
                }else {

                    muhasebeNoData.setVisibility(View.GONE);
                    muhasebeDepartment.setVisibility(View.VISIBLE);

                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    muhasebeDepartment.setHasFixedSize(true);
                    muhasebeDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list4,UpdateFaculty.this,"Muhasebe");
                    muhasebeDepartment.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateFaculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}