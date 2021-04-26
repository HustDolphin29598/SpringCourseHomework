package com.onrmount.uploadfilejava;

import lombok.Data;

@Data
public class UploadDocumentResponse {

    private String fileName;
    private String fileDownloadUri;
    private long size;

    public UploadDocumentResponse(String fileName, String fileDownloadUri, long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.size = size;

    }
}
