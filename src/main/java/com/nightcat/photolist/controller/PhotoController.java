package com.nightcat.photolist.controller;

import com.nightcat.photolist.PhotolistApplication;
import com.nightcat.photolist.util.FileUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * Created by Admin
 * User : Admin
 * Date : 2021-04-07 오후 3:47
 * Description :
 */
@Controller
public class PhotoController {

    @RequestMapping("/")
    public String index() {
        return "/views/index";
    }

    @RequestMapping("about")
    public String about() {
        return "/views/about";
    }

    @RequestMapping("blog")
    public String blog() {
        return "/views/blog";
    }

    @RequestMapping("contact")
    public String contact() {
        return "/views/contact";
    }

    @RequestMapping("elements")
    public String elements() {
        return "/views/elements";
    }

    @RequestMapping("main")
    public String main() {
        return "/views/main";
    }

    @RequestMapping("portfolio")
    public String portfolio(Model model) throws Exception {
        Class clazz = getClass();
        String path = "static/img/portfolio";

        File[] files = FileUtil.getResourceFolderFiles(clazz, path);
        String[] fileNameArr = null;
        if(files != null) {
            fileNameArr = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                fileNameArr[i] = "img/portfolio/" + file.getName();
                System.out.println("file : " + file.getName());
            }
        }

        model.addAttribute("imgs", fileNameArr);
        model.addAttribute("imgsCount", fileNameArr.length);
        return "/views/portfolio";
    }

    @RequestMapping("portfolio-1")
    public String portfolio1() {
        return "/views/portfolio-1";
    }

    @RequestMapping("portfolio-2")
    public String portfolio2() {
        return "/views/portfolio-2";
    }

}
