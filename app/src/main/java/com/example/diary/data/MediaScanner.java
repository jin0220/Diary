package com.example.diary.data;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

public class MediaScanner {
    private Context ctxt;
    private String file_Path;
    private MediaScannerConnection mMediaScanner;
    private MediaScannerConnection.MediaScannerConnectionClient mMediaScannerClient;



    public static MediaScanner newInstance(Context context)
    {
        return new MediaScanner (context);
    }

    private MediaScanner (Context context) {

        ctxt = context;

    }

    public void mediaScanning(final String path) {
        if (mMediaScanner == null) {
            mMediaScannerClient = new MediaScannerConnection.MediaScannerConnectionClient() {
                @Override public void onMediaScannerConnected() {
                    mMediaScanner.scanFile(file_Path, null);
                    Log.d("확인4","file_path " + file_Path);
                }

                @Override public void onScanCompleted(String path, Uri uri) {
                    System.out.println("::::MediaScan Success::::");

                    mMediaScanner.disconnect();
                }
            };

            mMediaScanner = new MediaScannerConnection(ctxt, mMediaScannerClient);
            Log.d("확인4","mMediaScannerClient " + file_Path);
        }

        file_Path = path;

        mMediaScanner.connect();
    }
}
