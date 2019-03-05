package wdx.musgig.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;


public class AddVenueViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;

    public AddVenueViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getDatabase(this.getApplication());

    }

    public void addVenue(final VenueModel VenueModel) {
        new addAsyncTask(appDatabase).execute(VenueModel);
    }

    private static class addAsyncTask extends AsyncTask<VenueModel, Void, Void> {

        private AppDatabase db;

        addAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final VenueModel... params) {
            db.VenuesModel().addVenue(params[0]);
            return null;
        }

    }
}
