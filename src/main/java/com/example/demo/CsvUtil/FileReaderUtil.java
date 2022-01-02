package com.example.demo.CsvUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileReaderUtil {


    public static List<String> readfile(String filepath) throws FileNotFoundException, IOException {
        File file = new File(filepath);
        List<String> res=new ArrayList<>();
        if (file.isDirectory()) {
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                String readfile = filepath + "\\" + filelist[i];
                res.add(readfile);
            }
        }
        return res;
    }


    public static  Set<String> readfileName(String filepath) throws FileNotFoundException, IOException {
        File file = new File(filepath);
        Set<String> res=new HashSet<>();
        if (file.isDirectory()) {
            String[] filelist = file.list();
            for (String f : filelist) {
                String s = f.split(" ")[0];
                res.add(s);
            }
        }
        return res;
    }
}
