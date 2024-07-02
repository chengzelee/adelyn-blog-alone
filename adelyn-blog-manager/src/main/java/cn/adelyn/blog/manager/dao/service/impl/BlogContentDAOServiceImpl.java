package cn.adelyn.blog.manager.dao.service.impl;

import cn.adelyn.blog.manager.dao.mapper.BlogContentMapper;
import cn.adelyn.blog.manager.dao.po.BlogContentPO;
import cn.adelyn.blog.manager.dao.service.BlogContentDAOService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class BlogContentDAOServiceImpl extends ServiceImpl<BlogContentMapper, BlogContentPO>  implements BlogContentDAOService {

    @Override
    public void insertBlogContent(Long blogId, String content) {
        BlogContentPO blogContentPO = new BlogContentPO();
        blogContentPO.setBlogId(blogId);
        blogContentPO.setBlogContent(content);

        save(blogContentPO);
    }

    @Override
    public void updateBlogContent(Long blogId, String content) {
        BlogContentPO blogContentPO = new BlogContentPO();
        blogContentPO.setBlogId(blogId);
        blogContentPO.setBlogContent(content);

        saveOrUpdate(blogContentPO);
    }

    @Override
    public String getBlogContent(Long blogId) {
        return getById(blogId).getBlogContent();
    }

    @Override
    public void deleteBlogContent(Long blogId) {
        removeById(blogId);
    }
}
