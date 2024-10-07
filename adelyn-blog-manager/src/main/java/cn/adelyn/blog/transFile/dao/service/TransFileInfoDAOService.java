package cn.adelyn.blog.transFile.dao.service;

import cn.adelyn.blog.transFile.dao.po.TransFileInfoPO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface TransFileInfoDAOService extends IService<TransFileInfoPO> {

    void addTransFileInfo(Long fileId, Long resourceId);

    List<TransFileInfoPO> getTransFileInfoListByFileIdList(List<Long> fileIdList);

    void deleteTransFileInfoByIdList(List<Long> fileIdList);
}
