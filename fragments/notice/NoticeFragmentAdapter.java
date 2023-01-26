package com.example.kayit.fragments.notice;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kayit.R;
import com.example.kayit.faculty.notice.NoticeData;
import com.example.kayit.view.Admin;
import com.example.kayit.view.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class NoticeFragmentAdapter extends RecyclerView.Adapter<NoticeFragmentAdapter.NoticeFragmentViewHolder> {

    private Context context;
    private ArrayList<NoticeData> list;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;

    //constructor initialize edildi
    public NoticeFragmentAdapter(Context context, ArrayList<NoticeData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NoticeFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notice_feed_layout,parent,false);
        return new NoticeFragmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeFragmentViewHolder holder, int position) {
        final NoticeData currentItem = list.get(position);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getInstance().getReference();

        //holder ile elemanlar birbirine bağlandı
        holder.noticeFeedText.setText(currentItem.getTitle());
        holder.dateText.setText(currentItem.getDate());
        holder.timeText.setText(currentItem.getTime());
        try {
            if(currentItem.getImage() != null){
                //resim null değil ise layouta bağlandı
                Glide.with(context).load(currentItem.getImage()).into(holder.noticeFeedImage);
            }
        }catch (Exception e){

        }

        databaseReference.child("Admins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    if(ds.child("email").getValue().equals(firebaseUser.getEmail())){
                        //Eğer email bir admin hesap ise
                        holder.deleteButton.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Notice");
                reference.child(currentItem.getKey()).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Silindi", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Bir şeyler ters gitti", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NoticeFragmentViewHolder extends RecyclerView.ViewHolder {

        private TextView timeText,dateText,noticeFeedText;
        private ImageView noticeFeedImage;
        private Button deleteButton;
        public NoticeFragmentViewHolder(@NonNull View itemView) {
            super(itemView);

            timeText = itemView.findViewById(R.id.timeText);
            dateText = itemView.findViewById(R.id.dateText);
            noticeFeedText = itemView.findViewById(R.id.noticeFeedText);
            noticeFeedImage = itemView.findViewById(R.id.noticeFeedImage);
            deleteButton = itemView.findViewById(R.id.deleteFeedNotice);
        }
    }
}
