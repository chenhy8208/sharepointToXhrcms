package com.hongru.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by chenhongyu on 2016/10/8.
 */
@Controller
public class PageController {

    @RequestMapping("/index")
    public void index(ModelMap model) {

    }

    private static final Logger logger = LogManager.getLogger(PageController.class);
}
