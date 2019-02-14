package wdx.musgig.addItem;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;

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
        choosePhoto();


    }

    public void choosePhoto() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        File file = new File(getCacheDir().getAbsolutePath() + "/" + "photo_" + System.currentTimeMillis() + ".jpg");
        imageUri = Uri.fromFile(file);
        Intent chooser = Intent.createChooser(galleryIntent, "Выберите откуда");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});
        chooser.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(chooser, 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        boolean isCamera = false;
        if (data.getAction() != null) isCamera = true;
        if (!isCamera) {
            Uri contentURI = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                //        String path = saveImage(bitmap);
                Toast.makeText(AddActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                //         imageview.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(AddActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        } else if (isCamera) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            pickImage.setImageBitmap(thumbnail);
            //    saveImage(thumbnail);

            //   Object obj = data.getExtras().get("data");
            //   if (obj instanceof Bitmap) {
            //   Bitmap bitmap = (Bitmap) obj;
            //    pickImage.setImageURI(imageUri);


            Toast.makeText(AddActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
        }







