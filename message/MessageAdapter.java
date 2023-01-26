package com.example.kayit.message;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kayit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewAdapter> {

    private Context context;
    private ArrayList<MessageData> list;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;

    //constructor initialize edildi
    public MessageAdapter(Context context, ArrayList<MessageData> list,String username) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public MessageViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_user_layout,parent,false);
        return new MessageViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewAdapter holder, @SuppressLint("RecyclerView") int position) {

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        //holder ile layouta veri bağlandı
        holder.usernameMessageText.setText(list.get(position).getName());

        //item üstüne tıklanınca intent gerçekleşti
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MessageOther.class);
                intent.putExtra("username",firebaseUser.getEmail());
                intent.putExtra("otherUsername",list.get(position).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //search için filtreleme fonksiyonu initializ edildi
    public void filteredList(ArrayList<MessageData> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }

    public class MessageViewAdapter extends RecyclerView.ViewHolder {
        private TextView usernameMessageText;
        public MessageViewAdapter(@NonNull View itemView) {
            super(itemView);

            usernameMessageText = itemView.findViewById(R.id.usernameMessageText);
        }
    }
}
