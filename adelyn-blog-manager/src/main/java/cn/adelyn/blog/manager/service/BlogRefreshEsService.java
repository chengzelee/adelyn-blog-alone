package cn.adelyn.blog.manager.service;

import cn.adelyn.blog.manager.dao.po.BlogContentPO;
import cn.adelyn.blog.manager.dao.po.BlogInfoPO;
import cn.adelyn.blog.manager.dao.service.BlogContentDAOService;
import cn.adelyn.blog.manager.dao.service.BlogInfoDAOService;
import cn.adelyn.blog.manager.event.listener.bo.EventInsertBlogBO;
import cn.adelyn.blog.search.service.EsRefreshService;
import cn.adelyn.framework.core.cglib.BeanCopierUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogRefreshEsService {

    private final BlogInfoDAOService blogInfoDAOService;
    private final BlogContentDAOService blogContentDAOService;
    private final EsRefreshService esRefreshService;
    private final ApplicationEventPublisher eventPublisher;

    public void refreshEs() {
        esRefreshService.recreateIndex();

        List<BlogInfoPO> blogInfoPOLIst = blogInfoDAOService.list();
        Map<Long, String> blogIdToContentMap = blogContentDAOService.list()
            .stream()
            .collect(Collectors.toMap(BlogContentPO::getBlogId, BlogContentPO::getBlogContent));

        blogInfoPOLIst.forEach(blogInfoPO -> {
            EventInsertBlogBO eventInsertBlogBO = BeanCopierUtil.copy(blogInfoPO, EventInsertBlogBO.class);
            eventInsertBlogBO.setBlogContent(blogIdToContentMap.get(blogInfoPO.getBlogId()));

            eventPublisher.publishEvent(eventInsertBlogBO);
        });
    }
}
