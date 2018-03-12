package me.danieleorlando.popularmovies.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.danieleorlando.popularmovies.R;

public class InfoFragment extends Fragment {

    private static final String MOVIE_INFO = "MOVIE_INFO";
    private String movie_info;

    public InfoFragment() {
        // Required empty public constructor
    }

    public static InfoFragment newInstance(String movie_info) {
        InfoFragment fragment = new InfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MOVIE_INFO, movie_info);
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        movie_info = getArguments().getString(MOVIE_INFO);
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        ((TextView)view.findViewById(R.id.overviewTv)).setText(movie_info);

        return view;
    }

}
