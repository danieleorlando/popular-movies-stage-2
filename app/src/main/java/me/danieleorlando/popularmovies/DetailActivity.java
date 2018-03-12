package me.danieleorlando.popularmovies;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.danieleorlando.popularmovies.model.Movie;
import me.danieleorlando.popularmovies.ui.fragment.InfoFragment;
import me.danieleorlando.popularmovies.ui.fragment.ReviewsFragment;
import me.danieleorlando.popularmovies.ui.fragment.TrailersFragment;

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
    ViewPager viewPager;

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
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void fillUI() {
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
        if (id == android.R.id.home)
            supportFinishAfterTransition();

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
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
