package me.danieleorlando.popularmovies;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import me.danieleorlando.popularmovies.adapter.MovieAdapter;
import me.danieleorlando.popularmovies.api.ApiService;
import me.danieleorlando.popularmovies.model.Data;
import me.danieleorlando.popularmovies.utils.HttpUtils;
import me.danieleorlando.popularmovies.utils.ItemOffsetDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String API_KEY = "";

    private MovieAdapter adapter;
    private RecyclerView recyclerView;
    private TextView messageTv;
    private String currentFilter = Constants.POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.hasFixedSize();
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

        adapter = new MovieAdapter(getLayoutInflater(), this);
        recyclerView.setAdapter(adapter);

        filter(currentFilter);

    }

    private void setupUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ResourcesCompat.getColor(getResources(),R.color.colorAccent,null));
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        messageTv = findViewById(R.id.messageTv);
        messageTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter(currentFilter);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort_popular:
                currentFilter = Constants.POPULAR;
                filter(currentFilter);
                break;
            case R.id.menu_sort_top_rated:
                currentFilter = Constants.TOP_RATED;
                filter(currentFilter);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void filter(String filter) {

        if (HttpUtils.isOnline(MainActivity.this)) {
            messageTv.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiService service = retrofit.create(ApiService.class);

            if (filter.equals(Constants.POPULAR)) {
                service.getPopularMovies(API_KEY).enqueue(new Callback<Data>() {
                    @Override
                    public void onResponse(Call<Data> call, Response<Data> response) {
                        adapter.clearMovies();
                        adapter.addMovies(response.body().getMovies());
                    }

                    @Override
                    public void onFailure(Call<Data> call, Throwable t) {
                    }
                });
            } else if (filter.equals(Constants.TOP_RATED)) {
                service.getTopRatedMovies(API_KEY).enqueue(new Callback<Data>() {
                    @Override
                    public void onResponse(Call<Data> call, Response<Data> response) {
                        adapter.clearMovies();
                        adapter.addMovies(response.body().getMovies());
                    }

                    @Override
                    public void onFailure(Call<Data> call, Throwable t) {
                    }
                });
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            messageTv.setVisibility(View.VISIBLE);
            messageTv.setText(getString(R.string.no_connection));
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(Constants.MOVIE, adapter.getMovie((int)v.getTag()));
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(MainActivity.this,v,
                        getString(R.string.poster_transition));
        startActivity(intent, options.toBundle());
    }
}
