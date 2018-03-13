package me.danieleorlando.popularmovies.ui.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.danieleorlando.popularmovies.Constants;
import me.danieleorlando.popularmovies.R;
import me.danieleorlando.popularmovies.database.MovieContract;
import me.danieleorlando.popularmovies.model.Movie;
import me.danieleorlando.popularmovies.ui.fragment.InfoFragment;
import me.danieleorlando.popularmovies.ui.fragment.ReviewsFragment;
import me.danieleorlando.popularmovies.ui.fragment.TrailersFragment;
import me.danieleorlando.popularmovies.view.CustomViewPager;

public class DetailActivity extends AppCompatActivity {

    Movie movie;
    ImageView backdropIv;
    ImageView posterIv;
    TextView titleTv;
    TextView dateTv;
    TextView ratingTv;
    CollapsingToolbarLayout collapsingToolbar;
    AppBarLayout appBar;
    Toolbar toolbar;
    TabLayout tabLayout;
    FloatingActionButton fabFavorite;
    CustomViewPager viewPager;

    boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movie = (Movie)getIntent().getExtras().get(Constants.MOVIE);

        setupUI();
        fillUI();
    }

    private void setupUI() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        backdropIv = findViewById(R.id.backdropIv);
        appBar = findViewById(R.id.appBar);
        posterIv = findViewById(R.id.posterIv);
        titleTv = findViewById(R.id.titleTv);
        dateTv = findViewById(R.id.dateTv);
        ratingTv = findViewById(R.id.ratingTv);
        viewPager = findViewById(R.id.viewpager);
        fabFavorite = findViewById(R.id.fabFavorite);
        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFavorite();
            }
        });
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void updateFavorite() {
        if (isFavorite) {
            removeMovieFromFavorite();
        } else {
            addMovieToFavorite();
        }
        isFavorite=!isFavorite;
    }

    private void addMovieToFavorite() {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getOriginalTitle());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());

        getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);

        fabFavorite.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_24dp,null));
        Snackbar.make(findViewById(R.id.coordinatorLayout),
                R.string.added_to_favorite, Snackbar.LENGTH_SHORT).show();
    }

    private void removeMovieFromFavorite() {
        Uri MovieUri = MovieContract.MovieEntry.CONTENT_URI;
        MovieUri = MovieUri.buildUpon().appendPath(Integer.toString(movie.getId())).build();
        getContentResolver().delete(MovieUri, null, null);
        fabFavorite.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_border_24dp,null));
        Snackbar.make(findViewById(R.id.coordinatorLayout),
                R.string.removed_from_favorite, Snackbar.LENGTH_SHORT).show();
    }

    private boolean findMovie() {
        Cursor cursor;
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(Integer.toString(movie.getId())).build();
        String[] projection = new String[]{MovieContract.MovieEntry._ID};

        cursor = getContentResolver().query(uri, projection,null, null, null);

        return (cursor.getCount()>0);
    }

    private void fillUI() {

        isFavorite = findMovie();
        if (isFavorite) {
            fabFavorite.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_24dp,null));
        } else {
            fabFavorite.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_border_24dp,null));
        }

        collapsingToolbar.setTitle(movie.getTitle());
        Picasso.with(this)
                .load(Constants.BASE_IMAGE_URL+Constants.IMAGE_W500+movie.getBackdropPath())
                .into(backdropIv);

        Picasso.with(this)
                .load(Constants.BASE_IMAGE_URL+Constants.IMAGE_W185+movie.getPosterPath())
                .into(posterIv);

        titleTv.setText(movie.getTitle());
        dateTv.setText(String.valueOf(movie.getReleaseDate()));
        ratingTv.setText(String.format("%s / 10", String.valueOf(movie.getVoteAverage())));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home);
            supportFinishAfterTransition();

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(CustomViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(InfoFragment.newInstance(movie.getOverview()), getString(R.string.info));
        adapter.addFragment(TrailersFragment.newInstance(movie.getId()), getString(R.string.trailers));
        adapter.addFragment(ReviewsFragment.newInstance(movie.getId()), getString(R.string.reviews));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
