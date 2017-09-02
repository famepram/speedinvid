package ekalaya.id.speedinvid.util;

import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import ekalaya.id.speedinvid.data.models.VideoSource;

/**
 * Created by Femmy on 8/27/2017.
 */

public class VideoProcessor {

    @Named("AppContext")
    @Inject
    Context ctx;

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

    public VideoProcessor(Context ctx){
        this.ctx = ctx;
//        prepareResult();
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

    public void loadFFMEPEG(){
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
                "-acodec","copy",
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

    private String buildPad(){
        int sh = getScreenHeight(); // screen height
        int sw = getScreenWidth(); // screen width

        double persori  = (double) sh / (double) sw;
        double persvid  = vidsource.getHeight() / vidsource.getWidth();

        int ph = 0;
        int pw = 0;

        int offsetX = 0;
        int offsetY = 0;

        if(persvid >= persori){
            ph      = vidsource.getHeight();
            pw      = vidsource.getHeight() * sw / sh ;
            offsetX = (pw - vidsource.getWidth()) / 2;
        } else {
            pw = vidsource.getWidth();
            ph = vidsource.getWidth() * sh / sw ;
            offsetY = (ph - vidsource.getHeight()) / 2;
        }
        String padparam = "pad=width="+pw+":height="+ph+":x="+offsetX+":y="+offsetY+":color=black";
        return padparam;
    }

    private boolean keepAudio(){
        if(vidsource.getSpeed() > 2){
            return false;
        } else if(vidsource.getSpeed() < 0.5){
            return false;
        } else if(vidsource.isRemoveAudio()){
            return false;
        }
        return true;
    }

    private double speedToSetPts(){
        double retval;
        double speed = vidsource.getSpeed();
        if(speed < 1.0){
            retval = 1 / speed;
        } else {
            retval = 100 / speed / 100;
        }
        retval = Math.ceil(retval * 10) /10;
        Log.d(Const.APP_TAG,"retval : "+retval);
//        DecimalFormat df = new DecimalFormat("#.#");
//        String sRetval = df.format(retval);
        return retval;
    }

    private String buildFilterComplex(){
        String fcv = "setpts="+speedToSetPts()+"*PTS";
        if(vidsource.isKeepPotrait()){
            fcv+=","+buildPad();
        }
        //fcv = "[0:v]"+fcv+"[v];[0:a]atempo="+vidsource.getSpeed()+"[a]";
        if(keepAudio()){
            fcv = "[0:v]"+fcv+"[v];[0:a]atempo="+vidsource.getSpeed()+"[a]";
//            fcv+="[0:a]atempo="+vidsource.getSpeed()+"[a]";
        }
        return fcv;
    }

    private String[] buildArguments(){
        String vidsrc = cutFirst() ? tempResultPath : vidsource.getPathsrc();
//        String setpts = setSpeed(vidsource.getSpeed());

        String filterComplex = buildFilterComplex();
        String fillterType = keepAudio() ? "-filter_complex":"-filter:v";

        String bitrate = ((int) Math.ceil(vidsource.getBitrate()/1000))+"K";
        Log.d(Const.APP_TAG," getBitrate : "+bitrate );
        String[] complexCommand = {
                "-i", vidsrc,
                fillterType,filterComplex,
                "-vcodec","libx264",
                "-pix_fmt","yuv420p",
//                "-filter:v","setpts="+setpts+"*PTS",
//                "-filter_complex","[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0[a]",
//                "-vf","",
//                "-vcodec","copy",
//                "-q","1",
//                "-crf","35",
                "-b:v","1M",
                "-minrate",bitrate,
                "-maxrate",bitrate,
                "-preset","ultrafast",
//                "-map", "[v]",
//                "-map", "[a]",
                "-safe", "0"};
        if(keepAudio()){
            complexCommand = append(complexCommand,"-map");
            complexCommand = append(complexCommand,"[v]");

            complexCommand = append(complexCommand,"-map");
            complexCommand = append(complexCommand,"[a]");

            complexCommand = append(complexCommand,"-b:a");
            complexCommand = append(complexCommand,"128k");
        } else {
            complexCommand = append(complexCommand,"-an");
        }

//        if(!keepAudio()){
//            Log.d(Const.APP_TAG," !keepAudio : false" );
//            complexCommand = append(complexCommand,"-an");
//        }
        complexCommand = append(complexCommand,videoFileResultPath);

//        Log.d(Const.APP_TAG," complexCommand : "+complexCommand.toString() );
//                "-an",
               // videoFileResultPath};
        return complexCommand;
    }

//    private String setSpeed(double speed){
//        String setpts = "";
//        if(speed < 1){
//            setpts = ((int) Math.ceil((1-speed)*10))+".0";
//        } else {
//            setpts = String.valueOf((100 / speed / 100));
//        }
//        return setpts;
//    }

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

                        String[] extracts2 = e.split("\\.");

                        int pt = Helper.timeToInt(extracts2[0]) * 1000;

                        int pcn = (int) ((double) pt * 100 / (double) vidsource.getProcessDuration());
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

    public int getScreenHeight(){
        DisplayMetrics metrics = new DisplayMetrics();
        int height= ctx.getResources().getDisplayMetrics().heightPixels;
        return height;
    }

    public int getScreenWidth(){
        DisplayMetrics metrics = new DisplayMetrics();
        int height= ctx.getResources().getDisplayMetrics().widthPixels;
        return height;
    }

    private static String[] append( String[] arr, String element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
    }


}
