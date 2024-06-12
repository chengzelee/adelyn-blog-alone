package cn.adelyn.blog.manager.dao.service;

import cn.adelyn.blog.manager.dao.po.BlogTagMappingPO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BlogTagMappingDAOService extends IService<BlogTagMappingPO> {

    void insertBlogTagMapping(Long blogId, List<Long> tagIdList);

    List<BlogTagMappingPO> selectBlogTagMappingByBlogIdList(List<Long> blogIdList);

    List<BlogTagMappingPO> selectBlogTagMappingByTagIdList(List<Long> tagIdList);

    void deleteBlogTagMappingByBlogId(Long blogId);
}
