package com.example.mobilalkfejl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HouseItemAdapter extends RecyclerView.Adapter<HouseItemAdapter.ViewHolder> implements Filterable {
    private ArrayList<HouseItem> houseItemsData;
    private ArrayList<HouseItem> houseItemsDataAll;
    private Context context;
    private int lastPosition = -1;

    HouseItemAdapter(Context context, ArrayList<HouseItem> houseItemsData) {
        this.context = context;
        this.houseItemsData = houseItemsData;
        this.houseItemsDataAll = houseItemsData;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(HouseItemAdapter.ViewHolder holder, int position) {
        HouseItem currentItem = houseItemsData.get(position);
        holder.bindTo(currentItem);

        if(holder.getBindingAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getBindingAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return houseItemsData.size();
    }

    @Override
    public Filter getFilter() {
        return houseItemFilter;
    }

    private Filter houseItemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<HouseItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.count = houseItemsDataAll.size();
                results.values = houseItemsDataAll;
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (HouseItem item : houseItemsDataAll) {
                    if (item.getAddress().toLowerCase().contains(filterPattern) || item.getDescription().toLowerCase().contains(filterPattern) || item.getPrice().toLowerCase().contains(filterPattern) || item.getContact().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
                results.values = filteredList;
                results.count = filteredList.size();
            }
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            houseItemsData.clear();
            houseItemsData.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mAddress;
        private TextView mDescription;
        private TextView mPrice;
        private TextView mContact;
        private ImageView mItemImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            mAddress = itemView.findViewById(R.id.maddress);
            mDescription = itemView.findViewById(R.id.mdescription);
            mPrice = itemView.findViewById(R.id.mprice);
            mContact = itemView.findViewById(R.id.mcontact);
            mItemImageView = itemView.findViewById(R.id.mImageView);
        }

        public void bindTo(HouseItem currentItem) {
            mAddress.setText(currentItem.getAddress());
            mDescription.setText(currentItem.getDescription());
            mPrice.setText(currentItem.getPrice());
            mContact.setText(currentItem.getContact());

            Glide.with(context).load(currentItem.getImageResource()).into(mItemImageView);

        }
    }
}

