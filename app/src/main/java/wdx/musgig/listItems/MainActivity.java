package wdx.musgig.listItems;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

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
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<VenueModel>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(recyclerViewAdapter);

        viewModel = ViewModelProviders.of(this).get(VenueListViewModel.class);

        viewModel.getVenuesList().observe(MainActivity.this, new Observer<SortedList<VenueModel>>() {
            @Override
            public void onChanged(@Nullable SortedList<VenueModel> Venues) {
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
}
