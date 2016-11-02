package com.hongru.controller;

import com.hongru.db.util.DBImportConfig;
import com.hongru.db.util.Db;
import com.hongru.io.pojo.SharePointFile;
import com.hongru.io.util.FileServer;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenhongyu on 2016/10/8.
 */
@Controller
public class PageController {

    @RequestMapping(value="/importStart", method=RequestMethod.POST)
    public @ResponseBody Map<String, Object> importStart(HttpServletRequest request, ModelMap model) {
        long startSecond = System.currentTimeMillis();

        Map<String, Object> responseBody = new HashMap<String, Object>();


        String share_url = request.getParameter("share_url");
        String share_username = request.getParameter("share_username");
        String share_password = request.getParameter("share_password");
        String cms_url = request.getParameter("cms_url");
        String cms_username = request.getParameter("cms_username");
        String cms_password = request.getParameter("cms_password");
        String allIn = request.getParameter("allIn");
        String import_time_area = request.getParameter("import_time_area");

        DBImportConfig dbi = new DBImportConfig.Builder()
                            .setAllIn(StringUtils.equals(allIn, "1")).setImport_time_area(import_time_area)
                            .setShare_password(share_password).setShare_url(share_url).setShare_username(share_username)
                            .setCms_password(cms_password).setCms_url(cms_url).setCms_username(cms_username)
                            .build();

        responseBody.put("state", true);  //导入成功
        try {
            new Db(dbi).dataImport();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            responseBody.put("state", false);  //导入失败
        }

        responseBody.put("time", (System.currentTimeMillis()-startSecond)/1000f);
        return responseBody;
    }


    @RequestMapping("/file_service")
    public void fileService(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        //图片显示或者下载
        String filePath = request.getParameter("f");
        if (StringUtils.isBlank(filePath)) return;

        SharePointFile file = new FileServer().getBinaryFromPath(filePath);
        if (file != null) {
            response.setContentType(file.getMimeType());
            if (file.getIoStream() == null) {
                try {
                    String p = request.getSession().getServletContext().getRealPath("/WEB-INF/views/share");

                    Path f = Paths.get(p + "/" + file.getFilename());
                    if (Files.exists(f))
                    {
                        file.setIoStream(new FileInputStream(f.toFile()));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                System.out.println("filename = " + file.getFilename()+", mime = " + file.getMimeType());
                System.out.println("file = " + file.getIoStream() + ", size=" + file.getIoStream().available());

                OutputStream output = response.getOutputStream();


                byte b[] = new byte[1024];
                int n;
                while ((n = file.getIoStream().read(b)) != -1) {
                    output.write(b, 0, n);
                }

                output.flush();
                output.close();
                file.getIoStream().close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @RequestMapping("/index")
    public void index(ModelMap model) {
    }

    @RequestMapping("/login")
    public void login(ModelMap model) {
    }

    private static final Logger logger = LogManager.getLogger(PageController.class);
}
