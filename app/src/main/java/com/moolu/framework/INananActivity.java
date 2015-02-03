package com.moolu.framework;

/**
 * Created by Nanan on 2/3/2015.
 */
public interface INananActivity {
    public void maskScreen();
    public void removeMaskScreen();
    public void release();
    public int getActivityStatus();
}
