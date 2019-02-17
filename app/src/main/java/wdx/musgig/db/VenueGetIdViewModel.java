package wdx.musgig.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

public class VenueGetIdViewModel extends AndroidViewModel {
    private AppDatabase appDatabase;


    public VenueGetIdViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getDatabase(this.getApplication());

    }

    public VenueModel getItemById(String itemId) {
        return appDatabase.VenuesModel().getItemById(itemId);
    }


}
