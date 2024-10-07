package cn.adelyn.blog.transFile.dao.service;

import cn.adelyn.blog.transFile.dao.po.TransCodeInfoPO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TransCodeInfoDAOService extends IService<TransCodeInfoPO> {

    void insertTransCodeInfo(Long transCode);

    boolean checkTransCodeExist(Long transCode);
}
