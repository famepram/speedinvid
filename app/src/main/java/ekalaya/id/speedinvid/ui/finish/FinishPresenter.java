package ekalaya.id.speedinvid.ui.finish;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import ekalaya.id.speedinvid.data.models.VideoSource;
import ekalaya.id.speedinvid.ui.base.BasePresenter;
import ekalaya.id.speedinvid.util.Helper;

/**
 * Created by Femmy on 8/27/2017.
 */

public class FinishPresenter extends BasePresenter<FinishContract.View> implements FinishContract.Presenter{

    Context ctx;

    @Inject
    public FinishPresenter(FinishContract.View view, @Named("AppContext") Context ctx) {
        super(view);
        this.ctx = ctx;
    }

    @Override
    public void initResult(String abspath) {
        String dur = "00:00";
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            Uri uri = Uri.parse(abspath);
            retriever.setDataSource(ctx, uri);
            int intdur = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            dur = Helper.formatTime(intdur);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        view.resultInitialized(dur);
    }
}
