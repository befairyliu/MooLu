package com.moolu.framework;

import java.util.Map;

/**
 * Created by Nanan on 2/3/2015.
 */
public interface IHookCallback {

    /**
     * If an instance of {@link AsyncTaskWithCallback} has been created with no ref argument, when the owning implemention of
     * {@link ActivityCallback} is called, this will be the ref argument.
     */
    public static int NO_REF = -1;

    /**
     * This method should be implemented to handle a callback from a {@link AsyncTaskWithCallback}, this method will be called from the UI
     * thread once the task completes, from its implementation of onPostExecute).
     */
    public void handleHookCallback(final Map map);
}
