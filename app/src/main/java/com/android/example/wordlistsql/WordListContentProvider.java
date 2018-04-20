package com.android.example.wordlistsql;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.android.example.wordlistsql.Contract.CONTENT_URI;
import static java.lang.Integer.parseInt;

public class WordListContentProvider extends ContentProvider {

    private static final String TAG = WordListContentProvider.class.getSimpleName();

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private WordListOpenHelper mDB;

    private static final int URI_ALL_ITEMS_CODE = 10;
    private static final int URI_ONE_ITEM_CODE = 20;
    private static final int URI_COUNT_CODE = 30;

    private static final int ALL_ITEMS = Contract.ALL_ITEMS;

    @Override
    public boolean onCreate() {
        mDB = new WordListOpenHelper(getContext());
        initializeUriMatching();
        return true;
    }

    private void initializeUriMatching() {
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.CONTENT_PATH, URI_ALL_ITEMS_CODE);
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.CONTENT_PATH + "/#", URI_ONE_ITEM_CODE);
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.CONTENT_PATH + "/" + Contract.COUNT, URI_COUNT_CODE);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor = null;

        switch (sUriMatcher.match(uri)) {
            case URI_ALL_ITEMS_CODE:
                cursor = mDB.query(ALL_ITEMS);
                break;

            case URI_ONE_ITEM_CODE:
                cursor = mDB.query(parseInt(uri.getLastPathSegment()));
                break;

            case URI_COUNT_CODE:
                cursor = mDB.count();
                break;

            case UriMatcher.NO_MATCH:
                Log.d(TAG, "NO MATCHER FOR THIS URI IN SCHEME: " + uri);
                break;

            default:
                Log.d(TAG, "INVALID URI - URI NOT RECOGNIZED: " + uri);
        }
        return cursor;
    }



    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long id = mDB.insert(contentValues);
        return Uri.parse(CONTENT_URI + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return mDB.delete(parseInt(strings[0]));
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return mDB.update(parseInt(strings[0]),contentValues.getAsString(Contract.WordList.KEY_WORD));
    }
}
