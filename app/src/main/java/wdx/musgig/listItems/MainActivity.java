package wdx.musgig.listItems;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import wdx.musgig.R;
import wdx.musgig.addItem.AddActivity;
import wdx.musgig.db.VenueModel;


public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {

    private VenueListViewModel viewModel;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView people_icon = (ImageView) findViewById(R.id.people_icon);
        ImageView price_icon = (ImageView) findViewById(R.id.price_icon);
        View.OnClickListener sortBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.people_icon:
                        price_icon.setImageResource(R.drawable.price_icon); // убираем стрелочки со всего остального
                        if (people_icon.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.people_icon_down).getConstantState()) { // Если уже сортировали по возрастанию
                            sortBy("people", false);
                            people_icon.setImageResource(R.drawable.people_icon_up);
                        } else {                                                               // Если нет то начинаем с сортировки по возрастанию
                            people_icon.setImageResource(R.drawable.people_icon_down);
                            sortBy("people", true);
                        }
                        break;
                    case R.id.price_icon:
                        people_icon.setImageResource(R.drawable.people_icon);
                        if (price_icon.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.price_icon_down).getConstantState()) { // Если уже сортировали по возрастанию
                            sortBy("price", false);
                            price_icon.setImageResource(R.drawable.price_icon_up);
                        } else {                                                               // Если нет то начинаем с сортировки по возрастанию
                            price_icon.setImageResource(R.drawable.price_icon_down);
                            sortBy("price", true);
                        }
                        break;
                }
            }
        };
        people_icon.setOnClickListener(sortBtn);
        price_icon.setOnClickListener(sortBtn);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<VenueModel>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);
        viewModel = ViewModelProviders.of(this).get(VenueListViewModel.class);
        viewModel.getVenuesList().observe(MainActivity.this, new Observer<List<VenueModel>>() {
            @Override
            public void onChanged(@Nullable List<VenueModel> Venues) {
                Collections.sort(Venues, new Comparator<VenueModel>() {
                    @Override
                    public int compare(VenueModel first, VenueModel second) {
                        return Integer.valueOf(first.getPrice()) > Integer.valueOf(second.getPrice()) ? -1 :
                                Integer.valueOf(first.getPrice()) < Integer.valueOf(second.getPrice()) ? 1 : 0;
                    }
                });
                recyclerViewAdapter.addItems(Venues);
            }


        });

    }

    @Override
    public boolean onLongClick(View v) {
        VenueModel VenueModel = (VenueModel) v.getTag();
        viewModel.deleteItem(VenueModel);
        return true;
    }

    public void addVenue(View view) {
        startActivity(new Intent(MainActivity.this, AddActivity.class));
    }

    public void sortBy(String param, boolean increase) {
        viewModel.getVenuesList().observe(MainActivity.this, new Observer<List<VenueModel>>() {
            @Override
            public void onChanged(@Nullable List<VenueModel> Venues) {
                Collections.sort(Venues, new Comparator<VenueModel>() {
                    @Override
                    public int compare(VenueModel first, VenueModel second) {
                        switch (param) {
                            case "people":
                                if (increase)
                                    return Integer.valueOf(first.getCapacity()) < Integer.valueOf(second.getCapacity()) ? -1 :
                                            Integer.valueOf(first.getCapacity()) > Integer.valueOf(second.getCapacity()) ? 1 : 0;
                                return Integer.valueOf(first.getCapacity()) > Integer.valueOf(second.getCapacity()) ? -1 :
                                        Integer.valueOf(first.getCapacity()) < Integer.valueOf(second.getCapacity()) ? 1 : 0;

                            case "price":
                                if (increase)
                                    return Integer.valueOf(first.getPrice()) < Integer.valueOf(second.getPrice()) ? -1 :
                                            Integer.valueOf(first.getPrice()) > Integer.valueOf(second.getPrice()) ? 1 : 0;
                                return Integer.valueOf(first.getPrice()) > Integer.valueOf(second.getPrice()) ? -1 :
                                        Integer.valueOf(first.getPrice()) < Integer.valueOf(second.getPrice()) ? 1 : 0;

                        }
                        return 0;
                    }
                });
                recyclerViewAdapter.addItems(Venues);
            }
        });
    }


}
