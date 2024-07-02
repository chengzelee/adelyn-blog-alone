package cn.adelyn.blog.manager.service;

import cn.adelyn.blog.manager.dao.po.BlogInfoPO;
import cn.adelyn.blog.manager.dao.po.BlogTagMappingPO;
import cn.adelyn.blog.manager.dao.po.TagInfoPO;
import cn.adelyn.blog.manager.dao.service.*;
import cn.adelyn.blog.manager.event.listener.bo.EventDeleteBlogBO;
import cn.adelyn.blog.manager.event.listener.bo.EventInsertBlogBO;
import cn.adelyn.blog.manager.event.listener.bo.EventUpdateBlogBO;
import cn.adelyn.blog.manager.pojo.bo.GetBlogPageBO;
import cn.adelyn.blog.manager.pojo.bo.InsertBlogBO;
import cn.adelyn.blog.manager.pojo.bo.UpdateBlogBO;
import cn.adelyn.blog.manager.pojo.dto.GetBlogPageDTO;
import cn.adelyn.blog.manager.pojo.vo.BlogVO;
import cn.adelyn.blog.resource.service.ResourceService;
import cn.adelyn.framework.core.cglib.BeanCopierUtil;
import cn.adelyn.framework.core.pojo.vo.PageVO;
import cn.adelyn.framework.core.util.ConcurrentUtil;
import cn.adelyn.framework.database.util.PageUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class BlogService {

    private final SnowflakeService snowflakeService;
    private final ApplicationEventPublisher eventPublisher;
    private final BlogInfoDAOService blogInfoDAOService;
    private final BlogContentDAOService blogContentDAOService;
    private final BlogTagMappingDAOService blogTagMappingDAOService;
    private final BlogPicMappingDAOService blogPicMappingDAOService;
    private final TagInfoDAOService tagInfoDAOService;
    private final BlogPicService blogPicService;

    public Long insertBlog(InsertBlogBO insertBlogBO) {
        Long blogId = snowflakeService.nextId();
        insertBlogBO.setBlogId(blogId);

        ConcurrentUtil.processTask(() -> {
            blogInfoDAOService.insertBlogInfo(insertBlogBO);
            blogContentDAOService.insertBlogContent(blogId, insertBlogBO.getBlogContent());
            blogTagMappingDAOService.insertBlogTagMapping(blogId, insertBlogBO.getTagIdList());
            blogPicMappingDAOService.insertBlogPicMapping(blogId, insertBlogBO.getPicIdList());
            log.info("insert blog to db success, blogId:{}", blogId);
        });

        EventInsertBlogBO eventInsertBlogBO = BeanCopierUtil.copy(insertBlogBO, EventInsertBlogBO.class);
        eventPublisher.publishEvent(eventInsertBlogBO);

        return blogId;
    }

    public void updateBlog(UpdateBlogBO updateBlogBO) {
        Long blogId = updateBlogBO.getBlogId();

        ConcurrentUtil.processTask(() -> {
            blogInfoDAOService.updateBlogInfo(updateBlogBO);
            blogContentDAOService.updateBlogContent(blogId, updateBlogBO.getBlogContent());
            blogTagMappingDAOService.deleteBlogTagMappingByBlogId(blogId);
            blogTagMappingDAOService.insertBlogTagMapping(blogId, updateBlogBO.getTagIdList());
            blogPicMappingDAOService.insertBlogPicMapping(blogId, updateBlogBO.getPicIdList());
            log.info("update blog to db success, blogId: {}", blogId);
        });

        EventUpdateBlogBO eventUpdateBlogBO = BeanCopierUtil.copy(updateBlogBO, EventUpdateBlogBO.class);
        eventPublisher.publishEvent(eventUpdateBlogBO);
    }

    public void deleteBlog(Long blogId) {
        ConcurrentUtil.processTask(() -> {
            blogInfoDAOService.deleteBlogInfoByBlogId(blogId);
            blogContentDAOService.deleteBlogContent(blogId);
            List<Long> picIdList = blogPicMappingDAOService.selectPicIdListByBlogId(blogId);
            blogPicService.deletePic(picIdList);
            blogTagMappingDAOService.deleteBlogTagMappingByBlogId(blogId);
            blogPicMappingDAOService.deleteBlogPicMappingByBlogId(blogId);
            log.info("delete blog from db success, blogId: {}", blogId);
        });

        EventDeleteBlogBO eventDeleteBlogBO = new EventDeleteBlogBO();
        eventDeleteBlogBO.setBlogId(blogId);
        eventPublisher.publishEvent(eventDeleteBlogBO);
    }

    public PageVO<BlogVO> getBlogVOPage(GetBlogPageDTO blogSearchPageDTO) {
        Page<BlogInfoPO> blogInfoPOPage = blogInfoDAOService.getBlogInfoPage(BeanCopierUtil.copy(blogSearchPageDTO, GetBlogPageBO.class));

        List<Long> blogIdList = blogInfoPOPage.getRecords().stream().map(BlogInfoPO::getBlogId).toList();
        List<BlogVO> blogVOList = getBlogVOList(blogIdList);

        PageVO<BlogVO> pageVO = PageUtil.getPageVO(blogInfoPOPage, BlogVO.class);
        pageVO.setList(blogVOList);
        return pageVO;
    }

    public List<BlogVO> getBlogVOList(List<Long> blogIdList) {
        List<BlogInfoPO> blogInfoPOList = blogInfoDAOService.getBlogInfoListByBlogIdList(blogIdList);

        List<Long> allTagIdList = new ArrayList<>();

        Map<Long, List<Long>> blogId2BlogTagIdListMap = blogTagMappingDAOService.selectBlogTagMappingByBlogIdList(blogIdList)
                .stream()
                .peek(blogTagMappingPO -> allTagIdList.add(blogTagMappingPO.getTagId()))
                .collect(Collectors.groupingBy(BlogTagMappingPO::getBlogId, Collectors.mapping(BlogTagMappingPO::getTagId, Collectors.toList())));

        Map<Long, String> tagId2TagNameMap = tagInfoDAOService.getTagInfoListByTagList(allTagIdList.stream().distinct().toList())
                .stream()
                .collect(Collectors.toMap(TagInfoPO::getTagId, TagInfoPO::getTagName, (k1, k2) -> k2));

        List<BlogVO> blogVOList = blogInfoPOList.stream()
                .map(blogInfoPO -> {
                    Long blogId = blogInfoPO.getBlogId();
                    BlogVO blogVO = BeanCopierUtil.copy(blogInfoPO, BlogVO.class);

                    List<BlogVO.TagInfo> blogTagInfoList = new ArrayList<>();
                    if (Objects.nonNull(blogId2BlogTagIdListMap.get(blogId))) {
                        for (Long tagId : blogId2BlogTagIdListMap.get(blogId)) {
                            BlogVO.TagInfo tagInfo = BlogVO.TagInfo.builder()
                                    .tagId(tagId)
                                    .tagName(tagId2TagNameMap.get(tagId))
                                    .build();
                            blogTagInfoList.add(tagInfo);
                        }
                    }

                    blogVO.setBlogTagInfoList(blogTagInfoList);

                    return blogVO;
                })
                .toList();

        return blogVOList;
    }

}
