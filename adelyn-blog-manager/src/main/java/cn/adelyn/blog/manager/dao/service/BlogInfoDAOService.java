package cn.adelyn.blog.manager.dao.service;

import cn.adelyn.blog.manager.dao.po.BlogInfoPO;
import cn.adelyn.blog.manager.pojo.bo.GetBlogPageBO;
import cn.adelyn.blog.manager.pojo.bo.InsertBlogBO;
import cn.adelyn.blog.manager.pojo.bo.UpdateBlogBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BlogInfoDAOService extends IService<BlogInfoPO> {

    void insertBlogInfo(InsertBlogBO insertBlogBO);

    void updateBlogInfo(UpdateBlogBO updateBlogBO);

    void deleteBlogInfoByBlogId(Long blogId);

    List<BlogInfoPO> getBlogInfoListByBlogIdList(List<Long> blogIdList);

    Page<BlogInfoPO> getBlogInfoPage(GetBlogPageBO getBlogPageBO);
}
