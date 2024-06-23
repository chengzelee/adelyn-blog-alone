package cn.adelyn.blog.resource.service;

import cn.adelyn.blog.manager.service.SnowflakeService;
import cn.adelyn.blog.resource.dao.service.ResourceInfoDAOService;
import cn.adelyn.blog.resource.pojo.bo.AddResourceInfoBO;
import cn.adelyn.framework.cache.util.CaffeineCacheUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class ResourceService {

    private final SnowflakeService snowflakeService;
    private final OssService ossService;
    private final ResourceInfoDAOService resourceInfoDAOService;

    public Long addResource(String resourceName, InputStream inputStream) {
        Long resourceId = getResourceId();
        String absolutePath = resourceId + resourceName;

        AddResourceInfoBO addResourceInfoBO = AddResourceInfoBO.builder()
                .absolutePath(absolutePath)
                .resourceName(resourceName)
                .resourceId(resourceId)
                .build();

        resourceInfoDAOService.addResourceInfo(addResourceInfoBO);

        ossService.putObject(absolutePath , inputStream);
        return resourceId;
    }

    public String generateDownloadUrl(Long resourceId, Long validMillSec) {
        String absolutePath = getAbsolutePathByResourceId(resourceId);

        return ossService.generateGetObjectUrl(absolutePath, validMillSec).toString();
    }

    public void deleteResource(List<Long> resourceIdList) {
        resourceIdList.forEach(resourceId -> {
            String absolutePath = getAbsolutePathByResourceId(resourceId);
            ossService.deleteObject(absolutePath);

            CaffeineCacheUtil.remove(resourceId);
        });
        resourceInfoDAOService.deleteResourceByResourceId(resourceIdList);
    }

    public String getAbsolutePathByResourceId(Long resourceId) {
        return (String) CaffeineCacheUtil.get("absolutePath:" + resourceId,
                key -> resourceInfoDAOService.getResourceInfoByResourceId(resourceId).getAbsolutePath(),
                3, TimeUnit.DAYS);
    }

    private Long getResourceId() {
        return snowflakeService.nextId();
    }
}
