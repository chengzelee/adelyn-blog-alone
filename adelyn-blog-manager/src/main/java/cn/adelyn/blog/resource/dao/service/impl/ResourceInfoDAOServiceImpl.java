package cn.adelyn.blog.resource.dao.service.impl;

import cn.adelyn.blog.resource.dao.mapper.ResourceInfoMapper;
import cn.adelyn.blog.resource.dao.po.ResourceInfoPO;
import cn.adelyn.blog.resource.dao.service.ResourceInfoDAOService;
import cn.adelyn.blog.resource.pojo.bo.AddResourceInfoBO;
import cn.adelyn.framework.core.cglib.BeanCopierUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class ResourceInfoDAOServiceImpl extends ServiceImpl<ResourceInfoMapper, ResourceInfoPO> implements ResourceInfoDAOService {

    public ResourceInfoPO getResourceInfoByResourceId(Long resourceId) {
        LambdaQueryWrapper<ResourceInfoPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ResourceInfoPO::getResourceId, resourceId);
        return getOne(queryWrapper);
    }

    public void addResourceInfo(AddResourceInfoBO addResourceInfoBO) {
        ResourceInfoPO resourceInfoPO = BeanCopierUtil.copy(addResourceInfoBO, ResourceInfoPO.class);

        save(resourceInfoPO);
    }
}
