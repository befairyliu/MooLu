package com.moolu.framework;

/**
 * This classes purpose is to allow a {@link AsyncTaskWithCallback} to callback to an
 * {@link android.app.Activity} implementing this interface with an instance of its self
 * when the task completes.
 */
public interface ActivityCallback {
    /**
    * If an instance of {@link AsyncTaskWithCallback} has been created with no ref argument,
    * when the owning implementation of {@link ActivityCallback) is called, this will be the ref argument.
    */
    public static int NO_REF = -1;

    /**
     * This method should be implemented to handle a callback from a {@link AsyncTaskWithCallback}, this method will be called from the UI
     * thread once the task completes, from its implementation of onPostExecute).
     *
     * @param ref
     * @param task
     */
    @SuppressWarnings("rawtypes")
    public void handleCallback(final AsyncTaskWithCallback task, final int ref);


}
