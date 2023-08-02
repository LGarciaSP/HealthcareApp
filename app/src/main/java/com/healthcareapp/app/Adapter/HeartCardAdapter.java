package com.healthcareapp.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.healthcareapp.app.Model.Card;
import com.healthcareapp.app.R;

import java.util.List;

public class HeartCardAdapter extends RecyclerView.Adapter<HeartCardAdapter.ViewHolder> {

    private Context mContext;
    private List <Card> mCards;

    public HeartCardAdapter(Context mContext, List <Card> mCards){
        this.mCards = mCards;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.heart_cards_layout, parent, false);

        return new HeartCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card card = mCards.get(position);
        holder.cardTitle.setText(card.getCardTitle());
        //holder.cardURL.setText(card.getCardURL());
        if (card.getCardImageURL().equals("default")){
            holder.cardImage.setImageResource(R.mipmap.nav_profile_round);
        }else{
            Glide.with(mContext).load(card.getCardImageURL()).into(holder.cardImage);
        }
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView cardTitle;
        public TextView cardURL;
        public ImageView cardImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardTitle = itemView.findViewById(R.id.card_title);
           // cardURL = itemView.findViewById(R.id.card_url);
            cardImage = itemView.findViewById(R.id.card_image);

        }
    }


}
