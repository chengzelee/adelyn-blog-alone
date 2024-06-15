package cn.adelyn.blog.manager.dao.service;

import cn.adelyn.blog.manager.dao.po.BlogPicInfoPO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BlogPicInfoDAOService extends IService<BlogPicInfoPO> {

    BlogPicInfoPO getBlogPicInfo(Long picId);

    void insertBlogPicInfo(Long picId, Long resourceId);

    void deleteBlogPicInfo(List<Long> picIdList);
}
