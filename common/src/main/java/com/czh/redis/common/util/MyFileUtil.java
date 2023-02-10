package com.czh.redis.common.util;

import com.czh.redis.common.emums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Slf4j
public class MyFileUtil {
    /**
     * 获取文件树
     *
     * @param path 文件夹路径
     * @return key: 文件夹完整路径，value: 文件夹下的所有文件名
     */
    public static Map<String, List<FileItem>> getFileTree(String path) {
        Map<String, List<FileItem>> res = new LinkedHashMap<>();
        collectFile(path, res);
        return res;
    }

    private static void collectFile(String path, Map<String, List<FileItem>> res) {
        File pathFile = new File(path);
        if (!pathFile.exists() || !pathFile.isDirectory()) {
            return;
        }
        String[] filenames = pathFile.list();
        List<FileItem> fileItems = new ArrayList<>(filenames.length);
        res.put(path, fileItems);
        for (String filename : filenames) {
            String tmpPath = join(path, filename);
            File tmpFile = new File(tmpPath);
            boolean isDir = tmpFile.isDirectory();
            int fileType = isDir ? CommonEnum.FileType.DIR.getValue() : CommonEnum.FileType.FILE.getValue();
            fileItems.add(FileItem.builder()
                    .path(tmpPath)
                    .name(filename)
                    .type(fileType)
                    .build());
            if (isDir) {
                collectFile(tmpPath, res);
            }
        }
    }

    public static String join(String dirPath, String... filePath) {
        StringBuilder sb = new StringBuilder();
        sb.append(dirPath);
        for (String path : filePath) {
            sb.append(File.separator).append(path);
        }
        return sb.toString();
    }

    public static void responseFileContent(HttpServletResponse response, File file) throws IOException {
        // todo 加入requestHeader的处理
        byte[] bytes = new byte[1024 * 10];
        int len;
        FileInputStream fileInputStream = new FileInputStream(file);
        String contentType = Files.probeContentType(file.toPath());
        response.setHeader("Content-Type", contentType);
        ServletOutputStream os = response.getOutputStream();
        while ((len = fileInputStream.read(bytes)) != -1) {
            os.write(bytes, 0, len);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FileItem {
        private String path;
        private String name;
        private Integer type;
    }
}
