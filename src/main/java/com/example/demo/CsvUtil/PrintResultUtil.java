package com.example.demo.CsvUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PrintResultUtil {

    public static void printAddressCoin(Map<String, List<String>> map) throws IOException {

        String path = "C:\\Users\\Lenovo\\Desktop\\AddressCoin.txt";
        File file = new File(path);
        //如果没有文件就创建
        if (!file.isFile()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));

        Set<String> set = map.keySet();

        for (String key : set) {
            List<String> list = map.get(key);
            if(list.size()<2){
                continue;
            }
            writer.write(key + ": "+"\r\n");

            for (String l : list) {
                writer.write(l+"\r\n");
            }
            writer.write("===========================");
        }
        writer.close();
    }
}
