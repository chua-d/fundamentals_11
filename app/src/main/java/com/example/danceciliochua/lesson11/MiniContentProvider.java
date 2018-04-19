package com.example.danceciliochua.lesson11;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static java.lang.Integer.parseInt;

public class MiniContentProvider extends ContentProvider {

    public static final String TAG = MiniContentProvider.class.getSimpleName();
    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public String[] mData;

    private void initializeUriMatching() {
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.CONTENT_PATH + "/#", 1);
        sUriMatcher.addURI(Contract.AUTHORITY, Contract.CONTENT_PATH, 0);
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mData = context.getResources().getStringArray(R.array.words);
        initializeUriMatching();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int id = -1;

        switch (sUriMatcher.match(uri)) {
            case 0:
                id = Contract.ALL_ITEMS;
                if(selection!=null) {
                    id = parseInt(selectionArgs[0]);
                }
                break;

            case 1:
                id = parseInt(uri.getLastPathSegment());
                break;

            case UriMatcher.NO_MATCH:
                Log.d(TAG, "NO MATCH FOR THIS URI IN SCHEME.");
                break;

            default:
                Log.d(TAG, "INVALID URI - URI NOT RECOGNIZED.");
                id = -1;
        }
        Log.d(TAG, "query: " + id);
        return populateCursor(id);
    }

    private Cursor populateCursor(int id) {
        MatrixCursor cursor = new MatrixCursor(new String[] { Contract.CONTENT_PATH });

        if(id == Contract.ALL_ITEMS) {
            for (int i = 0; i < mData.length; i++) {
                String word = mData[i];
                cursor.addRow(new Object[]{word});
            }
        } else if(id >=0) {
            String word = mData[id];
            cursor.addRow(new Object[]{word});
        }
        return cursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch(sUriMatcher.match(uri)) {
            case 0:
                return Contract.MULTIPLE_RECORD_MIME_TYPE;
            case 1:
                return Contract.SINGLE_RECORD_MIME_TYPE;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.e(TAG, getContext().getResources().getString(R.string.not_implemented)
                + "insert uri: " + uri.toString());
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.e(TAG, getContext().getResources().getString(R.string.not_implemented)
                + "delete uri: " + uri.toString());
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.e(TAG, getContext().getResources().getString(R.string.not_implemented)
                + "update uri: " + uri.toString());
        return 0;
    }
}
