package cn.adelyn.blog.manager.service;

import cn.adelyn.blog.manager.dao.service.BlogPicInfoDAOService;
import cn.adelyn.blog.resource.service.ResourceService;
import cn.adelyn.framework.cache.util.CaffeineCacheUtil;
import cn.adelyn.framework.core.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BlogPicService {

    private final ResourceService resourceService;
    private final SnowflakeService snowflakeService;
    private final BlogPicInfoDAOService blogPicInfoDAOService;

    public Long addPic(String picName, InputStream inputStream) {
        Long picId = snowflakeService.nextId();
        // 没有格式的默认给png
        if (!StringUtil.hasLength(picName) || !picName.contains(".")) {
            picName = picName + ".png";
        }

        Long resourceId = resourceService.addResource(picName, inputStream);

        blogPicInfoDAOService.insertBlogPicInfo(picId, resourceId);
        return picId;
    }

    public String getPicUrl(Long picId) {
        Long resourceId = getResourceIdByPicId(picId);

        return (String) CaffeineCacheUtil.get("downloadUrl:" + picId,
                // 搞一个短期有效的链接，之的后走cdn
                (key) -> resourceService.generateDownloadUrl(resourceId, 1729000000L),
                20, TimeUnit.DAYS);
    }

    public void deletePic(List<Long> picIdList) {
        List<Long> resourceIdList = new ArrayList<>();
        picIdList.forEach(picId -> {
            resourceIdList.add(getResourceIdByPicId(picId));
            CaffeineCacheUtil.remove(picId);
        });

        resourceService.deleteResource(resourceIdList);
        blogPicInfoDAOService.deleteBlogPicInfo(picIdList);
    }

    public Long getResourceIdByPicId(Long picId) {
        return (Long) CaffeineCacheUtil.get("resourceId:" + picId,
                key -> blogPicInfoDAOService.getBlogPicInfo(picId).getResourceId(),
                3, TimeUnit.DAYS);
    }
}
