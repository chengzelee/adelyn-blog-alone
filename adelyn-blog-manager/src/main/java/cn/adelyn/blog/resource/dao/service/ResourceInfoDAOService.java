package cn.adelyn.blog.resource.dao.service;

import cn.adelyn.blog.resource.dao.po.ResourceInfoPO;
import cn.adelyn.blog.resource.pojo.bo.AddResourceInfoBO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ResourceInfoDAOService extends IService<ResourceInfoPO> {

    ResourceInfoPO getResourceInfoByResourceId(Long resourceId);

    void addResourceInfo(AddResourceInfoBO addResourceInfoBO);

    void deleteResourceByResourceId(List<Long> resourceIdList);
}
