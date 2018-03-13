package me.danieleorlando.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.danieleorlando.popularmovies.Constants;
import me.danieleorlando.popularmovies.R;
import me.danieleorlando.popularmovies.model.Trailer;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.Holder>{

    private final LayoutInflater inflater;
    private List<Trailer> trailerList;

    public TrailerAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        trailerList = new ArrayList<>();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.item_trailer, parent, false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        holder.trailerTv.setText(trailerList.get(position).getName());
        holder.trailerTv.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo(position,holder.itemView.getContext());
            }
        });
    }

    public void addTrailer(List<Trailer> trailers) {
        trailerList.clear();
        trailerList.addAll(trailers);
        notifyDataSetChanged();
    }

    public void clearReviews() {
        trailerList.clear();
        notifyDataSetChanged();
    }

    public Trailer getTrailer(int position) {
        return trailerList.get(position);
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView trailerTv;

        public Holder(View v) {
            super(v);
            trailerTv = v.findViewById(R.id.trailerTv);
        }
    }

    private void playVideo(int position, Context context){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Constants.YOUTUBE_BASE_URL + trailerList.get(position).getKey()));
        context.startActivity(intent);

    }
}
