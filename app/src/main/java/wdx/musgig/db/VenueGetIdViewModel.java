package wdx.musgig.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

public class VenueGetIdViewModel extends AndroidViewModel {
    private AppDatabase appDatabase;


    public VenueGetIdViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getDatabase(this.getApplication());

    }

    public LiveData<VenueModel> getItemById(String itemId) {
        return appDatabase.VenuesModel().getItemById(itemId);
    }


}
