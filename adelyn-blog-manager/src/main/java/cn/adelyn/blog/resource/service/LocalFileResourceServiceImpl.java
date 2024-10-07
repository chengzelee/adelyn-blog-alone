package cn.adelyn.blog.resource.service;

import cn.adelyn.blog.manager.service.SnowflakeService;
import cn.adelyn.blog.transFile.service.FileStreamHandler;
import cn.adelyn.framework.core.execption.AdelynException;
import cn.adelyn.framework.core.util.RandomIdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocalFileResourceServiceImpl extends AbstractResourceService{

    private static final String FOLDER_PATH = "/docker/adelyn/blog/transFileTemp/";

    @Override
    public Long addResource(String resourceName, InputStream inputStream) {
        Long resourceId = getNewResourceId();

        String absolutePath = FOLDER_PATH + resourceId;

        log.info("start add local resource, resourceId: {}, path: {}", resourceId, absolutePath);

        try (InputStream fileInputStream = inputStream) {
            FileStreamHandler.uploadFile(fileInputStream, absolutePath);
        } catch (IOException e) {
            throw new AdelynException("Failed to upload file.");
        }

        saveResourceInfo(absolutePath, resourceName, resourceId);

        return resourceId;
    }

    @Override
    public File getResourceFile(Long resourceId) {
        String absolutePath = getAbsolutePathByResourceId(resourceId);
        File file = new File(absolutePath);

        if (!file.exists()) {
            throw new AdelynException("file not exists");
        }

        return file;
    }

    @Override
    public String generateDownloadUrl(Long resourceId, Long validMillSec) {
        throw new AdelynException("can not get file download url from local in current version");
    }

    @Override
    public void deleteResource(List<Long> resourceIdList) {
        resourceIdList.forEach(resourceId -> {
            String absolutePath = getAbsolutePathByResourceId(resourceId);

            File file = new File(absolutePath);

            if (!file.exists()) {
                return;
            }

            boolean res = file.delete();

            log.info("del local file {} res {}", absolutePath, res);
        });

        deleteResourceInfo(resourceIdList);
    }
}
