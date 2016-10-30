package com.hongru.io.util;

import com.hongru.config.GlobalConfig;
import com.hongru.controller.PageController;
import com.hongru.db.util.DBImportConfig;
import com.hongru.db.util.Db;
import com.hongru.io.pojo.SharePointFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chenhongyu on 2016/10/30.
 */
public class FileServer {

    DBImportConfig importConfig;
    DBImportConfig.Builder builder;
    public FileServer() {
        builder = new DBImportConfig.Builder();
        builder.setShare_url(GlobalConfig.SHARE_URL);
        builder.setShare_username(GlobalConfig.SHAER_NAME);
        builder.setShare_password(GlobalConfig.SHARE_PASSWORD);
        importConfig = builder.build();
    }

    public SharePointFile getBinaryFromPath(String path) {
        try {
            String realFilePath = new String(new BASE64Decoder().decodeBuffer(path), "utf-8");

            String filename = realFilePath.substring(realFilePath.lastIndexOf("/")).replace("/", "");
            String directory = realFilePath.replace("/" + filename, "");

            if (directory.startsWith("/")){
                directory = directory.substring(1);
            }

            System.out.println("filename = " + filename);
            System.out.println("directory = " + directory);

            InputStream io = null;
            try {
                io = new Db(importConfig).getFileStreamFromDatabase(directory, filename);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }

            SharePointFile spf = new SharePointFile();
            spf.setDirectory(directory);
            spf.setFilename(filename);
            spf.setIoStream(io);

            return spf;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static final Logger logger = LogManager.getLogger(FileServer.class);
}
