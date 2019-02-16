package wdx.musgig.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class VenueModel {

    @PrimaryKey(autoGenerate = true)
    public int id;
    private String capacity;
    private String name;
    private String price;
    private String location;
    private String rating;
    private String photo;

    public VenueModel(String capacity, String name, String price, String location, String rating, String photo) {

        this.capacity = capacity;
        this.name = name;
        this.price = price;
        this.location = location;
        this.rating = rating;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public String getRating() {
        return rating;
    }

    public String getPhoto() {
        return photo;
    }
}
