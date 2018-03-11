package me.danieleorlando.popularmovies;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.danieleorlando.popularmovies.model.Movie;

public class DetailActivity extends AppCompatActivity {

    Movie movie;
    ImageView backdropIv;
    ImageView posterIv;
    TextView titleTv;
    TextView dateTv;
    TextView ratingTv;
    TextView overviewTv;
    CollapsingToolbarLayout collapsingToolbar;
    AppBarLayout appBar;
    Toolbar toolbar;

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
        overviewTv = findViewById(R.id.overviewTv);
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
        ratingTv.setText(String.format("%s/10", String.valueOf(movie.getVoteAverage())));
        overviewTv.setText(String.valueOf(movie.getOverview()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            supportFinishAfterTransition();

        return super.onOptionsItemSelected(item);
    }
}
