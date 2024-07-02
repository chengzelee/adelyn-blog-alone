package cn.adelyn.blog.manager.service;

import cn.adelyn.blog.manager.dao.po.BlogTagMappingPO;
import cn.adelyn.blog.manager.dao.po.TagInfoPO;
import cn.adelyn.blog.manager.dao.service.BlogTagMappingDAOService;
import cn.adelyn.blog.manager.dao.service.TagInfoDAOService;
import cn.adelyn.blog.manager.pojo.vo.TagVO;
import cn.adelyn.framework.cache.util.CaffeineCacheUtil;
import cn.adelyn.framework.core.cglib.BeanCopierUtil;
import cn.adelyn.framework.core.execption.AdelynException;
import cn.adelyn.framework.core.pojo.vo.PageVO;
import cn.adelyn.framework.core.util.CollectionUtil;
import cn.adelyn.framework.database.pojo.dto.PageDTO;
import cn.adelyn.framework.database.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TagService {

    private final SnowflakeService snowflakeService;
    private final TagInfoDAOService tagInfoDAOService;
    private final BlogTagMappingDAOService blogTagMappingDAOService;

    private final String TAG_CACHE_KEY = "blog:tag";

    public Long insertTag(String tagName, Long userId) {
        Long tagId = snowflakeService.nextId();
        tagInfoDAOService.insertTag(tagId, tagName, userId);

        CaffeineCacheUtil.remove(TAG_CACHE_KEY);

        return tagId;
    }

    public void deleteTag(List<Long> tagIdList) {
        List<BlogTagMappingPO> blogTagMappingPOList = blogTagMappingDAOService.selectBlogTagMappingByTagIdList(tagIdList);

        if (CollectionUtil.isNotEmpty(blogTagMappingPOList)) {
            throw new AdelynException("current tag is mapping blog, can not delete");
        }

        tagInfoDAOService.deleteTag(tagIdList);

        CaffeineCacheUtil.remove(TAG_CACHE_KEY);
    }

    public void updateTag(Long tagId, String tagName) {
        tagInfoDAOService.updateTag(tagId, tagName);
    }

    public List<TagVO> getAllTags(Long userId) {

        return (List<TagVO>) CaffeineCacheUtil.get(TAG_CACHE_KEY, key -> {
            List<TagInfoPO> tagInfoPOList = tagInfoDAOService.getAllTags(userId);
            return BeanCopierUtil.copyList(tagInfoPOList, TagVO.class);
        }, 7, TimeUnit.DAYS);
    }

    public PageVO<TagVO> getTagPage(Long userId, PageDTO pageDTO) {
        Page<TagInfoPO> tagPOPage = tagInfoDAOService.getTagPage(userId, pageDTO);
        return PageUtil.getPageVO(tagPOPage, TagVO.class);
    }

    public PageVO<TagVO> getTagPageWithBlogNum(Long userId, PageDTO pageDTO) {
        Page<TagInfoPO> tagPOPage = tagInfoDAOService.getTagPage(userId, pageDTO);

        PageVO<TagVO> pageVO = PageUtil.getPageVO(tagPOPage, TagVO.class);

        List<Long> tagIdList = new ArrayList<>();
        pageVO.getList().forEach(tagVO -> {
            tagIdList.add(tagVO.getTagId());
        });

        Map<Long, List<BlogTagMappingPO>> tagId2BlogTagMappingListMap = blogTagMappingDAOService.selectBlogTagMappingByTagIdList(tagIdList)
                .stream()
                .collect(Collectors.groupingBy(BlogTagMappingPO::getTagId));

        pageVO.getList().forEach(tagVO -> {
            tagVO.setTagBlogNum(tagId2BlogTagMappingListMap.getOrDefault(tagVO.getTagId(), new ArrayList<>()).size());
        });

        return pageVO;
    }
}
