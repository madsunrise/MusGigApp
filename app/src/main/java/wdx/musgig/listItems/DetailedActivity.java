package wdx.musgig.listItems;


import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import wdx.musgig.R;
import wdx.musgig.db.VenueGetIdViewModel;
import wdx.musgig.db.VenueModel;

public class DetailedActivity extends AppCompatActivity {

    private TextView capacityTextView;
    private TextView nameTextView;
    private TextView priceTextView;
    private TextView locationTextView;
    private TextView ratingTextView;
    private ImageView image;
    private VenueGetIdViewModel VenueGetIdViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_item);
        capacityTextView = findViewById(R.id.capacityTextView);
        nameTextView = findViewById(R.id.nameTextView);
        priceTextView = findViewById(R.id.priceTextView);
        locationTextView = findViewById(R.id.locationTextView);
        ratingTextView = findViewById(R.id.ratingTextView);
        image = findViewById(R.id.image);

        String id = getIntent().getStringExtra("EXTRA_ID");
        VenueGetIdViewModel = ViewModelProviders.of(this).get(VenueGetIdViewModel.class);


        VenueModel VenueModel = VenueGetIdViewModel.getItemById(id);
        nameTextView.setText(VenueModel.getName());
        capacityTextView.setText("Вместимость: " + VenueModel.getCapacity() + " чел.");
        priceTextView.setText("Залог: " + VenueModel.getPrice() + " руб.");
        locationTextView.setText("Находится: " + VenueModel.getLocation());
        ratingTextView.setText("Рейтинг: " + VenueModel.getRating() + " из 10");
        if (VenueModel.getPhoto() != null)
            image.setImageURI(Uri.parse(VenueModel.getPhoto()));
        else
            image.setImageResource(R.drawable.mezzo);
    }

}


