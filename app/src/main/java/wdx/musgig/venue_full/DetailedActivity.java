package wdx.musgig.venue_full;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import wdx.musgig.R;

public class DetailedActivity extends AppCompatActivity {

    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private TextView capacity;
    private TextView nameTextView;
    private TextView max_price;
    private TextView location;
    private TextView rating;
    private ArrayList<Uri> ImagesArray = new ArrayList<Uri>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_item);
        capacity = findViewById(R.id.capacity);
        nameTextView = findViewById(R.id.nameTextView);
        max_price = findViewById(R.id.max_price);
        location = findViewById(R.id.location);
        rating = findViewById(R.id.rating);

        String id = getIntent().getStringExtra("EXTRA_ID");

        //    wdx.musgig.db.VenueGetIdViewModel venueGetIdViewModel = ViewModelProviders.of(this).get(VenueGetIdViewModel.class);
        //    venueGetIdViewModel.getItemById(id).observe(this, new Observer<VenueModel>() {
        //        @Override
        //      public void onChanged(@Nullable VenueModel VenueModel) {
        //          if (VenueModel != null) {
        //               nameTextView.setText(VenueModel.getName());
        //               capacity.setText(String.valueOf(VenueModel.getCapacity()));
        //               max_price.setText(String.valueOf(VenueModel.getPrice()));
        //               location.setText(VenueModel.getLocation() + ", Лубянский проезд, 25с1");
        //               rating.setText(String.valueOf(VenueModel.getRating()));
        //               if (VenueModel.getPhoto2() != null)
        //                   init_pager(Uri.parse(VenueModel.getPhoto2()), Uri.parse(VenueModel.getPhoto2()));
        //           }
        //       }
        //    });
    }


    private void init_pager(Uri... IMAGES) {
        Collections.addAll(ImagesArray, IMAGES);
        ViewPager mPager = findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(DetailedActivity.this, ImagesArray));
        final float density = getResources().getDisplayMetrics().density;
        NUM_PAGES = IMAGES.length;
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
    }
}
