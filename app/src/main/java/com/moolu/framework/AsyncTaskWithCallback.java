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
    protected final ActivityCallback comletionCallback;
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

        this.comletionCallback = callback;
        this.taskReference = ref;
    }

    @Override
    protected void onPostExecute(final Result result) {
        //TODO for AsyncTask class
        this.result = result;
        this.comletionCallback.handleCallback(this,this.taskReference);
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
}
