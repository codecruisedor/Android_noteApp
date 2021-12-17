package com.nisarg.ListIt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class cardAdapter_taksLIst extends RecyclerView.Adapter<cardAdapter_taksLIst.cardViewHolder> {


    private ArrayList<cardHandler_tasklist> cardHandlerArrayList;
    ArrayList<cardHandler_tasklist> selectedItems;
    private Context context;
    private cardAdapter_taksLIst.OnItemClickListener mListener;


    public cardAdapter_taksLIst(ArrayList<cardHandler_tasklist> cardHandlersList, Context c) {
        cardHandlerArrayList = cardHandlersList;
        context = c;
    }

    public ArrayList<cardHandler_tasklist> getArrayList() {
        return this.cardHandlerArrayList;
    }

    public interface OnItemClickListener {
        void singleClicked_task(cardAdapter_taksLIst.cardViewHolder holder, cardHandler_tasklist card, int pos);

        void singleClicked_checkBox(cardAdapter_taksLIst.cardViewHolder holder, cardHandler_tasklist card, int pos);
    }

    public void setmListener(OnItemClickListener itemClickListener) {
        mListener = itemClickListener;
    }

    @NonNull
    @Override
    public cardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlecard_wishlist, parent, false);
        cardViewHolder holder = new cardViewHolder(view, mListener);
        return holder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull cardViewHolder holder, int position) {
        cardHandler_tasklist currentCard = cardHandlerArrayList.get(position);

        holder.task.setText(currentCard.getTask());
        holder.priority = currentCard.getPriority();


        if (holder.priority.equalsIgnoreCase("high")) {
            holder.task.setTextColor(ContextCompat.getColor(context, R.color.tasklist_high_pr));
        } else
            holder.task.setTextColor(ContextCompat.getColor(context, R.color.tasklist_mid_pr));

        holder.task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.singleClicked_task(holder, currentCard, position);
            }
        });
        holder.materialCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.singleClicked_checkBox(holder, currentCard, position);
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return cardHandlerArrayList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return cardHandlerArrayList.size();
    }

    public static class cardViewHolder extends RecyclerView.ViewHolder {

        public MaterialCheckBox materialCheckBox;
        public TextView task;
        public String priority;

        public cardViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            materialCheckBox = itemView.findViewById(R.id.checkbox_wish_list);
            task = itemView.findViewById(R.id.textView_wish_list);

        }
    }
}