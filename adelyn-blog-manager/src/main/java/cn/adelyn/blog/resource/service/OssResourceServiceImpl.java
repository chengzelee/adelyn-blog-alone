package cn.adelyn.blog.resource.service;

import cn.adelyn.blog.manager.service.SnowflakeService;
import cn.adelyn.blog.resource.dao.service.ResourceInfoDAOService;
import cn.adelyn.blog.resource.pojo.bo.AddResourceInfoBO;
import cn.adelyn.framework.cache.util.CaffeineCacheUtil;
import cn.adelyn.framework.core.execption.AdelynException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class OssResourceServiceImpl extends AbstractResourceService {

    private final OssService ossService;

    public Long addResource(String resourceName, InputStream inputStream) {
        Long resourceId = getNewResourceId();
        String absolutePath = resourceId + resourceName;

        ossService.putObject(absolutePath , inputStream);

        saveResourceInfo(absolutePath, resourceName, resourceId);
        return resourceId;
    }

    @Override
    public File getResourceFile(Long resourceId) {
        throw new AdelynException("can not get file from oss in current version");
    }

    public String generateDownloadUrl(Long resourceId, Long validMillSec) {
        String absolutePath = getAbsolutePathByResourceId(resourceId);

        return ossService.generateGetObjectUrl(absolutePath, validMillSec).toString();
    }

    public void deleteResource(List<Long> resourceIdList) {
        resourceIdList.forEach(resourceId -> {
            String absolutePath = getAbsolutePathByResourceId(resourceId);
            ossService.deleteObject(absolutePath);
        });

        deleteResourceInfo(resourceIdList);
    }
}
