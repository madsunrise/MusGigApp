package wdx.musgig.listItems;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import wdx.musgig.db.AppDatabase;
import wdx.musgig.db.VenueModel;


public class VenueListViewModel extends AndroidViewModel {

    private final LiveData<List<VenueModel>> itemAndPersonList;

    private AppDatabase appDatabase;

    public VenueListViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getDatabase(this.getApplication());

        itemAndPersonList = appDatabase.itemAndPersonModel().getAllVenueItems();
    }


    public LiveData<List<VenueModel>> getItemAndPersonList() {
        return itemAndPersonList;
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
            db.itemAndPersonModel().deleteVenue(params[0]);
            return null;
        }

    }

}
