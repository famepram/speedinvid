package ekalaya.id.speedinvid.data.models;

import android.net.Uri;
import android.support.annotation.IntegerRes;
import android.util.Log;

import ekalaya.id.speedinvid.util.Helper;

import static ekalaya.id.speedinvid.util.Const.APP_TAG;

/**
 * Created by Femmy on 8/27/2017.
 */

public class VideoSource {

    private String pathsrc;

    private int duration;

    private int trimmedstart;

    private int trimmedend;

    private int size;

    private int height;

    private int width;

    private int start;

    private int finish;

    private double speed = 1.0;

    private int quality;

    private int bitrate;

    private boolean keepPotrait;

    private boolean removeAudio;

    public VideoSource(){}

    public VideoSource(String pathsrc) {
        this.pathsrc = pathsrc;
    }

    public String getPathsrc() {
        return pathsrc;
    }

    public void setPathsrc(String pathsrc) {
        this.pathsrc = pathsrc;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTrimmedstart() {
        return trimmedstart;
    }

    public void setTrimmedstart(int trimmedstart) {
        this.trimmedstart = trimmedstart;
    }

    public int getTrimmedend() {
        return trimmedend;
    }

    public void setTrimmedend(int trimmedend) {
        this.trimmedend = trimmedend;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public String getFileName(){
        Uri vidUri = Uri.parse(pathsrc);
        String filename = vidUri.getLastPathSegment().substring(0, vidUri.getLastPathSegment().lastIndexOf('.'));
        return filename;
    }

    public String getFileExt(){
        Uri vidUri      = Uri.parse(pathsrc);
        String fileExt  = vidUri.getLastPathSegment()
                            .substring(vidUri.getLastPathSegment()
                            .lastIndexOf(".") + 1, vidUri.getLastPathSegment().length());
        return fileExt;
    }

    public String getFormattedStart(){
        return Helper.formatTime(start);
    }

    public String getFormattedFinish(){
        return Helper.formatTime(finish);
    }

    public int getPercentStartCut(){
        double i = (double) getStart() / (double) getDuration();
        int result = (int) Math.ceil(i * 100);
        return result;
    }

    public int getPercentFinishCut(){
        double i =  ((double) getDuration() - (double) getFinish()) / (double) getDuration();
        int result = (int) Math.ceil(i * 100);
        return result;
    }

    public int getPercentTrimmed(){
        int result = 100 - (getPercentStartCut() + getPercentFinishCut());
        return result;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public boolean isKeepPotrait() {
        return keepPotrait;
    }

    public void setKeepPotrait(boolean keepPotrait) {
        this.keepPotrait = keepPotrait;
    }

    public boolean isRemoveAudio() {
        return removeAudio;
    }

    public void setRemoveAudio(boolean removeAudio) {
        this.removeAudio = removeAudio;
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public int getProcessDuration(){
        int dur = getFinish() - getStart();
        double time = dur / getSpeed();
        time = time + ((int) Math.ceil(5 * time / 100));
        return ((int) time);
    }
}
