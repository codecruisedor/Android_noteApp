package com.nisarg.ListIt;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class cardAdapter extends RecyclerView.Adapter<cardAdapter.cardViewHolder> {


    private ArrayList<cardHandler> cardHandlerArrayList;
    ArrayList<cardHandler> selectedItems;
    private OnItemClickListener mListener;

    public cardAdapter(ArrayList<cardHandler> cardHandlersList) {
        cardHandlerArrayList = cardHandlersList;
    }

    public interface OnItemClickListener {
        void singleClicked(cardViewHolder holder, cardHandler card);

        void longClicked(cardViewHolder holder, cardHandler card);
    }

    public void setmListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public ArrayList<cardHandler> getCardHandlerArrayList() {
        return cardHandlerArrayList;
    }

    @NonNull
    @Override
    public cardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        selectedItems = new ArrayList<>();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlecard, parent, false);
        cardViewHolder cardViewHolder = new cardViewHolder(view, mListener);


        return cardViewHolder;
    }

    void setFilter(ArrayList<cardHandler> FilteredDataList) {
        cardHandlerArrayList = FilteredDataList;
        notifyDataSetChanged();
    }

    public static class cardViewHolder extends RecyclerView.ViewHolder {

        //4
        public TextView title;
        public TextView content;
        public TextView date;
        LinearLayout card_layout;
        public ImageView imageView;


        public cardViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            title = itemView.findViewById(R.id.titleOfCard);
            content = itemView.findViewById(R.id.contentOfCard);
            card_layout = itemView.findViewById(R.id.cardlayout);
            date = itemView.findViewById(R.id.DateOfCreation);
            imageView = itemView.findViewById(R.id.selectedImage);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull cardViewHolder holder, int position) {
        cardHandler currentCard = cardHandlerArrayList.get(position);
        holder.content.setText(currentCard.getmContent());

        holder.title.setText(currentCard.getmTitle());
        holder.card_layout.setBackgroundColor(currentCard.getC());
        holder.date.setText(currentCard.getDate());
        holder.imageView.setVisibility(View.INVISIBLE);


        holder.card_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.singleClicked(holder, currentCard);
            }
        });

        holder.card_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mListener.longClicked(holder, currentCard);
                return true;
            }
        });

    }

    @Override
    public long getItemId(int position) {
        cardHandler currentCard = cardHandlerArrayList.get(position);
        return currentCard.getId();
    }

    @Override
    public int getItemCount() {
        return cardHandlerArrayList.size();
    }
}

