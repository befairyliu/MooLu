package com.moolu.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.moolu.R;

/**
 * Created by Nanan on 2/3/2015.
 */
public class LoadingDialog extends ProgressDialog{
    private String message;
    private LoadingDialog loadingDialog = null;
    public LoadingDialog(Context context,String message) {
        super(context,R.style.dialog_not_dim);
        this.message = message;
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
        TextView mMsgView = (TextView)findViewById(R.id.message_view);
        if(message!=null && message.length()>0){
            mMsgView.setVisibility(View.VISIBLE);
            mMsgView.setText(message);
        }else{
            mMsgView.setVisibility(View.GONE);
        }
    }
}
