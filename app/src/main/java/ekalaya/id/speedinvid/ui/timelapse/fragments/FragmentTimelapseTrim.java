package ekalaya.id.speedinvid.ui.timelapse.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class FragmentTimelapseTrim extends Fragment
                                implements FragmentTimelapseTrimContract.View,
                                            OnRangeSeekbarChangeListener{

    private static final String ARG_PARAM1 = "videouri";

    private String videoURI;

    private OnFragmentInteractionListener mListener;

    FragmentTimelapseTrimPresenter presenter;

    RecyclerView mRecycleView;

    TimelineVideoAdapter mAdapter;

    CrystalRangeSeekbar mSeekbar;

    TextView tvstart, tvend, tvprog;

    RelativeLayout rlLeft, rlCenter, rlRight;

    LinearLayout.LayoutParams pl, pc, pr;

    VideoSource vidsrc;

    @Inject
    @Named("AppContext")
    Context context;

    public FragmentTimelapseTrim() {
    }

    public static FragmentTimelapseTrim newInstance(String viduri) {
        FragmentTimelapseTrim fragment = new FragmentTimelapseTrim();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, viduri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            videoURI = getArguments().getString(ARG_PARAM1);
        }
        initPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timelapse_trim, container, false);
        initUI(v);
        initPresenter();
        presenter.drawTimeline(videoURI);

        return v;
    }


    private void initPresenter(){
        presenter = new FragmentTimelapseTrimPresenter(this);
    }

    private void initUI(View v){
        context = ((TimelapseActivity) getActivity()).getApplicationContext();

        mRecycleView    = (RecyclerView) v.findViewById(R.id.my_recycler_view);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        mRecycleView.setLayoutManager(mLayoutManager);

        mAdapter        = new TimelineVideoAdapter(null,context);
        mRecycleView.setAdapter(mAdapter);

        rlLeft      = (RelativeLayout) v.findViewById(R.id.rl_left_space);
        rlCenter    = (RelativeLayout) v.findViewById(R.id.rl_center_space);
        rlRight     = (RelativeLayout) v.findViewById(R.id.rl_right_space);
        mSeekbar    = (CrystalRangeSeekbar) v.findViewById(R.id.rangeSeekbar);
        tvstart     = (TextView) v.findViewById(R.id.tv_vidstart);
        tvend       = (TextView) v.findViewById(R.id.tv_vidend);
        tvprog      = (TextView) v.findViewById(R.id.tv_vidprogress);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    }

    @Override
    public void valueChanged(Number minValue, Number maxValue) {
        presenter.seekbarvaluechanged(minValue, maxValue);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
