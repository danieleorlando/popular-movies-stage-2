package me.danieleorlando.popularmovies.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieContentProvider extends ContentProvider {

    private MovieDBHelper dbHelper;
    private static final int MOVIES = 100;
    private static final int MOVIES_BY_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIES, MOVIES);
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/#", MOVIES_BY_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new MovieDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;

        if (sUriMatcher.match(uri)==MOVIES) {
            cursor = dbHelper.getReadableDatabase().query(
                    MovieContract.MovieEntry.TABLE_MOVIE,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
        } else if (sUriMatcher.match(uri)==MOVIES_BY_ID) {
            String select = MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?";
            String[] args = new String[]{uri.getLastPathSegment()};

            cursor = dbHelper.getReadableDatabase().query(
                    MovieContract.MovieEntry.TABLE_MOVIE,
                    projection,
                    select,
                    args,
                    null,
                    null,
                    sortOrder);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri returnUri = null;
        long id;

        if (sUriMatcher.match(uri)==MOVIES) {
            id = dbHelper.getWritableDatabase().insert(
                        MovieContract.MovieEntry.TABLE_MOVIE,
                        null,
                        values);
            if(id > 0){
                returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
            } else {
                throw new android.database.SQLException("Error inserting row at uri: " + uri);
            }
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String id = uri.getLastPathSegment();
        int deleted = dbHelper.getWritableDatabase().delete(
                MovieContract.MovieEntry.TABLE_MOVIE,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{id});

        if (deleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
