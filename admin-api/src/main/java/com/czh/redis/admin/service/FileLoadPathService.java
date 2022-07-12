package com.czh.redis.admin.service;

import com.czh.redis.common.emums.ResultEnum;
import com.czh.redis.common.entity.FileLoadPath;
import com.czh.redis.common.mapper.FileLoadPathMapper;
import com.czh.redis.common.util.MyFileUtil;
import com.czh.redis.common.view.BaseView;
import com.czh.redis.framework.exception.BusinessException;
import com.czh.redis.framework.service.BaseCurdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.czh.redis.common.constants.CommonConstants.RedisKey.REDIS_FILE_LOAD_PATH_FILE_LIST;

@Service
@Slf4j
public class FileLoadPathService extends BaseCurdService<FileLoadPathMapper, FileLoadPath, BaseView> {
    @Resource
    HttpServletResponse response;
    public void cachePathFile(@NotNull FileLoadPath fileLoadPath) {
        File pathFile = new File(fileLoadPath.getPath());
        if (!pathFile.exists()) {
            log.error("文件路径不存在, id: {}, path: {}", fileLoadPath.getId(), fileLoadPath.getPath());
            return;
        }
        if (!pathFile.isDirectory()) {
            log.error("文件路径不是文件夹, id: {}, path: {}", fileLoadPath.getId(), fileLoadPath.getPath());
            return;
        }
        Map<String, List<MyFileUtil.FileItem>> fileTree = MyFileUtil.getFileTree(fileLoadPath.getPath());
        fileTree.forEach((path, fileItems) -> {
            String key = MessageFormat.format(REDIS_FILE_LOAD_PATH_FILE_LIST, fileLoadPath.getId(), path);
            redisUtil.set(key, fileItems, 15, TimeUnit.MINUTES);
        });
    }

    public List<MyFileUtil.FileItem> getFileItems(@NotNull Integer id, @NotBlank String path) {
        String key = MessageFormat.format(REDIS_FILE_LOAD_PATH_FILE_LIST, id, path);
        return (List<MyFileUtil.FileItem>) redisUtil.get(key);
    }

    public void getFileContent(String path, String filename) throws IOException {
        String filepath = MyFileUtil.join(path, filename);
        File file = new File(filepath);
        if (!file.exists()) {
            log.error("文件不存在：{}", filepath);
            throw new BusinessException(ResultEnum.COMMON_ERROR.format("文件不存在：" + filepath));
        }
        MyFileUtil.responseFileContent(response, file);
    }
}
