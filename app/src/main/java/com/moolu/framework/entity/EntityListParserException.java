package com.moolu.framework.entity;

/**
 * Created by Nanan on 2/26/2015.
 */
public class EntityListParserException extends Exception{
    private static final long serialVersionUID = 1;

    public EntityListParserException(){
        super();
    }

    public EntityListParserException(final String detailMessage, final Throwable throwable){
        super(detailMessage,throwable);
    }

    public EntityListParserException(final String detailMessage){
        super(detailMessage);
    }

    public EntityListParserException(final Throwable throwable){
        super(throwable);
    }

}
