package me.danieleorlando.popularmovies.adapter;

import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.danieleorlando.popularmovies.R;
import me.danieleorlando.popularmovies.Constants;
import me.danieleorlando.popularmovies.model.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.Holder> {

    private View.OnClickListener onClickListener;
    private final LayoutInflater inflater;
    private List<Movie> movieList;

    public MovieAdapter(LayoutInflater inflater, View.OnClickListener onClickListener) {
        this.inflater = inflater;
        this.onClickListener = onClickListener;
        movieList = new ArrayList<>();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.item_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        Picasso.with(holder.movieIv.getContext())
                .load(Constants.BASE_IMAGE_URL+Constants.IMAGE_W185+movieList.get(position).getPosterPath())
                .into(holder.movieIv, new Callback() {
                    @Override
                    public void onSuccess() {
                        Palette.from(((BitmapDrawable)holder.movieIv.getDrawable()).getBitmap())
                                .generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        holder.backgroundTv.setBackgroundColor(palette.getDarkVibrantColor(ResourcesCompat.getColor(
                                                holder.backgroundTv.getContext().getResources(),
                                                R.color.grey_800,
                                                null)));
                                    }
                                });
                    }

                    @Override
                    public void onError() {

                    }
                });
        holder.titleTv.setText(movieList.get(position).getTitle());
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(onClickListener);
    }

    public void addMovies(List<Movie> movies) {
        movieList.clear();
        movieList.addAll(movies);
        notifyDataSetChanged();
    }

    public void clearMovies() {
        movieList.clear();
        notifyDataSetChanged();
    }

    public Movie getMovie(int position) {
        return movieList.get(position);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView movieIv;
        TextView titleTv;
        TextView backgroundTv;

        public Holder(View v) {
            super(v);
            movieIv = v.findViewById(R.id.movieIv);
            backgroundTv = v.findViewById(R.id.backgroundTv);
            titleTv = v.findViewById(R.id.titleTv);
        }
    }
}
