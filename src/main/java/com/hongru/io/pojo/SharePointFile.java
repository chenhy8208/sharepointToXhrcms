package com.hongru.io.pojo;

import java.io.InputStream;

/**
 * Created by chenhongyu on 2016/10/30.
 */
public class SharePointFile {

    public String getMimeType() {
        if (filename.indexOf(".") < 0) {
            return "application/octet-stream";
        }

        String tailType = filename.substring(filename.indexOf(".")).toLowerCase();
        switch (tailType) {
            case ".png":
                return "image/png";
            case ".jpg":
                return "image/jpeg";
            case ".gif":
                return "image/gif";
            case ".pdf":
                return "application/pdf";
            case ".ppt":
                return "application/vnd.ms-powerpoint";
            case ".pptx":
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case ".doc":
                return "application/msword";
            case ".docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case ".xls":
                return "application/vnd.ms-excel";
            case ".xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet\n";
            default:
                return "application/octet-stream";
        }
    }


    private InputStream ioStream;
    private String filename;
    private String directory;
    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public InputStream getIoStream() {
        return ioStream;
    }

    public void setIoStream(InputStream ioStream) {
        this.ioStream = ioStream;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}
