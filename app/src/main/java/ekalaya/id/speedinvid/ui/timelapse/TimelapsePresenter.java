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
    public void generateFrames(String abspath) {
        Bitmap bmp = null;
        MediaMetadataRetriever retriever = new  MediaMetadataRetriever();
        try {
            retriever.setDataSource(abspath);
            bmp = retriever.getFrameAtTime();
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            Long dur    = Long.parseLong(time );
            List<Bitmap> bmps = new ArrayList<Bitmap>();
            if(dur > 0){
                int frametime;
                for(int i=0; i < 8; i++){
                    frametime = i > 0 ? (int) Math.ceil(dur / 8) * i + 1:1;
                    frametime = frametime * 1000;
                    Bitmap bmFrame = retriever.getFrameAtTime(frametime, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                    bmps.add(bmFrame);
                }
            }
            retriever.release();
            view.frameGenerated(bmps);
        } catch (Exception e){
            Log.e(Const.APP_TAG, "Exception : " + e.getMessage());
        }
    }

    @Override
    public void adjustToSeekbar(Number min, Number max, VideoSource vs) {
        int start = Integer.parseInt(String.valueOf(min));
        int end   = Integer.parseInt(String.valueOf(max));
        vs.setStart(start);
        vs.setFinish(end);
        view.videoModified(vs);
    }

    @Override
    public void initializinVidSrc(String abspath) {
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
