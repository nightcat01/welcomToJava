package com.nightcat.photolist.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.support.ServletContextResourceLoader;

import javax.servlet.ServletContextListener;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin
 * User : Admin
 * Date : 2021-04-08 오전 10:17
 * Description :
 */
public class FileUtil {

    public static File[] getFileList(String path) throws Exception {
        File[] files = null;
        File file = new File(path);
        if(file != null) {
            files = file.listFiles();
        }
        return files;
    }

    public static File[] getResourceFolderFiles(Class clazz, String folder) {
        ClassLoader loader = clazz.getClassLoader();
        URL url = loader.getResource(folder);
        File file = null;
        File[] files = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            file = new File(url.getPath());
        }
        if(file != null) {
            files = file.listFiles();
        }

        return files;
    }
}
