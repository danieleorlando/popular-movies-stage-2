package me.danieleorlando.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.danieleorlando.popularmovies.R;
import me.danieleorlando.popularmovies.model.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.Holder>{

    private final LayoutInflater inflater;
    private List<Review> reviewList;

    public ReviewAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        reviewList = new ArrayList<>();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.item_review, parent, false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        holder.reviewTv.setText(reviewList.get(position).getContent());
        holder.reviewTv.setTag(position);
    }

    public void addReviews(List<Review> reviews) {
        reviewList.clear();
        reviewList.addAll(reviews);
        notifyDataSetChanged();
    }

    public void clearReviews() {
        reviewList.clear();
        notifyDataSetChanged();
    }

    public Review getReview(int position) {
        return reviewList.get(position);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView reviewTv;

        public Holder(View v) {
            super(v);
            reviewTv = v.findViewById(R.id.reviewTv);
        }
    }
}
