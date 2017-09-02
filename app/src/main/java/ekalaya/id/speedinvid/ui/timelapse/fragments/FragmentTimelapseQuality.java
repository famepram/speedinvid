package ekalaya.id.speedinvid.ui.timelapse.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import ekalaya.id.speedinvid.R;
import ekalaya.id.speedinvid.data.models.VideoSource;
import ekalaya.id.speedinvid.ui.timelapse.TimelapseActivity;

public class FragmentTimelapseQuality extends Fragment
                                    implements FragmentTimelapseQualityContract.View,
                                                CompoundButton.OnCheckedChangeListener{


    private OnFragmentInteractionListener mListener;

    FragmentTimelapseQualityPresenter presenter;


    Switch aSwitch, switchAudio;


    TimelapseActivity mAct;

    public FragmentTimelapseQuality() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timelapse_quality, container, false);
        mAct = (TimelapseActivity) getActivity();
        presenter = new FragmentTimelapseQualityPresenter(this);
        aSwitch     = (Switch) v.findViewById(R.id.switch1);
        aSwitch.setOnCheckedChangeListener(this);
        switchAudio = (Switch) v.findViewById(R.id.switch2);
        switchAudio.setOnCheckedChangeListener(this);
        mListener.delegateFQltySwitchView(switchAudio);
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
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
        if(id == aSwitch.getId()){
            presenter.changeOrientationMode(mAct.getVidSource(),b);
        } else if(id == switchAudio.getId()){
            presenter.changeRemovingAudio(mAct.getVidSource(),b);
        }
    }

    @Override
    public void vidsourceModified(VideoSource vs) {
        mListener.FQVidSourceModified(vs);
    }

    public interface OnFragmentInteractionListener {
        void FQVidSourceModified(VideoSource vs);

        void delegateFQltySwitchView(Switch s);
    }
}
