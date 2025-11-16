package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.MessageModel;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    private ArrayList<MessageModel> messages;

    public MessageAdapter(ArrayList<MessageModel> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_message_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int i) {
        MessageModel model = messages.get(i);
        holder.phoneNumberView.setText(model.getPhoneNumber());
        holder.messageView.setText(model.getMessage());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void updateList(ArrayList<MessageModel> newList){
        this.messages = newList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView phoneNumberView, messageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneNumberView = itemView.findViewById(R.id.phoneNumberView);
            messageView = itemView.findViewById(R.id.messageView);
        }
    }
}
