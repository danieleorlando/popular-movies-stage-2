package me.danieleorlando.popularmovies;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class TopRatedFragment extends Fragment implements View.OnClickListener{

    private MovieAdapter adapter;
    private RecyclerView recyclerView;
    private TextView messageTv;

    public TopRatedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_popular, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        messageTv = view.findViewById(R.id.messageTv);
        messageTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        if (HttpUtils.isOnline(getActivity())) {
            messageTv.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiService service = retrofit.create(ApiService.class);

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

        } else {
            recyclerView.setVisibility(View.GONE);
            messageTv.setVisibility(View.VISIBLE);
            messageTv.setText(getString(R.string.no_connection));
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(Constants.MOVIE, adapter.getMovie((int)v.getTag()));
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(),v,
                        getString(R.string.poster_transition));
        startActivity(intent, options.toBundle());
    }
}
