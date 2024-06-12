package cn.adelyn.blog.manager.dao.service;

import cn.adelyn.blog.manager.dao.po.BlogPicMappingPO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BlogPicMappingDAOService extends IService<BlogPicMappingPO> {

    void insertBlogPicMapping(Long blogId, List<Long> picIdList);

    void deleteBlogPicMappingByBlogId(Long blogId);

    List<Long> selectPicIdListByBlogId(Long blogId);
}
