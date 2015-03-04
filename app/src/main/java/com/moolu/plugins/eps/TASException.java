package com.moolu.plugins.eps;

/**
 * Created by Nanan on 3/4/2015.
 */
public class TASException extends Exception{
    private final static long serialVersionUID = 1L;
    public TASException(){
        super();
    }

    public TASException(final String detailMessage,final Throwable throwable){
        super(detailMessage,throwable);
    }

    public TASException(final String detailMessage){
        super(detailMessage);
    }

    public TASException(final Throwable throwable){
        super(throwable);
    }
}
