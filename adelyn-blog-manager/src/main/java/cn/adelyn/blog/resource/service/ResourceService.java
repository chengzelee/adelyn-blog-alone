package cn.adelyn.blog.resource.service;

import cn.adelyn.blog.manager.service.SnowflakeService;
import cn.adelyn.blog.resource.dao.service.ResourceInfoDAOService;
import cn.adelyn.blog.resource.pojo.bo.AddResourceInfoBO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class ResourceService {

    private final SnowflakeService snowflakeService;
    private final OssService ossService;
    private final ResourceInfoDAOService resourceInfoDAOService;

    public Long addPic(String picName, InputStream inputStream) {
        Long resourceId = getResourceId();
        // todo 缓存查路径 不写死png
        String absolutePath = resourceId + ".png";

        AddResourceInfoBO addResourceInfoBO = AddResourceInfoBO.builder()
            .absolutePath(absolutePath)
            .resourceName(picName)
            .resourceId(resourceId)
            .build();

        resourceInfoDAOService.addResourceInfo(addResourceInfoBO);

        ossService.putObject(absolutePath , inputStream);
        return resourceId;
    }

    public void deleteResource(List<Long> resourceIdList) {
        resourceIdList.forEach(resourceId -> {
            ossService.deleteObject(resourceId + ".png");
        });

        resourceInfoDAOService.deleteResourceByResourceId(resourceIdList);
    }

    public String getPicUrl(Long picId) {
        // valid 20 sec
        // todo 缓存查路径 不写死png
        return ossService.generateGetObjectUrl(picId + ".png", 20000L).toString();
    }



    private Long getResourceId() {
        return snowflakeService.nextId();
    }
}
