package wdx.musgig.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

public class VenueGetIdViewModel extends AndroidViewModel {
    // private final VenueModel VenuesList;

    private static AppDatabase appDatabase;

    public VenueGetIdViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getDatabase(this.getApplication());

        // VenuesList = appDatabase.VenuesModel().getAllVenueItems();
    }

    public static VenueModel getItemById(String itemId) {
        return appDatabase.VenuesModel().getItemById(itemId);
    }


}
