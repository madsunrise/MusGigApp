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
import wdx.musgig.venue_full.DetailedActivity;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    private List<wdx.musgig.retrofit.Venues> Venues;


    RecyclerViewAdapter(List<wdx.musgig.retrofit.Venues> Venues) {
        this.Venues = Venues;
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false));


    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, int position) {
        wdx.musgig.retrofit.Venues Venue = Venues.get(position);
        holder.nameTextView.setText(Venue.getName());
        holder.capacityTextView.setText("Вместимость: " + String.valueOf(Venue.getCapacity()) + " чел.");
        holder.priceTextView.setText("Залог: " + String.valueOf(Venue.getPrice()) + " руб.");
        holder.locationTextView.setText("Находится: " + Venue.getLocation());
        holder.ratingTextView.setText("Рейтинг: " + String.valueOf(Venue.getRating()) + " из 10");
        if (Venue.getPhoto() != null)
            holder.image.setImageURI(Uri.parse(Venue.getPhoto()));
        else
            holder.image.setImageResource(R.drawable.mezzo);
        //     holder.itemView.setTag(VenueModel);
        //      holder.itemView.setOnLongClickListener(longClickListener);


        holder.recycler_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Pair<View, String> pair1 = Pair.create((View) holder.image, holder.image.getTransitionName());
                    //  Pair<View, String> pair2 = Pair.create((View) holder.nameTextView, holder.nameTextView.getTransitionName());
                    Intent intent = new Intent(v.getContext(), DetailedActivity.class);
                    intent.putExtra("EXTRA_ID", String.valueOf(Venue.getId()));
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) v.getContext(), pair1);
                    v.getContext().startActivity(intent, optionsCompat.toBundle());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        if (Venues == null)
            return 0;
        return Venues.size();
    }

    void addItems(List<wdx.musgig.retrofit.Venues> Venues) {
        this.Venues = Venues;
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