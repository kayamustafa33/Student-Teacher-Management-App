package com.example.kayit.fragments.faculty;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kayit.R;
import com.example.kayit.faculty.AddTeachers;
import com.example.kayit.faculty.TeacherAdapter;
import com.example.kayit.faculty.TeacherData;
import com.example.kayit.faculty.UpdateFaculty;
import com.example.kayit.view.Admin;
import com.example.kayit.view.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FacultyFragment extends Fragment {
    private RecyclerView csDepartment,fizyoterapiDepartment,grafikDepartment,muhasebeDepartment;
    private LinearLayout csNoData,fizyoNoData,grafikNoData,muhasebeNoData;
    private List<TeacherData> list1,list2,list3,list4;
    private TeacherAdapter adapter;
    private DatabaseReference reference,dbRef;
    FloatingActionButton fab;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_faculty, container, false);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        //layout elemanları initialize edildi
        csDepartment = view.findViewById(R.id.csDepartment);
        fizyoterapiDepartment = view.findViewById(R.id.fizyoterapiDepartment);
        grafikDepartment = view.findViewById(R.id.grafikDepartment);
        muhasebeDepartment = view.findViewById(R.id.muhasebeDepartment);


        csNoData = view.findViewById(R.id.csNoData);
        fizyoNoData = view.findViewById(R.id.fizyoNoData);
        grafikNoData = view.findViewById(R.id.grafikNoData);
        muhasebeNoData = view.findViewById(R.id.muhasebeNoData);

        reference = FirebaseDatabase.getInstance().getReference().child("öğretmen");

        //fonksiyonlar çağırıldı
        csDepartment();
        fizyoterapiDepartment();
        grafikDepartment();
        muhasebeDepartment();

        fab = view.findViewById(R.id.fab);

        checkThePerson();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), AddTeachers.class));
            }
        });
        return view;
    }

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
                    csDepartment.setLayoutManager(new LinearLayoutManager(requireView().getContext()));
                    adapter = new TeacherAdapter(list1, requireView().getContext(),"Bilgisayar Programcılığı");
                    csDepartment.setAdapter(adapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireView().getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

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
                    fizyoterapiDepartment.setLayoutManager(new LinearLayoutManager(requireView().getContext()));
                    adapter = new TeacherAdapter(list2,requireView().getContext(),"Fizyoterapi");
                    fizyoterapiDepartment.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireView().getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void grafikDepartment() {
        dbRef = reference.child("Grafik Tasarım");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list3 = new ArrayList<>();
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
                    grafikDepartment.setLayoutManager(new LinearLayoutManager(requireView().getContext()));
                    adapter = new TeacherAdapter(list3,requireView().getContext(),"Grafik Tasarım");
                    grafikDepartment.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireView().getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void muhasebeDepartment() {
        dbRef = reference.child("Muhasebe");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list4 = new ArrayList<>();
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
                    muhasebeDepartment.setLayoutManager(new LinearLayoutManager(requireView().getContext()));
                    adapter = new TeacherAdapter(list4,requireView().getContext(),"Muhasebe");
                    muhasebeDepartment.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireView().getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkThePerson(){

        //Admin veri tabanından referans alındı
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Admins");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int flag = 0;
                for(DataSnapshot ds : snapshot.getChildren()){
                    if(ds.child("email").getValue().equals(firebaseUser.getEmail())){
                        //Eğer email bir admin hesap ise
                        flag = 1;
                    }
                }
                if(flag == 1){
                    fab.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}