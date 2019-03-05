package wdx.musgig.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class VenueModel {

    @PrimaryKey(autoGenerate = true)
    public int id;
    private int capacity;
    private String name;
    private int price;
    private String location;
    private float rating;
    private String photo;
    private String photo2;

    public VenueModel(int capacity, String name, int price, String location, float rating, String photo, String photo2) {

        this.capacity = capacity;
        this.name = name;
        this.price = price;
        this.location = location;
        this.rating = rating;
        this.photo = photo;
        this.photo2 = photo2;
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public float getRating() {
        return rating;
    }

    public String getPhoto() {
        return photo;
    }

    public String getPhoto2() {
        return photo2;
    }
}

