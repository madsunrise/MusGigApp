package wdx.musgig.listItems;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.v7.util.SortedList;

import java.util.List;

import wdx.musgig.db.AppDatabase;
import wdx.musgig.db.VenueModel;


public class VenueListViewModel extends AndroidViewModel {

    private final LiveData<List<VenueModel>> VenuesList;

    private AppDatabase appDatabase;

    public VenueListViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getDatabase(this.getApplication());

        VenuesList = appDatabase.VenuesModel().getAllVenueItems();
    }


    public LiveData<SortedList<VenueModel>> getVenuesList() {
        return VenuesList;
    }

    public void deleteItem(VenueModel VenueModel) {
        new deleteAsyncTask(appDatabase).execute(VenueModel);
    }

    private static class deleteAsyncTask extends AsyncTask<VenueModel, Void, Void> {

        private AppDatabase db;

        deleteAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final VenueModel... params) {
            db.VenuesModel().deleteVenue(params[0]);
            return null;
        }

    }

}
