package com.moolu.http.fileupload.task;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Nanan on 3/2/2015.
 */
public class MooLuMultipartEntity {
    private MultipartProgressListener listener;
    private CountingOutputStream  countingOutputStream=null;
    public long totalSize;
    public MooLuMultipartEntity(){
        super();
        this.listener=null;
    }

    public MooLuMultipartEntity(final MultipartProgressListener listener) {
        super();
        this.listener = listener;
    }

    public void cancel(){
        countingOutputStream.cancelFlag=true;
    }

    public void setMultipartEntityProgressListener(MultipartProgressListener listener){
        this.listener=listener;
    }

    public  class CountingOutputStream extends FilterOutputStream {
        private final MultipartProgressListener listener;
        private long transferred;
        public boolean cancelFlag=false;
        public CountingOutputStream(final OutputStream out,
                                    final MultipartProgressListener listener) {
            super(out);
            this.listener = listener;
            this.transferred = 0;
        }

        public void write(byte[] b, int off, int len) throws IOException {
            if(cancelFlag){
                throw new ForceCancelIOException ();
            }
            out.write(b, off, len);
            this.transferred += len;
            this.listener.transferred(MooLuMultipartEntity.this,this.transferred);
        }

        public void write(int b) throws IOException {
            if(cancelFlag){
                throw new ForceCancelIOException ();
            }
            out.write(b);
            this.transferred++;
            this.listener.transferred(MooLuMultipartEntity.this,this.transferred);
        }
    }
}
