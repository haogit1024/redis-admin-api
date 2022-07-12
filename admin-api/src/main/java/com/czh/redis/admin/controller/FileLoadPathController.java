package com.czh.redis.admin.controller;

import com.czh.redis.admin.service.FileLoadPathService;
import com.czh.redis.common.entity.FileLoadPath;
import com.czh.redis.common.mapper.FileLoadPathMapper;
import com.czh.redis.common.util.MyFileUtil;
import com.czh.redis.common.view.BaseView;
import com.czh.redis.framework.annotation.Response;
import com.czh.redis.framework.controller.BaseRestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;

@Response
@RestController
@RequestMapping("file_load_path")
public class FileLoadPathController extends BaseRestController<FileLoadPathService, FileLoadPathMapper, FileLoadPath, BaseView> {
    @GetMapping("file_item")
    public List<MyFileUtil.FileItem> getFileItem(Integer id, String path, String filename) {
        return getServiceInstance().getFileItems(id, path, filename);
    }

    @GetMapping("file_content")
    public void getFileContent(String path, String filename) throws IOException {
        getServiceInstance().getFileContent(path, filename);
    }
}