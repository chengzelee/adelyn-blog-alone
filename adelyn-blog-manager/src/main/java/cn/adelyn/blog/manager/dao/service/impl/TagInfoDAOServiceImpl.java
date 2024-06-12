package cn.adelyn.blog.manager.dao.service.impl;

import cn.adelyn.blog.manager.dao.mapper.TagInfoMapper;
import cn.adelyn.blog.manager.dao.po.TagInfoPO;
import cn.adelyn.blog.manager.dao.service.TagInfoDAOService;
import cn.adelyn.framework.core.util.CollectionUtil;
import cn.adelyn.framework.database.pojo.dto.PageDTO;
import cn.adelyn.framework.database.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TagInfoDAOServiceImpl extends ServiceImpl<TagInfoMapper, TagInfoPO> implements TagInfoDAOService {

    @Override
    public void insertTag(Long tagId, String tagName, Long userId) {
        TagInfoPO tagInfoPO = new TagInfoPO();
        tagInfoPO.setTagId(tagId);
        tagInfoPO.setTagName(tagName);
        tagInfoPO.setUserId(userId);

        save(tagInfoPO);
    }

    @Override
    public void deleteTag(List<Long> tagIdList) {
        removeByIds(tagIdList);
    }

    @Override
    public void updateTag(Long tagId, String tagName) {
        TagInfoPO tagInfoPO = new TagInfoPO();
        tagInfoPO.setTagId(tagId);
        tagInfoPO.setTagName(tagName);

        updateById(tagInfoPO);
    }

    @Override
    public Page<TagInfoPO> getTagPage(Long userId, PageDTO pageDTO) {
        LambdaQueryWrapper<TagInfoPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(TagInfoPO::getTagId, TagInfoPO::getTagName)
                .eq(TagInfoPO::getUserId, userId);

        return baseMapper.selectPage(PageUtil.getPage(pageDTO), queryWrapper);
    }

    @Override
    public List<TagInfoPO> getTagInfoListByTagList(List<Long> tagIdList) {
        if (CollectionUtil.isEmpty(tagIdList)) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<TagInfoPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(TagInfoPO::getTagId, TagInfoPO::getTagName)
                .in(TagInfoPO::getTagId, tagIdList);

        return baseMapper.selectList(queryWrapper);
    }
}
