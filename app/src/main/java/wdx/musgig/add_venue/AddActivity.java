package wdx.musgig.add_venue;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import wdx.musgig.R;
import wdx.musgig.db.AddVenueViewModel;
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
    private Uri imageUri2;
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
                    Integer.parseInt(capacity.getText().toString()),
                    name.getText().toString(),
                    Integer.parseInt(price.getText().toString()),
                    location.getText().toString(),
                    Float.parseFloat(rating.getText().toString()),
                    imageUri.toString(),
                    imageUri2.toString()
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

    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCropImageActivity(mCropImageUri);

            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        //   File in =new File(imageUri.getPath());
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File out = new File(getCacheDir().getAbsolutePath() + "/" + "not_cropped" + System.currentTimeMillis() + ".jpg");
        imageUri2 = Uri.fromFile(out);
        try (FileOutputStream out1 = new FileOutputStream(out)) {
            Objects.requireNonNull(bitmap).compress(Bitmap.CompressFormat.JPEG, 80, out1);
        } catch (IOException e) {
            e.printStackTrace();
        }


        CropImage.activity(imageUri)
                .setRequestedSize(600, 600)
                .setAspectRatio(1, 1)
                .start(this);
    }


    //  public  void copy(File src, File dst) throws IOException {
    //      try (InputStream in = new FileInputStream(src)) {
    //          try (OutputStream out = new FileOutputStream(dst)) {
    //              // Transfer bytes from in to out
    //              byte[] buf = new byte[1024];
    //              int len;
    //              while ((len = in.read(buf)) > 0) {
    //                  out.write(buf, 0, len);
    //               }
    //           }
    //       }
    //   }

}







