package me.danieleorlando.popularmovies.api;

import me.danieleorlando.popularmovies.model.Data;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("movie/popular")
    Call<Data> getPopularMovies(@Query("api_key") String api_key);

    @GET("movie/top_rated")
    Call<Data> getTopRatedMovies(@Query("api_key") String api_key);

}
