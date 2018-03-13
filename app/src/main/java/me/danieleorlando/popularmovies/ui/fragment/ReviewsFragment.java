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
import me.danieleorlando.popularmovies.adapter.ReviewAdapter;
import me.danieleorlando.popularmovies.api.ApiService;
import me.danieleorlando.popularmovies.model.ReviewResult;
import me.danieleorlando.popularmovies.utils.ItemOffsetDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReviewsFragment extends Fragment {

    private static final String MOVIE_ID = "MOVIE_ID";
    private int movie_id;

    private ReviewAdapter adapter;
    private RecyclerView recyclerView;


    public ReviewsFragment() {
        // Required empty public constructor
    }

    public static ReviewsFragment newInstance(int movie_id) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MOVIE_ID, movie_id);
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        movie_id = getArguments().getInt(MOVIE_ID);
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewReviews);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));

        recyclerView.hasFixedSize();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(true);

        //ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        //recyclerView.addItemDecoration(itemDecoration);

        adapter = new ReviewAdapter(getActivity().getLayoutInflater());
        recyclerView.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        service.getReviews(movie_id, Constants.API_KEY).enqueue(new Callback<ReviewResult>() {
            @Override
            public void onResponse(Call<ReviewResult> call, Response<ReviewResult> response) {
                adapter.clearReviews();
                adapter.addReviews(response.body().getReviews());
            }

            @Override
            public void onFailure(Call<ReviewResult> call, Throwable t) {
                Log.v("dsd","dsd");
            }
        });
    }
}
