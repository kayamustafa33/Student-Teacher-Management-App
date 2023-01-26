package com.example.kayit.message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kayit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.protobuf.StringValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MessageOther extends AppCompatActivity {

    String userName=null,otherName=null;
    ImageView sendMessageBtn;
    EditText messageEditText;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String mesaj="";
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private ArrayList<ChatData> arrayList;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_other);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //list ve elemanlar initialize edildi
        arrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_chat);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(this,arrayList);
        recyclerView.setAdapter(adapter);


        //bundle ile veriler alındı
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userName = extras.getString("username");
            otherName = extras.getString("otherUsername");

        }

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        sendMessageBtn = findViewById(R.id.sendMessageBtn);
        messageEditText = findViewById(R.id.messageEditText);

        //app bar title ayarlandı
        if(otherName != null){
            getSupportActionBar().setTitle(otherName);
        }

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mesaj = messageEditText.getText().toString();
                messageEditText.setText("");
                if(!(mesaj.isEmpty())){
                    mesajGonder(mesaj);
                }
            }
        });

        loadMesaj();

    }

    public void mesajGonder(String text){
        //veritabanına veri yazılıp map ile veride tutuldu.
        String key = databaseReference.child("mesajlar").child(firebaseUser.getUid()).child(otherName).push().getKey();

        Map<String,Object> messageMap = new HashMap<>();
        messageMap.put("text",text);
        messageMap.put("from",userName);
        firebaseUser = auth.getCurrentUser();
        databaseReference.child("mesajlar").child(firebaseUser.getUid()).child(otherName).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    databaseReference.child("mesajlar").child(otherName).child(firebaseUser.getUid()).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            }
        });
    }

    //veriler recyclerView ile ekrana bastırıldı
    public void loadMesaj(){
        databaseReference.child("mesajlar").child(firebaseUser.getUid()).child(otherName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChatData chatData = snapshot.getValue(ChatData.class);
                arrayList.add(chatData);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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

        //searchView initialize edildi
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

    //elemanlar filtrelenip listeye eklendi
    private void filter(String newText) {
        ArrayList<ChatData> filteredList = new ArrayList<>();
        for(ChatData item : arrayList){
            if(item.getText().toLowerCase().contains(newText)){
                filteredList.add(item);
            }
        }
        adapter.filteredList(filteredList);
    }
}