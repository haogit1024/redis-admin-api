package com.czh.redis.common.util;

import org.aspectj.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MyFileUtilTest {
    @Test
    void testFileObject() {
        File dir = new File("/Users/chenzhihao/git_clones/my");
        System.out.println(Utils.object2Json(dir.list()));
    }

    @Test
    void getFileTree() {
        Map<String, List<MyFileUtil.FileItem>> res = MyFileUtil.getFileTree("/Users/chenzhihao/work_space");
        System.out.println(Utils.object2Json(res));
    }

    @Test
    void join() {
        String dirPath = "/usr/bin";
        System.out.println(MyFileUtil.join(dirPath, "a", "b", "c.html"));
        System.out.println(System.getProperty("user.home"));
    }
}