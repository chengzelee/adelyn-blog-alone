package cn.adelyn.blog.manager.service;

import cn.adelyn.blog.manager.dao.service.BlogPicInfoDAOService;
import cn.adelyn.blog.resource.service.OssResourceServiceImpl;
import cn.adelyn.framework.cache.util.CaffeineCacheUtil;
import cn.adelyn.framework.core.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BlogPicService {

    private final OssResourceServiceImpl ossResourceService;
    private final SnowflakeService snowflakeService;
    private final BlogPicInfoDAOService blogPicInfoDAOService;

    public Long addPic(String picName, InputStream inputStream) {
        Long picId = snowflakeService.nextId();
        // 没有格式的默认给png
        if (!StringUtil.hasLength(picName) || !picName.contains(".")) {
            picName = picName + ".png";
        }

        Long resourceId = ossResourceService.addResource(picName, inputStream);

        blogPicInfoDAOService.insertBlogPicInfo(picId, resourceId);
        return picId;
    }

    public String getPicUrl(Long picId) {
        Long resourceId = getResourceIdByPicId(picId);

        return (String) CaffeineCacheUtil.get("downloadUrl:" + picId,
                // oss 连接有效期和cdn失效时间相同
                (key) -> ossResourceService.generateDownloadUrl(resourceId, 2678400000L),
                31, TimeUnit.DAYS);
    }

    public void deletePic(List<Long> picIdList) {
        List<Long> resourceIdList = new ArrayList<>();
        picIdList.forEach(picId -> {
            resourceIdList.add(getResourceIdByPicId(picId));
            CaffeineCacheUtil.remove(picId);
        });

        ossResourceService.deleteResource(resourceIdList);
        blogPicInfoDAOService.deleteBlogPicInfo(picIdList);
    }

    public Long getResourceIdByPicId(Long picId) {
        return (Long) CaffeineCacheUtil.get("resourceId:" + picId,
                key -> blogPicInfoDAOService.getBlogPicInfo(picId).getResourceId(),
                3, TimeUnit.DAYS);
    }
}
