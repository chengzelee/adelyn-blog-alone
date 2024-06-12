package cn.adelyn.blog.manager.dao.service.impl;

import cn.adelyn.blog.manager.dao.mapper.BlogInfoMapper;
import cn.adelyn.blog.manager.dao.po.BlogInfoPO;
import cn.adelyn.blog.manager.dao.service.BlogInfoDAOService;
import cn.adelyn.blog.manager.pojo.bo.GetBlogPageBO;
import cn.adelyn.blog.manager.pojo.bo.InsertBlogBO;
import cn.adelyn.blog.manager.pojo.bo.UpdateBlogBO;
import cn.adelyn.framework.core.cglib.BeanCopierUtil;
import cn.adelyn.framework.core.util.CollectionUtil;
import cn.adelyn.framework.database.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class BlogInfoDAOServiceImpl extends ServiceImpl<BlogInfoMapper, BlogInfoPO> implements BlogInfoDAOService {

    @Override
    public void insertBlogInfo(InsertBlogBO insertBlogBO) {
        BlogInfoPO blogInfoPO = BeanCopierUtil.copy(insertBlogBO, BlogInfoPO.class);

        save(blogInfoPO);
    }

    @Override
    public void updateBlogInfo(UpdateBlogBO updateBlogBO) {
        BlogInfoPO blogInfoPO = BeanCopierUtil.copy(updateBlogBO, BlogInfoPO.class);

        updateById(blogInfoPO);
    }

    @Override
    public void deleteBlogInfoByBlogId(Long blogId) {
        LambdaQueryWrapper<BlogInfoPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlogInfoPO::getBlogId, blogId);

        baseMapper.delete(wrapper);
    }

    @Override
    public List<BlogInfoPO> getBlogInfoListByBlogIdList(List<Long> blogIdList) {
        if (CollectionUtil.isEmpty(blogIdList)) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<BlogInfoPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(BlogInfoPO::getBlogId, blogIdList);

        return baseMapper.selectList(wrapper);
    }

    @Override
    public Page<BlogInfoPO> getBlogInfoPage(GetBlogPageBO getBlogPageBO) {
        LambdaQueryWrapper<BlogInfoPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(getBlogPageBO.getUserId()), BlogInfoPO::getUserId, getBlogPageBO.getUserId())
                .eq(Objects.nonNull(getBlogPageBO.getBlogVisible()), BlogInfoPO::getBlogVisible, getBlogPageBO.getBlogVisible());

        return baseMapper.selectPage(PageUtil.getPage(getBlogPageBO.getPageDTO()), wrapper);
    }
}
