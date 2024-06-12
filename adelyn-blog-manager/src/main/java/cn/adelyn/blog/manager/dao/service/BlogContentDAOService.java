package cn.adelyn.blog.manager.dao.service;

import cn.adelyn.blog.manager.dao.po.BlogContentPO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface BlogContentDAOService extends IService<BlogContentPO> {

    void insertBlogContent(Long blogId, String content);

    void updateBlogContent(Long blogId, String content);

    String getBlogContent(Long blogId);

    void deleteBlogContent(Long blogId);
}
