package me.danieleorlando.popularmovies.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import me.danieleorlando.popularmovies.Constants;
import me.danieleorlando.popularmovies.R;
import me.danieleorlando.popularmovies.api.ApiService;
import me.danieleorlando.popularmovies.model.TrailerResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TrailersFragment extends Fragment {

    private static final String MOVIE_ID = "MOVIE_ID";
    private int movie_id;

    public TrailersFragment() {
        // Required empty public constructor
    }

    public static TrailersFragment newInstance(int movie_id) {
        TrailersFragment fragment = new TrailersFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MOVIE_ID, movie_id);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        movie_id = getArguments().getInt(MOVIE_ID);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        service.getTrailers(movie_id, Constants.API_KEY).enqueue(new Callback<TrailerResult>() {
            @Override
            public void onResponse(Call<TrailerResult> call, Response<TrailerResult> response) {
                Log.v("dsd","dsd");
            }

            @Override
            public void onFailure(Call<TrailerResult> call, Throwable t) {
                Log.v("dsd","dsd");
            }
        });

        return inflater.inflate(R.layout.fragment_trailers, container, false);
    }

}
