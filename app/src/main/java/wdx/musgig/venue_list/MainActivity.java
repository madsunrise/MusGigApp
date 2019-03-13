package wdx.musgig.venue_list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import wdx.musgig.R;
import wdx.musgig.add_venue.AddActivity;
import wdx.musgig.db.VenueListViewModel;
import wdx.musgig.db.VenueModel;


public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {

    private VenueListViewModel viewModel;
    private RecyclerViewAdapter recyclerViewAdapter;
    FrameLayout bar;
    List<VenueModel> filterMem;
    RecyclerView recyclerView;
    DrawerLayout drawer;
    private long mBackPressed;
    CheckBox checkRate, checkAlco, checkWater, checkParking, checkDressing, checkFast, checkWeekdays, checkWifi, checkRoof, checkSmoke;
    boolean rating, alco, water, parking, dressing, fast, weekdays, wifi, roof, smoke;
    Button clearButton;
    ImageView people_icon, price_icon;
    TextView acc_name, acc_position;
    RoundedImageView acc_photo;

    ExpandableLayout expand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewsById();
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
        viewModel = ViewModelProviders.of(this).get(VenueListViewModel.class);
        recyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<VenueModel>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                bar.animate().translationY(-bar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
            }
            @Override
            public void onShow() {
                bar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItem = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findFirstVisibleItemPosition();
                //show views if first item is first visible position and views are hidden
                if (firstVisibleItem == 0) {
                    if (!controlsVisible) {
                        onShow();
                        controlsVisible = true;
                    }
                } else {
                    if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                        onHide();
                        controlsVisible = false;
                        scrolledDistance = 0;
                    } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                        onShow();
                        controlsVisible = true;
                        scrolledDistance = 0;
                    }
                }
                if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                    scrolledDistance += dy;
                }
            }

        });
        initRecycler();
    }


    public void addVenue(View view) {
        startActivity(new Intent(MainActivity.this, AddActivity.class));
    }

    public void sortBy(String param, boolean increase) {

        Collections.sort(Objects.requireNonNull(filterMem), new Comparator<VenueModel>() {
            @Override
            public int compare(VenueModel first, VenueModel second) {
                switch (param) {
                    case "people":
                        if (increase)
                            return Integer.compare(first.getCapacity(), second.getCapacity());
                        return Integer.compare(second.getCapacity(), first.getCapacity());
                    case "price":
                        if (increase)
                            return Integer.compare(first.getPrice(), second.getPrice());
                        return Integer.compare(second.getPrice(), first.getPrice());
                }
                return 0;
            }
        });
        recyclerViewAdapter.addItems(filterMem);
    }

    public void expand_filter(View view) {
        expand.toggle();
    }

    public void filter(View view) {
        Iterator<VenueModel> itr = filterMem.iterator();
        while (itr.hasNext()) {
            VenueModel i = itr.next();
            rating = (checkRate.isChecked()) && (i.getRating() < 4);
            alco = (checkAlco.isChecked()) && (i.getPrice() > 5000);
            if (rating || alco)
                itr.remove();
        }
        recyclerViewAdapter.addItems(filterMem);
    }

    public void clearFilter(View view) {
        initRecycler();
    }

    public void initRecycler() {
        viewModel.getVenuesList().observe(MainActivity.this, new Observer<List<VenueModel>>() {
            @Override
            public void onChanged(@Nullable List<VenueModel> Venues) {
                filterMem = new ArrayList<>(Objects.requireNonNull(Venues));
                recyclerViewAdapter.addItems(Venues);
            }
        });
    }

    public void setAccountData(String name, int id, String photo_200_orig) {
        if ((name != null) && (id != 0) && (photo_200_orig != null)) {

            acc_name.setText(name);
            acc_position.setText(String.valueOf(id));
            Glide.with(this).load(photo_200_orig).into(acc_photo);
        }
    }


    @Override
    public boolean onLongClick(View view) {
        VenueModel VenueModel = (VenueModel) view.getTag();
        viewModel.deleteItem(VenueModel);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else if (mBackPressed + 5000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Еще раз \"назад\" чтобы выйти", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

    public void findViewsById() {
        recyclerView = findViewById(R.id.recyclerView);
        bar = findViewById(R.id.appbar);
        drawer = findViewById(R.id.drawer_layout);
        people_icon = findViewById(R.id.people_icon);
        price_icon = findViewById(R.id.price_icon);
        checkRate = findViewById(R.id.checkRate);
        checkAlco = findViewById(R.id.checkAlco);
        checkWater = findViewById(R.id.checkWater);
        checkParking = findViewById(R.id.checkParking);
        checkDressing = findViewById(R.id.checkDressing);
        checkFast = findViewById(R.id.checkFast);
        checkWeekdays = findViewById(R.id.checkWeekdays);
        checkWifi = findViewById(R.id.checkWifi);
        checkRoof = findViewById(R.id.checkRoof);
        checkSmoke = findViewById(R.id.checkSmoke);
        clearButton = findViewById(R.id.button2);
        expand = findViewById(R.id.expandable_layout);
        acc_name = findViewById(R.id.acc_name);
        acc_position = findViewById(R.id.acc_position);
        acc_photo = findViewById(R.id.acc_photo);

    }

    public void open_drawer(View view) {
        drawer.openDrawer(GravityCompat.START);
    }

    public void start_login_activity(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }
}