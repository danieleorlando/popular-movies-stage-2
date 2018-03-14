package me.danieleorlando.popularmovies.ui.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.danieleorlando.popularmovies.config.Constants;
import me.danieleorlando.popularmovies.R;
import me.danieleorlando.popularmovies.adapter.MovieAdapter;
import me.danieleorlando.popularmovies.api.ApiService;
import me.danieleorlando.popularmovies.database.MovieContract;
import me.danieleorlando.popularmovies.model.Data;
import me.danieleorlando.popularmovies.model.Movie;
import me.danieleorlando.popularmovies.ui.activity.DetailActivity;
import me.danieleorlando.popularmovies.ui.activity.MainActivity;
import me.danieleorlando.popularmovies.utils.HttpUtils;
import me.danieleorlando.popularmovies.utils.ItemOffsetDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment implements View.OnClickListener  {

    public String filter_type;

    private MovieAdapter adapter;
    private RecyclerView recyclerView;
    private TextView messageTv;

    public MoviesFragment() {
        // Required empty public constructor
    }

    public static MoviesFragment newInstance(String filter_type) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.MOVIE_FILTER_TYPE, filter_type);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        filter_type = getArguments().getString(MainActivity.MOVIE_FILTER_TYPE);

        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        messageTv = view.findViewById(R.id.messageTv);
        messageTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMovies();
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.hasFixedSize();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

        adapter = new MovieAdapter(getActivity().getLayoutInflater(), this);
        recyclerView.setAdapter(adapter);

        getMovies();

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(Constants.MOVIE, adapter.getMovie((int)v.getTag()));
        startActivity(intent);
    }

    private void getMovies() {

        switch (filter_type) {
            case MainActivity.MOVIE_FILTER_POPULAR:
                getOnlineMovies(filter_type);
                break;

            case MainActivity.MOVIE_FILTER_TOP_RATED:
                getOnlineMovies(filter_type);
                break;

            case MainActivity.MOVIE_FILTER_FAVORITES:
                getLocalMovies();
                break;

        }
    }

    private void getOnlineMovies(String filter_type) {
        if (HttpUtils.isOnline(getActivity())) {
            messageTv.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiService service = retrofit.create(ApiService.class);

            if (Objects.equals(filter_type, MainActivity.MOVIE_FILTER_POPULAR)) {
                service.getPopularMovies(Constants.API_KEY).enqueue(new Callback<Data>() {
                    @Override
                    public void onResponse(Call<Data> call, Response<Data> response) {
                        adapter.clearMovies();
                        adapter.addMovies(response.body().getMovies());
                    }

                    @Override
                    public void onFailure(Call<Data> call, Throwable t) {
                    }
                });
            } else if (filter_type.equals(MainActivity.MOVIE_FILTER_TOP_RATED)) {
                service.getTopRatedMovies(Constants.API_KEY).enqueue(new Callback<Data>() {
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

    private void getLocalMovies() {
        Cursor cursor = getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                        MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
                        MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
                        MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE,
                        MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
                        MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH},
                null,
                null,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID);

        List<Movie> favoritesMovies = new ArrayList<>();
        if(cursor != null && cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                Movie movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW)));
                movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH)));
                favoritesMovies.add(movie);

                cursor.moveToNext();
            }

            adapter.clearMovies();
            adapter.addMovies(favoritesMovies);

        }
    }
}
