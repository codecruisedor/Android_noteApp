package com.nisarg.ListIt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class cardAdapterShoppingElement extends RecyclerView.Adapter<cardAdapterShoppingElement.cardViewHolder> {

    private ArrayList<cardHanlder_shoppingCard> cardHandlerArrayList;

    private OnItemClickListener mListener;

    public cardAdapterShoppingElement(ArrayList<cardHanlder_shoppingCard> cardHandlersList) {
        cardHandlerArrayList = cardHandlersList;
    }

    public interface OnItemClickListener {
        void singleClicked(cardViewHolder holder, cardHanlder_shoppingCard card);

    }

    public void setmListener(cardAdapterShoppingElement.OnItemClickListener listener) {
        mListener = listener;
    }

    public ArrayList<cardHanlder_shoppingCard> getCardHandlerArrayList() {
        return cardHandlerArrayList;
    }

    public static class cardViewHolder extends RecyclerView.ViewHolder {

        //4
        public TextView itemName;
        public TextView price;
        public TextView quantity;
        public TextView size;
        public View divider;
        public LinearLayout shoppingItemLayout;

        public cardViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            itemName = itemView.findViewById(R.id.itemName);
            price = itemView.findViewById(R.id.price);
            divider = itemView.findViewById(R.id.divider_shopping_item);
            quantity = itemView.findViewById(R.id.quantity);
            shoppingItemLayout = itemView.findViewById(R.id.shoppingItemLayout);
            size = itemView.findViewById(R.id.size);

        }
    }


    @NonNull
    @Override
    public cardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleshoppinglistelement, parent, false);
        cardViewHolder holder = new cardViewHolder(view, mListener);

        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull cardViewHolder holder, int position) {

        cardHanlder_shoppingCard currentCard = cardHandlerArrayList.get(position);
        holder.divider.setBackgroundColor(currentCard.getMcolor());
        holder.itemName.setText(currentCard.getmItemName());
        holder.price.setText(currentCard.getmPrice());
        holder.quantity.append(currentCard.getmQuant());
        holder.size.append(currentCard.getmSize());


        holder.shoppingItemLayout.setOnClickListener(new View.OnClickListener() {
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

    public void setFilter(ArrayList<cardHanlder_shoppingCard> filteredList) {
        cardHandlerArrayList = filteredList;
        notifyDataSetChanged();
    }
}
