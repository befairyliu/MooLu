package com.ssl.cert;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.moolu.R;

/**
 * Created by Nanan on 2/13/2015.
 *
 */
public class MooFragment extends Fragment implements AdapterView.OnItemSelectedListener,MooAsyncTask.MooAsyncListener{
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String Tag = "MooFragment";

    private Spinner mUrlList;
    private TextView mContent;
    private String mUrl;

    public static MooFragment newInstance(int sectionNumber){
        MooFragment fragment = new MooFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MooFragment(){

    }

    @Override
    public void onSuccess(String result) {

    }

    @Override
    public void onErrorOrCancel() {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_main,container,false);
        mUrlList = (Spinner)rootView.findViewById(R.id.sp_url);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(rootView.getContext(),R.array.list_url,android.R.layout.simple_spinner_dropdown_item);
        mUrlList.setAdapter(adapter);
        mUrlList.setOnItemSelectedListener(this);
        mContent = (TextView)rootView.findViewById(R.id.tvContent);

        return rootView;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    public void fetch() {
        mContent.setText("");
        int i = getArguments().getInt(ARG_SECTION_NUMBER);
        MooAsyncTask task = new MooAsyncTask(this);
        task.execute(i, mUrl, this.getActivity());
    }
}

