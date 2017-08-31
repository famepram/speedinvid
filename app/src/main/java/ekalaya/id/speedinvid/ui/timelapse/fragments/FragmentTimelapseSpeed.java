package ekalaya.id.speedinvid.ui.timelapse.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import ekalaya.id.speedinvid.R;
import ekalaya.id.speedinvid.data.models.VideoSource;
import ekalaya.id.speedinvid.ui.timelapse.TimelapseActivity;
import ekalaya.id.speedinvid.util.Const;

public class FragmentTimelapseSpeed extends Fragment
                                    implements FragmentTimelapseSpeedContract.View,
                                                SeekBar.OnSeekBarChangeListener{


    TimelapseActivity mAct;

    SeekBar mSeekbar;

    TextView tvSpeed;

    private OnFragmentInteractionListener mListener;

    FragmentTimelapseSpeedPresenter presenter;

    public FragmentTimelapseSpeed() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timelapse_speed, container, false);
        mAct = (TimelapseActivity) getActivity();
        presenter = new FragmentTimelapseSpeedPresenter(this);
        mSeekbar = (SeekBar) v.findViewById(R.id.speed_seekbar);
        mSeekbar.setProgress(50);
        mSeekbar.setOnSeekBarChangeListener(this);
        tvSpeed = (TextView) v.findViewById(R.id.speed_vid);

        return v;
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
    public void seekbarValueChanged(VideoSource vs) {
        tvSpeed.setText(vs.getSpeed()+"x");
        mListener.FSVidSourceModified(vs);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        presenter.changeSpeedVideo(mAct.getVidSource(),i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    public interface OnFragmentInteractionListener {
        void FSVidSourceModified(VideoSource vs);
    }
}
