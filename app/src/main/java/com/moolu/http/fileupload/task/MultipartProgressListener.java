package com.moolu.http.fileupload.task;

/**
 * Created by Nanan on 3/2/2015.
 */
public interface MultipartProgressListener {
    void transferred(MooLuMultipartEntity multipartEntity, long num);
}
