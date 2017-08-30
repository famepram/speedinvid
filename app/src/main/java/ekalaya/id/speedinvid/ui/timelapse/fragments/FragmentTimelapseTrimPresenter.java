package ekalaya.id.speedinvid.ui.timelapse.fragments;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ekalaya.id.speedinvid.ui.base.BasePresenter;
import ekalaya.id.speedinvid.util.Const;

/**
 * Created by WebDev on 30/08/2017.
 */

public class FragmentTimelapseTrimPresenter extends BasePresenter<FragmentTimelapseTrimContract.View>
        implements FragmentTimelapseTrimContract.Presenter {

    FragmentTimelapseTrimContract.View view;

    public FragmentTimelapseTrimPresenter(FragmentTimelapseTrimContract.View view) {
        super(view);
        this.view = view;
    }

    @Override
    public void drawTimeline(String abspath) {
        Bitmap bmp = null;
        Log.d(Const.APP_TAG, "abspath : " + abspath);
        MediaMetadataRetriever retriever = new  MediaMetadataRetriever();
        try {
            retriever.setDataSource(abspath);
            bmp = retriever.getFrameAtTime();
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            Long dur    = Long.parseLong(time );
            Log.d("SPEEDINVID", "Dur : "+dur);
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
            view.timelineDrawn(bmps);
        } catch (Exception e){
            e.printStackTrace();
            //og.e(Const.APP_TAG, "Exception : " + e.getMessage());
        }
    }
}