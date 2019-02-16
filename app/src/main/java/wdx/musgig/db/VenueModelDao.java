package wdx.musgig.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface VenueModelDao {

    @Query("select * from VenueModel")
    LiveData<List<VenueModel>> getAllVenueItems();

    @Query("select * from VenueModel where id = :id")
    VenueModel getItembyId(String id);


    @Insert(onConflict = REPLACE)
    void addVenue(VenueModel VenueModel);

    @Delete
    void deleteVenue(VenueModel VenueModel);

}
