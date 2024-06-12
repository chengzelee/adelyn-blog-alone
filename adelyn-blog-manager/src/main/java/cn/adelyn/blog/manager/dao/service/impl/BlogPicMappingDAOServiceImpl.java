package cn.adelyn.blog.manager.dao.service.impl;

import cn.adelyn.blog.manager.dao.mapper.BlogPicMappingMapper;
import cn.adelyn.blog.manager.dao.po.BlogPicMappingPO;
import cn.adelyn.blog.manager.dao.service.BlogPicMappingDAOService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BlogPicMappingDAOServiceImpl extends ServiceImpl<BlogPicMappingMapper, BlogPicMappingPO> implements BlogPicMappingDAOService {

    @Override
    public void insertBlogPicMapping(Long blogId, List<Long> picIdList) {
        List<BlogPicMappingPO> blogPicMappingPOList = new ArrayList<>();

        picIdList.forEach(picId -> {
            BlogPicMappingPO blogPicMappingPO = new BlogPicMappingPO();
            blogPicMappingPO.setBlogId(blogId);
            blogPicMappingPO.setPicId(picId);
            blogPicMappingPOList.add(blogPicMappingPO);
        });

        saveBatch(blogPicMappingPOList, 100);
    }

    @Override
    public void deleteBlogPicMappingByBlogId(Long blogId) {
        LambdaQueryWrapper<BlogPicMappingPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlogPicMappingPO::getBlogId, blogId);

        baseMapper.delete(wrapper);
    }
}
