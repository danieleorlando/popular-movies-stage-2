package me.danieleorlando.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME ="movie.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_MOVIE + " ( " +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE + " REAL, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " DATETIME " +
                "); ";

        db.execSQL(CREATE_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.delete(MovieContract.MovieEntry.TABLE_MOVIE, null, null);
    }
}
