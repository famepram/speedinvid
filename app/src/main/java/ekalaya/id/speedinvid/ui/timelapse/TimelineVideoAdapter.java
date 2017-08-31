package ekalaya.id.speedinvid.ui.timelapse;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ekalaya.id.speedinvid.R;

/**
 * Created by Femmy on 8/27/2017.
 */

public class TimelineVideoAdapter extends RecyclerView.Adapter<TimelineVideoAdapter.TimelineVideoAdapterrHolder>{

    private List<Bitmap> files;

    Context ctx;

    public TimelineVideoAdapter(List<Bitmap> files, Context ctx){
        this.files = files != null ? files : new ArrayList<Bitmap>();
        this.ctx = ctx;
    }

    @Override
    public TimelineVideoAdapterrHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tlv_item,parent,false);
        return new TimelineVideoAdapterrHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TimelineVideoAdapterrHolder holder, int position) {
        Bitmap file = files.get(position);
        holder.img.setImageBitmap(file);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public void setFiles (List<Bitmap> files){
        this.files = files;
        this.notifyDataSetChanged();
    }

    public class TimelineVideoAdapterrHolder extends RecyclerView.ViewHolder {

        ImageView img;
        RelativeLayout rlPad;
        public TimelineVideoAdapterrHolder(View itemView) {
            super(itemView);
            DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
            float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
            float dpWidth = displayMetrics.widthPixels - (24 * displayMetrics.density * 2);
            float perImg  = dpWidth / 8;
            float hpad    =  displayMetrics.heightPixels / displayMetrics.widthPixels * perImg;



            img = (ImageView) itemView.findViewById(R.id.iv_rv_item);
            rlPad = (RelativeLayout) itemView.findViewById(R.id.rl_rv_pad);
            rlPad.getLayoutParams().height = (int) hpad;
            rlPad.getLayoutParams().width = (int) perImg;

            img.getLayoutParams().height =  (int)perImg;
            img.getLayoutParams().width =  (int)perImg;
//            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            img.getLayoutParams().height = 210;
//            Log.d("TESTING", "dpWidth : "+width_img);
        }

    }


}
