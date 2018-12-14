package wdx.musgig.addItem;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.myhexaville.smartimagepicker.ImagePicker;

import wdx.musgig.R;
import wdx.musgig.db.VenueModel;

public class AddActivity extends AppCompatActivity {

    private EditText capacity;
    private EditText name;
    private EditText price;
    private EditText location;
    private EditText rating;
    private ImageView pickImage;
    private String imageString;
    private AddVenueViewModel addVenueViewModel;
    private ImagePicker imagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        capacity = findViewById(R.id.capacity);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        location = findViewById(R.id.location);
        rating = findViewById(R.id.rating);
        pickImage = findViewById(R.id.pickImage);

        imagePicker = new ImagePicker(this,
                null,
                imageUri -> {
                    imageString = imageUri.toString();
                    pickImage.setImageURI(imageUri);
                    Base64();
                })
                .setWithImageCrop(
                        1,
                        1);


        addVenueViewModel = ViewModelProviders.of(this).get(AddVenueViewModel.class);
    }

    public void addButton(View view) {
        if ((capacity.getText().toString().isEmpty()) || (name.getText().toString().isEmpty()) || (price.getText().toString().isEmpty()) || (location.getText().toString().isEmpty()) || (rating.getText().toString().isEmpty()))
            Toast.makeText(AddActivity.this, "Missing fields", Toast.LENGTH_SHORT).show();
        else {

            addVenueViewModel.addVenue(new VenueModel(
                    capacity.getText().toString(),
                    name.getText().toString(),
                    price.getText().toString(),
                    location.getText().toString(),
                    rating.getText().toString(),
                    imageString
            ));
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode, requestCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.handlePermission(requestCode, grantResults);
    }

    public void pickImage(View view) {
        imagePicker.choosePicture(true);
    }


    public void Base64() {
    }


}



