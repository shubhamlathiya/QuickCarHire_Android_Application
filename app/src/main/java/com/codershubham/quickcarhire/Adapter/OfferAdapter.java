package com.codershubham.quickcarhire.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codershubham.quickcarhire.ClassFile.Offer;
import com.codershubham.quickcarhire.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {
    private Context context;
    private ArrayList<Offer> offerList;

    public OfferAdapter(Context context, ArrayList<Offer> offerList) {
        this.context = context;
        this.offerList = offerList;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_offers, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        Offer offer = offerList.get(position);
        holder.nameTextView.setText(offer.getName());
        holder.codeTextView.setText(offer.getCode());
        holder.offerPercentageTextView.setText(offer.getPercentage() + " %");
        holder.offerDateTextView.setText("End Date " + offer.getEndDate());

        Picasso.get().load(offer.getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView codeTextView,offerPercentageTextView,offerDateTextView;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.offerImageView);
            nameTextView = itemView.findViewById(R.id.offerNameTextView);
            codeTextView = itemView.findViewById(R.id.offerCodeTextView);
            offerPercentageTextView = itemView.findViewById(R.id.offerPercentageTextView);
            offerDateTextView = itemView.findViewById(R.id.offerDateTextView);
        }
    }
}