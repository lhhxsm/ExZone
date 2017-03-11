package com.exzone.demo.foldable;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.CancellationSignal;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间：2016/10/7.
 */
public class AssetProvider extends ContentProvider {

    @Override
    public AssetFileDescriptor openAssetFile(Uri uri, String mode) throws FileNotFoundException {
        AssetManager am = getContext().getAssets();
        String fileName = "demo-pictures/" + uri.getLastPathSegment();
        AssetFileDescriptor afd = null;
        try {
            afd = am.openFd(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return afd;
    }

    @Override
    public String getType(Uri p1) {
        return null;
    }

    @Override
    public int delete(Uri p1, String p2, String[] p3) {
        return 0;
    }

    @Override
    public Cursor query(Uri p1, String[] p2, String p3, String[] p4, String p5) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder, CancellationSignal cancellationSignal) {
        return super.query(uri, projection, selection, selectionArgs, sortOrder, cancellationSignal);
    }

    @Override
    public Uri insert(Uri p1, ContentValues p2) {
        return null;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public int update(Uri p1, ContentValues p2, String p3, String[] p4) {
        return 0;
    }
}

