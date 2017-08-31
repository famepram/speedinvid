package ekalaya.id.speedinvid.ui.timelapse;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import ekalaya.id.speedinvid.data.models.VideoSource;
import ekalaya.id.speedinvid.ui.base.BasePresenter;
import ekalaya.id.speedinvid.util.Const;
import ekalaya.id.speedinvid.util.Helper;

/**
 * Created by Femmy on 8/26/2017.
 */

public class TimelapsePresenter extends BasePresenter<TimelapseContract.View> implements TimelapseContract.Presenter{

    Context ctx;

    @Inject
    public TimelapsePresenter(TimelapseContract.View view, @Named("AppContext") Context ctx) {
        super(view);
        this.ctx = ctx;
    }


    @Override
    public void initializinVidSrc(String abspath) {
        //Log.d(Const.APP_TAG,"abspath-------------------"+abspath);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            Uri uri = Uri.parse(abspath);
            retriever.setDataSource(ctx, uri);
            int dur = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            int height = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            int width = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));

            VideoSource vs = new VideoSource(abspath);
            vs.setDuration(dur);
            vs.setHeight(height);
            vs.setWidth(width);
            vs.setStart(0);
            vs.setFinish(dur);
            view.videoInitialized(vs);
            view.videoModified(vs);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
    }
}
