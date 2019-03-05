package wdx.musgig.venue_full;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jsibbold.zoomage.ZoomageView;

import wdx.musgig.R;


public class FullscreenImage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_image);
        String uri = getIntent().getStringExtra("URI");
        ZoomageView ZoomageView = findViewById(R.id.myZoomageView);
        ZoomageView.setImageURI(Uri.parse(uri));
    }

    public void back(View view) {
        onBackPressed();
    }
}
