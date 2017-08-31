package ekalaya.id.speedinvid.ui.timelapse.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import ekalaya.id.speedinvid.R;
import ekalaya.id.speedinvid.data.models.VideoSource;
import ekalaya.id.speedinvid.ui.timelapse.TimelapseActivity;
import ekalaya.id.speedinvid.ui.timelapse.TimelineVideoAdapter;
import ekalaya.id.speedinvid.util.Const;

public class FragmentTimelapseTrim extends Fragment
                                implements FragmentTimelapseTrimContract.View,
                                            OnRangeSeekbarChangeListener{

    private String videoURI;

    private OnFragmentInteractionListener mListener;

    FragmentTimelapseTrimPresenter presenter;

    RecyclerView mRecycleView;

    TimelineVideoAdapter mAdapter;

    CrystalRangeSeekbar mSeekbar;

    TextView tvstart, tvend, tvprog;

    RelativeLayout rlLeft, rlCenter, rlRight;

    LinearLayout.LayoutParams pl, pc, pr;

    TimelapseActivity mAct;

    @Inject
    @Named("AppContext")
    Context context;

    public FragmentTimelapseTrim() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timelapse_trim, container, false);
        initUI(v);
        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new FragmentTimelapseTrimPresenter(this);
        presenter.drawTimeline(mAct.getVidSource().getPathsrc());
    }

    private void initUI(View v){
        mAct = (TimelapseActivity) getActivity();
        context = mAct.getApplicationContext();

        mRecycleView    = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        mRecycleView.setLayoutManager(mLayoutManager);

        mAdapter        = new TimelineVideoAdapter(null,context);
        mRecycleView.setAdapter(mAdapter);

        rlLeft      = (RelativeLayout) v.findViewById(R.id.rl_left_space);
        rlCenter    = (RelativeLayout) v.findViewById(R.id.rl_center_space);
        rlRight     = (RelativeLayout) v.findViewById(R.id.rl_right_space);

        pl          = (LinearLayout.LayoutParams) rlLeft.getLayoutParams();
        pc          = (LinearLayout.LayoutParams) rlCenter.getLayoutParams();
        pr          = (LinearLayout.LayoutParams) rlRight.getLayoutParams();

        mSeekbar    = (CrystalRangeSeekbar) v.findViewById(R.id.rangeSeekbar);
        tvstart     = (TextView) v.findViewById(R.id.tv_vidstart);
        tvend       = (TextView) v.findViewById(R.id.tv_vidend);
        tvprog      = (TextView) v.findViewById(R.id.tv_vidprogress);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void timelineDrawn(List<Bitmap> e) {
        mAdapter.setFiles(e);
        mSeekbar.setMinValue(0);
        mSeekbar.setMaxValue(mAct.getVidSource().getDuration());
        mSeekbar.setOnRangeSeekbarChangeListener(this);
    }

    @Override
    public void overlaySeekbarchange(int ml, int mr, int cw) {
        Log.d(Const.APP_TAG,"ml :"+ml+" mr : "+mr+" cw :"+cw);
        pl = pl == null ? (LinearLayout.LayoutParams) rlLeft.getLayoutParams() : pl;
        pc = pc == null ? (LinearLayout.LayoutParams) rlCenter.getLayoutParams() : pc;
        pr = pr == null ? (LinearLayout.LayoutParams) rlRight.getLayoutParams() : pr;

        pl.weight = ml;
        pc.weight = cw;
        pr.weight = mr;

        rlLeft.setLayoutParams(pl);
        rlCenter.setLayoutParams(pc);
        rlRight.setLayoutParams(pr);
    }

    @Override
    public void parentVideoSrcModified(VideoSource e) {
        mListener.videoModified(e);
        overlaySeekbarchange(e.getPercentStartCut(), e.getPercentFinishCut(), e.getPercentTrimmed());
        setTextTime(e.getFormattedStart(),e.getFormattedFinish());
    }

    @Override
    public void valueChanged(Number minValue, Number maxValue) {
        presenter.seekbarvaluechanged(minValue, maxValue, mAct.getVidSource());
    }

    //@Override
    public void setTextTime(String start, String end) {
        tvprog.setText(start);
        tvstart.setText(start);
        tvend.setText(end);
    }

    public void setTextProgress(String time) {
        tvprog.setText(time);
    }

    public interface OnFragmentInteractionListener {
        void videoModified(VideoSource e);
    }
}
