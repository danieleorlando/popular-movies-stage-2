package me.danieleorlando.popularmovies.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.danieleorlando.popularmovies.Constants;
import me.danieleorlando.popularmovies.R;
import me.danieleorlando.popularmovies.adapter.TrailerAdapter;
import me.danieleorlando.popularmovies.api.ApiService;
import me.danieleorlando.popularmovies.model.TrailerResult;
import me.danieleorlando.popularmovies.utils.ItemOffsetDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TrailersFragment extends Fragment {

    private static final String MOVIE_ID = "MOVIE_ID";
    private int movie_id;

    private TrailerAdapter adapter;
    private RecyclerView recyclerView;

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
        View view = inflater.inflate(R.layout.fragment_trailers, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewTrailers);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));

        recyclerView.hasFixedSize();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(true);

        adapter = new TrailerAdapter(getActivity().getLayoutInflater());
        recyclerView.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        service.getTrailers(movie_id, Constants.API_KEY).enqueue(new Callback<TrailerResult>() {
            @Override
            public void onResponse(Call<TrailerResult> call, Response<TrailerResult> response) {
                adapter.clearReviews();
                adapter.addTrailer(response.body().getTrailers());
            }

            @Override
            public void onFailure(Call<TrailerResult> call, Throwable t) {
                Log.v("dsd","dsd");
            }
        });
    }

}
