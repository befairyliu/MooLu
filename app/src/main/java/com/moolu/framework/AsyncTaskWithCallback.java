/*
 * This software is only to be used for the purpose for which it has been
 * provided. No part of it is to be reproduced, disassembled, transmitted,
 * stored in a retrieval system nor translated in any human or computer
 * language in any way or for any other purposes whatsoever without the
 * prior written consent of Nanan Holdings plc.
 */
package com.moolu.framework;

import android.os.AsyncTask;

/**
 * Created by Nanan on 2/3/2015.
 */
public abstract class AsyncTaskWithCallback<Params, Progress, Result>
        extends AsyncTask<Params, Progress, Result> {

    /**
     * Code returned by {@link AsyncTaskWithCallback#getError()} if the task executed successfully.
     */
    public final static int SUCCESS = 0;
    /**
     * Generate failure code.
     */
    public final static int FAILED = 1;
    /**
     * Task fatal code such as Out of meomory error
     */
    public final static int FATAL = 2;
    /**
     * Callback for task completion.
     */
    protected final ActivityCallback completionCallback;
    /**
     * Tasks reference.
     */
    protected final int taskReference;
    /**
     * Should return a defined code for an error occurring in this task
     */
    private int errorCode = AsyncTaskWithCallback.SUCCESS;
    /**
     * Any stored error message
     */
    private String error = null;
    /**
     * Result of the task if any;
     */
    private Result result;

    /**
     * Creates and instance of {@link AsyncTaskWithCallback} which on completion will call the
     * {@link ActivityCallback#handleCallback(AsyncTaskWithCallback, int)} with the given reference.
     * The reference must be <code>>= 0</code> or {@link ActivityCallback#NO_REF}.
     *
     * @param callback a non <code>null</code> implementor of {@link ActivityCallback}.
     * @param ref where <code>ref >= 0</code>
     */
    public AsyncTaskWithCallback(final ActivityCallback callback,final int ref) {
        //check params
        if(callback == null){
            throw new IllegalArgumentException("callback cannot be null");
        }
        if(ref != ActivityCallback.NO_REF && ref < 0){
            throw new IllegalArgumentException("ref must either be ActivityCallback.NO_REF or >= 0");
        }

        this.completionCallback = callback;
        this.taskReference = ref;
    }

    public final int getRef(){
        return this.taskReference;
    }

    public final Result getResult(){
        return this.result;
    }

    public final void setError(final int errorCode) {
        this.errorCode = errorCode;
    }

    public final void setError(final int errorCode,final String error) {
        this.errorCode = errorCode;
        this.error = error;
    }

    public final int getError(){
        return this.errorCode;
    }

    public final String getErrorMessage(){
        return this.error;
    }

    //do some prepare work for background thread, and called by UI thread.
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //after onPreExecute function, the function will do in background thread(do some work which cost time).
    //In this function, could call publishProgress function to show the progress
    @Override
    protected Result doInBackground(Params... params) {
        return null;
    }

    //after the publishProgress function be called, the onProgressUpdate function would be called in
    // UI thread to show the progress.
    @Override
    protected void onProgressUpdate(Progress... values) {
        super.onProgressUpdate(values);
    }

    //after doInBackground function, the onPostExecute function will be call by UI thread, and the
    // result will be deliver to the UI thread.
    @Override
    protected void onPostExecute(final Result result) {
        //TODO for AsyncTask class
        this.result = result;
        this.completionCallback.handleCallback(this,this.taskReference);
    }

    //There some guide line must be apply for AsyncTask class
    //
    //1. the instance of Task must be create in UI thread.
    //2. execute function must be called in UI thread.
    //3. do not call onPreExecute(), onPostExecute(Result), doInBackground(Params...) and
    //   onProgressUpdate(Progress...) function actively.
    //4. the task only could be executed once, else the exceptions will be throw out.

}
