package ekalaya.id.speedinvid.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import ekalaya.id.speedinvid.data.models.VideoSource;

/**
 * Created by Femmy on 8/27/2017.
 */

public class VideoProcessor {

    private VideoSource vidsource;

    VPCallback mVPCallback;

    private FFmpeg ffmpeg;

    File destDir  = Environment.getExternalStoragePublicDirectory(Const.APP_RESULT_DIR_NAME);

    File tempDir  = Environment.getExternalStoragePublicDirectory(Const.APP_RESULT_DIR_NAME+"/temp");

    String videoFileResultPath;

    String tempResultPath;

    public interface VPCallback {
        void unknownError(String e);

        void ffmpegNotSupported();

        void ffmpegSuccessLoaded();

        void ffmpegAlreadyRunning();

        void onExecfailure(String s);

        void onExecSuccess(String s);

        void onExecRunning(int percent, String msg);

        void onExecFinished();

        void onExecStart();
    }

    public VideoProcessor(){}

    public VideoProcessor(VideoSource vs){
        vidsource = vs;
        prepareResult();
    }

    public void setVidsource(VideoSource vs){
        vidsource = vs;
        prepareResult();
    }

    private void prepareResult(){
        if(!destDir.isDirectory()){
            destDir.mkdir();
        }

        if(!tempDir.isDirectory()){
            tempDir.mkdir();
        }

        String timestamp = String.valueOf( (System.currentTimeMillis() / 1000L));
        File videoFileResult = new File(destDir, vidsource.getFileName() +"_"+timestamp+"."+vidsource.getFileExt());
        videoFileResultPath = videoFileResult.getAbsolutePath();

    }

    public void setVideoProcessorCallback(VPCallback mVPCallback){
        this.mVPCallback = mVPCallback;
    }

    public void loadFFMEPEG(Context ctx){
        try {
            if (ffmpeg == null) {
                //Log.d(TAG, "ffmpeg : era nulo");
                ffmpeg = FFmpeg.getInstance(ctx);
            }
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    mVPCallback.ffmpegNotSupported();
                }

                @Override
                public void onSuccess() {
                    mVPCallback.ffmpegSuccessLoaded();
                }
            });
        } catch (FFmpegNotSupportedException e) {
            mVPCallback.ffmpegNotSupported();
        } catch (Exception e) {
            mVPCallback.unknownError(e.getMessage());
        }
    }

    public boolean cutFirst(){
        if(vidsource.getStart() > 0){
            return true;
        }
        if(vidsource.getFinish() < vidsource.getDuration()){
            return true;
        }
        return false;
    }

    private void cutVidThenExec(){
        File videoFileTempResult = new File(tempDir, "temp."+vidsource.getFileExt());
        tempResultPath = videoFileTempResult.getAbsolutePath();
        int ps = (int) Math.ceil((double) vidsource.getStart() / (double) 1000);
        int pf = (int) Math.ceil((double) vidsource.getFinish() / (double) 1000);

        String ss = toTime(ps)+".000";
        String to = toTime(pf)+".000";
        String[] complexCommand = {
                "-i", vidsource.getPathsrc(),
                "-vcodec","copy",
                "-an",
                "-ss", "" + ss,
                "-to", "" + to,
                tempResultPath};

        try {
            ffmpeg.execute(complexCommand, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFinish() {
                    String[] complexCommand = buildArguments();
                    execFFmpegBinary(complexCommand);
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }

    private String[] buildArguments(){
        String vidsrc = cutFirst() ? tempResultPath : vidsource.getPathsrc();
        String[] complexCommand = {
                "-i", vidsrc,
                "-filter:v","setpts=0.2*PTS",
                "-vcodec","libx264",
                "-preset","ultrafast",
                "-an",
                videoFileResultPath};
        return complexCommand;
    }

    public void execute(){
        if(cutFirst()){
            cutVidThenExec();
        } else {
            String[] complexCommand = buildArguments();
            execFFmpegBinary(complexCommand);
        }

    }

    public String getVideoFileResultPath(){
        return videoFileResultPath;
    }


    private void execFFmpegBinary(final String[] command) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    mVPCallback.onExecfailure(s);
                }

                @Override
                public void onSuccess(String s) {
                    mVPCallback.onExecSuccess(s);
                }

                @Override
                public void onProgress(String s) {
                    Log.d(Const.APP_TAG, s);
                    String ss = s.replaceAll("\\s+","");
                    if(ss.contains("time=")){

                        String[] extracts = ss.split("=");
                        String e = String.valueOf(extracts[5]);
                        Log.d(Const.APP_TAG, "progress "+e);
                        String[] extracts2 = e.split("\\.");
                        int pt = Helper.timeToInt(extracts2[0]) * 1000;
                        int pcn = (int) ((double) pt * 100 / (double) vidsource.getDuration()) ;
                        mVPCallback.onExecRunning(pcn, s);
                    }
                }

                @Override
                public void onStart() {
                    mVPCallback.onExecStart();
                }

                @Override
                public void onFinish() {
                    mVPCallback.onExecFinished();
                    if (tempDir.isDirectory()) {
                        for (File c : tempDir.listFiles()) {
                            if (!c.delete()) {
                                new FileNotFoundException("Failed to delete file: " + c);
                            }
                        }
                    }

                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            mVPCallback.ffmpegAlreadyRunning();
            e.printStackTrace();
        }
    }

    private static String toTime(int timeint){
        long hours = TimeUnit.MINUTES.toHours(timeint);
        long remainMinute = timeint - TimeUnit.HOURS.toMinutes(hours);
        String result = String.format("%02d", hours) + ":"
                + String.format("%02d", remainMinute);
        result = "00:"+result;
        return result;
    }


}
