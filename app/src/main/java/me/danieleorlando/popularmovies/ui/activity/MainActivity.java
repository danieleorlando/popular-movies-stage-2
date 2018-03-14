package me.danieleorlando.popularmovies.ui.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import me.danieleorlando.popularmovies.ui.fragment.MoviesFragment;
import me.danieleorlando.popularmovies.R;

public class MainActivity extends AppCompatActivity {

    public static final String MOVIE_FILTER_TYPE = "MOVIE_FILTER_TYPE";
    public static final String MOVIE_FILTER_POPULAR = "MOVIE_FILTER_POPULAR";
    public static final String MOVIE_FILTER_TOP_RATED = "MOVIE_FILTER_TOP_RATED";
    public static final String MOVIE_FILTER_FAVORITES = "MOVIE_FILTER_FAVORITES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();

        switchFragment(MoviesFragment.newInstance(MOVIE_FILTER_POPULAR));
    }

    private void setupUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.action_popular) {
                    switchFragment(MoviesFragment.newInstance(MOVIE_FILTER_POPULAR));
                } else if (item.getItemId()==R.id.action_top_rated) {
                    switchFragment(MoviesFragment.newInstance(MOVIE_FILTER_TOP_RATED));
                } else if (item.getItemId()==R.id.action_favorites) {
                    switchFragment(MoviesFragment.newInstance(MOVIE_FILTER_FAVORITES));
                }
                return true;
            }
        });
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

}
