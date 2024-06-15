package cn.adelyn.blog.manager.dao.service.impl;

import cn.adelyn.blog.manager.dao.mapper.BlogPicInfoMapper;
import cn.adelyn.blog.manager.dao.po.BlogPicInfoPO;
import cn.adelyn.blog.manager.dao.service.BlogPicInfoDAOService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BlogPicInfoDAOServiceImpl extends ServiceImpl<BlogPicInfoMapper, BlogPicInfoPO> implements BlogPicInfoDAOService {

    @Override
    public BlogPicInfoPO getBlogPicInfo(Long resourceId) {
        return getById(resourceId);
    }

    @Override
    public void insertBlogPicInfo(Long picId, Long resourceId) {
        BlogPicInfoPO blogPicInfoPO = new BlogPicInfoPO();
        blogPicInfoPO.setPicId(picId);
        blogPicInfoPO.setResourceId(resourceId);

        save(blogPicInfoPO);
    }

    @Override
    public void deleteBlogPicInfo(List<Long> picIdList) {
        removeBatchByIds(picIdList);
    }
}
