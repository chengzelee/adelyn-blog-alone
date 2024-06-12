package cn.adelyn.blog.manager.dao.service.impl;

import cn.adelyn.blog.manager.dao.mapper.BlogTagMappingMapper;
import cn.adelyn.blog.manager.dao.po.BlogTagMappingPO;
import cn.adelyn.blog.manager.dao.service.BlogTagMappingDAOService;
import cn.adelyn.framework.core.util.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BlogTagMappingDAOServiceImpl extends ServiceImpl<BlogTagMappingMapper, BlogTagMappingPO> implements BlogTagMappingDAOService {

    public void insertBlogTagMapping(Long blogId, List<Long> tagIdList) {
        List<BlogTagMappingPO> blogTagMappingPOList = new ArrayList<>();

        tagIdList.forEach(tagId -> {
            BlogTagMappingPO blogTagMappingPO = new BlogTagMappingPO();
            blogTagMappingPO.setBlogId(blogId);
            blogTagMappingPO.setTagId(tagId);
            blogTagMappingPOList.add(blogTagMappingPO);
        });

        saveBatch(blogTagMappingPOList, 100);
    }

    public List<BlogTagMappingPO> selectBlogTagMappingByBlogIdList(List<Long> blogIdList) {
        if (CollectionUtil.isEmpty(blogIdList)) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<BlogTagMappingPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(BlogTagMappingPO::getBlogId, blogIdList);

        return baseMapper.selectList(wrapper);
    }

    public List<BlogTagMappingPO> selectBlogTagMappingByTagIdList(List<Long> tagIdList) {
        if (CollectionUtil.isEmpty(tagIdList)) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<BlogTagMappingPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(BlogTagMappingPO::getTagId, tagIdList);

        return baseMapper.selectList(wrapper);
    }

    public void deleteBlogTagMappingByBlogId(Long blogId) {
        LambdaQueryWrapper<BlogTagMappingPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlogTagMappingPO::getBlogId, blogId);

        baseMapper.delete(wrapper);
    }
}
