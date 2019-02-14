package wdx.musgig.addItem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import wdx.musgig.R;
import wdx.musgig.db.VenueModel;

public class AddActivity extends AppCompatActivity {
    private EditText capacity;
    private EditText name;
    private EditText price;
    private EditText location;
    private EditText rating;
    public static final int RC_CAMERA = 5;
    private AddVenueViewModel addVenueViewModel;
    ImageView pickImage;
    private Uri imageUri;
    private Activity activity;
    Uri mCropImageUri;

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
                    imageUri.toString()
            ));
            finish();
        }
    }

    public void openCamera(View view) {

        CropImage.startPickImageActivity(this);
    }



    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                pickImage.setImageURI(imageUri);
            }
        }
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imageUri = CropImage.getPickImageResultUri(this, data);

            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                startCropImageActivity(imageUri);

            }
        }

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCropImageActivity(mCropImageUri);

            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setAspectRatio(1, 1)
                .start(this);
    }



        }







