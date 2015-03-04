package com.moolu.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Nanan on 3/4/2015.
 */
public class ProgressSpinner extends ImageView{
    public ProgressSpinner(Context context) {
        super(context);
    }

    public ProgressSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Drawable drawable = this.getDrawable();
        if (drawable instanceof AnimationDrawable) {
            ((AnimationDrawable) drawable).start();
        }
    }
}
