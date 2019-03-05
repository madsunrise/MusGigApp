package wdx.musgig.venue_list;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import wdx.musgig.R;
import wdx.musgig.db.VenueModel;
import wdx.musgig.venue_full.DetailedActivity;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    private List<VenueModel> VenueModelList;
    private View.OnLongClickListener longClickListener;


    RecyclerViewAdapter(List<VenueModel> VenueModelList, View.OnLongClickListener longClickListener) {
        this.VenueModelList = VenueModelList;
        this.longClickListener = longClickListener;
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false));


    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, int position) {
        VenueModel VenueModel = VenueModelList.get(position);
        holder.nameTextView.setText(VenueModel.getName());
        holder.capacityTextView.setText("Вместимость: " + String.valueOf(VenueModel.getCapacity()) + " чел.");
        holder.priceTextView.setText("Залог: " + String.valueOf(VenueModel.getPrice()) + " руб.");
        holder.locationTextView.setText("Находится: " + VenueModel.getLocation());
        holder.ratingTextView.setText("Рейтинг: " + String.valueOf(VenueModel.getRating()) + " из 10");
        if (VenueModel.getPhoto() != null)
            holder.image.setImageURI(Uri.parse(VenueModel.getPhoto()));
        else
            holder.image.setImageResource(R.drawable.mezzo);
        holder.itemView.setTag(VenueModel);
        holder.itemView.setOnLongClickListener(longClickListener);


        holder.recycler_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Pair<View, String> pair1 = Pair.create((View) holder.image, holder.image.getTransitionName());
                    //  Pair<View, String> pair2 = Pair.create((View) holder.nameTextView, holder.nameTextView.getTransitionName());
                    Intent intent = new Intent(v.getContext(), DetailedActivity.class);
                    intent.putExtra("EXTRA_ID", String.valueOf(VenueModel.getId()));
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) v.getContext(), pair1);
                    v.getContext().startActivity(intent, optionsCompat.toBundle());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return VenueModelList.size();
    }

    void addItems(List<VenueModel> VenueModelList) {
        this.VenueModelList = VenueModelList;
        notifyDataSetChanged();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView capacityTextView;
        private TextView nameTextView;
        private TextView priceTextView;
        private TextView locationTextView;
        private TextView ratingTextView;
        private ImageView image;
        private LinearLayout recycler_item;

        RecyclerViewHolder(View view) {
            super(view);
            capacityTextView = view.findViewById(R.id.capacityTextView);
            nameTextView = view.findViewById(R.id.nameTextView);
            priceTextView = view.findViewById(R.id.priceTextView);
            locationTextView = view.findViewById(R.id.locationTextView);
            ratingTextView = view.findViewById(R.id.ratingTextView);
            image = view.findViewById(R.id.image);
            recycler_item = view.findViewById(R.id.recycler_item);

        }
    }


}