package com.example.kayit.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kayit.R;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewAdapter> {

    private Context context;
    private ArrayList<ChatData> list;

    //constructor initialize edildi
    public ChatAdapter(Context context, ArrayList<ChatData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChatViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_feed_layout,parent,false);
        return new ChatViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewAdapter holder, int position) {
        //holder ile layouta veri bağlandı
        holder.chatText.setText(list.get(position).getText());
    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    //search için filtreleme fonsiyonu eklendi
    public void filteredList(ArrayList<ChatData> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }

    public class ChatViewAdapter extends RecyclerView.ViewHolder {

        private TextView chatText,usernameText;

        public ChatViewAdapter(@NonNull View itemView) {
            super(itemView);

            chatText = itemView.findViewById(R.id.chatText);

        }
    }
}
