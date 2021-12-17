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

public class cardAdapter_locked extends RecyclerView.Adapter<cardAdapter_locked.cardViewHolder> {

    private ArrayList<cardHandler> cardHandlerArrayList, FilteredDataList;

    private OnItemClickListener mListener;

    public cardAdapter_locked(ArrayList<cardHandler> cardHandlersList) {
        cardHandlerArrayList = cardHandlersList;
    }

    public interface OnItemClickListener {
        void singleClicked(cardViewHolder holder, cardHandler card);
    }

    public void setmListener(OnItemClickListener listener) {
        mListener = listener;
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


    @NonNull
    @Override
    public cardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlecard, parent, false);
        cardViewHolder holder = new cardViewHolder(view, mListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull cardViewHolder holder, int position) {
        cardHandler currentCard = cardHandlerArrayList.get(position);
        holder.content.setText(currentCard.getmContent());
        holder.content.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                int count = ((TextView) view).getLineCount();
                if (count > 2)
                    holder.content.setText(currentCard.getmContent().concat("..."));
            }
        });

        holder.title.setText(currentCard.getmTitle());
        holder.card_layout.setBackgroundColor(currentCard.getC());
        holder.date.setText(currentCard.getDate());
        holder.imageView.setVisibility(View.GONE);

        holder.card_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.singleClicked(holder, currentCard);
            }
        });


    }

    @Override
    public int getItemCount() {
        return cardHandlerArrayList.size();
    }

    public void setFilter(ArrayList<cardHandler> filteredList) {
        cardHandlerArrayList = filteredList;
        notifyDataSetChanged();
    }
}
