package com.example.kayit.message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kayit.R;
import com.example.kayit.ebook.EbookData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class MessageActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private ArrayList<MessageData> list;
    private MessageAdapter adapter;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    String userName="";
    ImageView messageImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Mesajlar");

        //liste initialize edildi
        list = new ArrayList<>();

        //veritabanı initialize edildi
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        messageImage = findViewById(R.id.messageImage);

        //recylerView initialize edildi
        recyclerView = findViewById(R.id.recyclerUserList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        getData();
    }

    private void getData() {
        //veritabanından veriler alındı
        databaseReference = FirebaseDatabase.getInstance().getReference().child("TotalPerson");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(!(dataSnapshot.child("email").getValue().equals(firebaseUser.getEmail()))){
                        //listeye veriler eklendi
                        MessageData messageData = dataSnapshot.getValue(MessageData.class);
                        list.add(messageData);

                    }
                }

                checkUserName();
                //layouta bağlanıldı
                adapter = new MessageAdapter(MessageActivity.this,list,userName);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MessageActivity.this, "Bir hata oluştu!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //kullanıcı admin mi yoksa öğrenci mi kontrol edildi
    public void checkUserName(){
        databaseReference.child("Admins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.child("email").getValue().equals(firebaseUser.getEmail())){
                        userName = dataSnapshot.child("name").getValue(String.class);
                        //intent ile veriler alındı
                        Bundle extras = getIntent().getExtras();
                        if (extras != null) {
                            userName = extras.getString("username");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("Students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.child("email").getValue().equals(firebaseUser.getEmail())){
                        String userName = dataSnapshot.child("name").getValue(String.class);

                        //veriler intent ile transfer edildi
                        Intent intent = new Intent();
                        intent.putExtra("username",userName);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //menu initialize edildi
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_item,menu);
        MenuItem menuItem = menu.findItem(R.id.nav_search);

        //searcview initialize edildi
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Bir şeyler ara...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //listedeki veriler küçük harfe çevrilip listeye eklendi
    private void filter(String newText) {
        ArrayList<MessageData> filteredList = new ArrayList<>();
        for(MessageData item : list){
            if(item.getName().toLowerCase().contains(newText)){
                filteredList.add(item);
            }
        }
        adapter.filteredList(filteredList);
    }
}