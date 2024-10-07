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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ResourceInfoDAOServiceImpl extends ServiceImpl<ResourceInfoMapper, ResourceInfoPO> implements ResourceInfoDAOService {

    public List<ResourceInfoPO> getResourceBaseInfoListByIdList(List<Long> resourceIdList) {
        LambdaQueryWrapper<ResourceInfoPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ResourceInfoPO::getResourceId, resourceIdList);

        return Optional
                .ofNullable(list(queryWrapper))
                .orElse(new ArrayList<>());
    }

    public ResourceInfoPO getResourceInfoByResourceId(Long resourceId) {
        return getById(resourceId);
    }

    public void addResourceInfo(AddResourceInfoBO addResourceInfoBO) {
        ResourceInfoPO resourceInfoPO = BeanCopierUtil.copy(addResourceInfoBO, ResourceInfoPO.class);

        save(resourceInfoPO);
    }

    @Override
    public void deleteResourceByResourceId(List<Long> resourceIdList) {
        removeBatchByIds(resourceIdList);
    }
}
