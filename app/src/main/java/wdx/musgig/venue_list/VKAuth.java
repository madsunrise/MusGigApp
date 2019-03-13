package wdx.musgig.venue_list;

import android.app.Application;

import com.vk.sdk.VKSdk;

public class VKAuth extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
    }
}



