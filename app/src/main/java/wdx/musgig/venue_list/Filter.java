package wdx.musgig.venue_list;

import java.util.Iterator;
import java.util.List;

import wdx.musgig.db.VenueModel;

public class Filter extends MainActivity {

    public List<VenueModel> filter() {


        Iterator<VenueModel> itr = filterMem.iterator();
        while (itr.hasNext()) {
            VenueModel i = itr.next();
            rating = (checkRate.isChecked()) && (i.getRating() < 4);
            alco = (checkAlco.isChecked()) && (i.getPrice() > 5000);
            if (rating || alco)
                itr.remove();
        }
        return filterMem;
    }


}
