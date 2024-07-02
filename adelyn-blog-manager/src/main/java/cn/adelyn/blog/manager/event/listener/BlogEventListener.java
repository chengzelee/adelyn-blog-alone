package cn.adelyn.blog.manager.event.listener;

import cn.adelyn.blog.manager.event.listener.bo.EventDeleteBlogBO;
import cn.adelyn.blog.manager.event.listener.bo.EventInsertBlogBO;
import cn.adelyn.blog.manager.event.listener.bo.EventUpdateBlogBO;
import cn.adelyn.blog.manager.pojo.bo.InsertBlogBO;
import cn.adelyn.blog.search.service.BlogSearchService;
import cn.adelyn.framework.core.cglib.BeanCopierUtil;
import cn.adelyn.framework.core.util.ConcurrentUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class BlogEventListener {

    private final BlogSearchService blogSearchService;

    @EventListener
    public void listenInsertBlog(EventInsertBlogBO eventInsertBlogBO) {
        log.info("listen insert blog: {}", eventInsertBlogBO.getBlogTitle());
        ConcurrentUtil.processTask(() -> {
            InsertBlogBO insertBlogBO = BeanCopierUtil.copy(eventInsertBlogBO, InsertBlogBO.class);
            blogSearchService.insertBlog(insertBlogBO);
            log.info("insert blog to es success, blogId: {}", insertBlogBO.getBlogId());
        });
    }

    @EventListener
    public void listenUpdateBlog(EventUpdateBlogBO eventUpdateBlogBO) {
        log.info("listen update blog: {}", eventUpdateBlogBO.getBlogTitle());
        ConcurrentUtil.processTask(() -> {
            InsertBlogBO insertBlogBO = BeanCopierUtil.copy(eventUpdateBlogBO, InsertBlogBO.class);
            blogSearchService.updateBlog(insertBlogBO);
            log.info("update blog to es success, blogId: {}", insertBlogBO.getBlogId());
        });
    }

    @EventListener
    public void listenDeleteBlog(EventDeleteBlogBO eventDeleteBlogBO) {
        log.info("listen delete blog: {}", eventDeleteBlogBO.getBlogId());
        ConcurrentUtil.processTask(() -> {
            Long blogId = eventDeleteBlogBO.getBlogId();
            blogSearchService.deleteBlog(blogId);
            log.info("delete blog blog es success, blogId: {}", blogId);
        });
    }
}
