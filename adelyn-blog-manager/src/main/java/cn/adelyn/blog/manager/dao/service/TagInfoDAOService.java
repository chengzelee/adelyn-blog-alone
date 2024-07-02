package cn.adelyn.blog.manager.dao.service;

import cn.adelyn.blog.manager.dao.po.TagInfoPO;
import cn.adelyn.framework.database.pojo.dto.PageDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface TagInfoDAOService extends IService<TagInfoPO> {

    void insertTag(Long tagId, String tagName, Long userId);

    void deleteTag(List<Long> tagIdList);

    void updateTag(Long tagId, String tagName);

    List<TagInfoPO> getAllTags(Long userId);

    Page<TagInfoPO> getTagPage(Long userId, PageDTO pageDTO);

    List<TagInfoPO> getTagInfoListByTagList(List<Long> tagIdList);
}
