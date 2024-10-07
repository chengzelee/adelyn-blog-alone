package cn.adelyn.blog.resource.service;

import cn.adelyn.blog.manager.service.SnowflakeService;
import cn.adelyn.blog.resource.dao.po.ResourceInfoPO;
import cn.adelyn.blog.resource.dao.service.ResourceInfoDAOService;
import cn.adelyn.blog.resource.pojo.bo.AddResourceInfoBO;
import cn.adelyn.framework.cache.util.CaffeineCacheUtil;
import jakarta.annotation.Resource;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class AbstractResourceService {

    @Resource protected SnowflakeService snowflakeService;
    @Resource protected ResourceInfoDAOService resourceInfoDAOService;

    public abstract Long addResource(String resourceName, InputStream inputStream);

    public abstract File getResourceFile(Long resourceId);
    public abstract String generateDownloadUrl(Long resourceId, Long validMillSec);

    public abstract void deleteResource(List<Long> resourceIdList);

    public ResourceInfoPO getResourceBaseInfo(Long resourceId) {
        return resourceInfoDAOService.getResourceInfoByResourceId(resourceId);
    }

    public List<ResourceInfoPO> getResourceBaseInfoListByIdList(List<Long> resourceIdList) {
        return resourceInfoDAOService.getResourceBaseInfoListByIdList(resourceIdList);
    }

    protected void saveResourceInfo(String absolutePath, String resourceName, Long resourceId) {
        AddResourceInfoBO addResourceInfoBO = AddResourceInfoBO.builder()
                .absolutePath(absolutePath)
                .resourceName(resourceName)
                .resourceId(resourceId)
                .build();

        resourceInfoDAOService.addResourceInfo(addResourceInfoBO);
    }

    protected void deleteResourceInfo(List<Long> resourceIdList) {
        resourceInfoDAOService.deleteResourceByResourceId(resourceIdList);

        resourceIdList.forEach(resourceId -> {
            CaffeineCacheUtil.remove("absolutePath:" + resourceId);
        });
    }

    protected String getAbsolutePathByResourceId(Long resourceId) {
        return (String) CaffeineCacheUtil.get("absolutePath:" + resourceId,
                key -> resourceInfoDAOService.getResourceInfoByResourceId(resourceId).getAbsolutePath(),
                3, TimeUnit.DAYS);
    }

    protected Long getNewResourceId() {
        return snowflakeService.nextId();
    }
}
