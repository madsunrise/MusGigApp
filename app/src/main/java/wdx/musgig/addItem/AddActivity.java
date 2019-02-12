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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

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


    private String SavePicture(ImageView iv, String folderToSave) {
        OutputStream fOut = null;


        try {
            File file = new File(folderToSave, "5466.jpg");
            fOut = new FileOutputStream(file);


            //    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // сохранять картинку в jpeg-формате с 85% сжатия.
            fOut.flush();
            fOut.close();

        } catch (Exception e) // здесь необходим блок отслеживания реальных ошибок и исключений, общий Exception приведен в качестве примера
        {
            return e.getMessage();
        }
        return "";
    }

    private void moveFile(File file, File dir)
            throws IOException {
        File newFile = new File(dir, file.getName());
        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            outputChannel = new FileOutputStream(newFile).getChannel();
            inputChannel = new FileInputStream(file).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            file.delete();
        } finally {
            if (inputChannel != null) inputChannel.close();
            if (outputChannel != null) outputChannel.close();
        }
    }

}



