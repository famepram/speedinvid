package ekalaya.id.speedinvid.ui.main;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Named;

import ekalaya.id.speedinvid.ui.base.BasePresenter;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Femmy on 8/26/2017.
 */

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter{

    @Named("AppContext")
    @Inject
    Context context;

    @Inject
    public MainPresenter(MainContract.View view) {
        super(view);

    }

    @Override
    public void openFileBrowser() {

    }

    @Override
    public void receiveVideoFromIntent(Intent data) {
            Log.d("SPEEDINVI", "sdsa----------------------dsadsad");
            Uri selectedVideo = data.getData();
            String path = getPath(selectedVideo);
            view.videoValid(path);

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getPathResult(Uri uri){
        String path = "";
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String selection = "_id=?";
        final String[] selectionArgs = new String[]{
                split[1]
        };

        try {
            Uri mUuri   = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            cursor      = context.getContentResolver().query(mUuri, projection, selection, selectionArgs,null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                path = cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return path;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getPath( final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
}
