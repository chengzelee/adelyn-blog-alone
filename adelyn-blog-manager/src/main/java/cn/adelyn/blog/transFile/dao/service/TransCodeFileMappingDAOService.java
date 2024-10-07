package cn.adelyn.blog.transFile.dao.service;

import cn.adelyn.blog.transFile.dao.po.TransCodeFileMappingPO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface TransCodeFileMappingDAOService extends IService<TransCodeFileMappingPO> {

    void addCodeFileMapping(Long transCode, Long fileId);

    List<TransCodeFileMappingPO> getTransCodeFileMappingInfoList(Long transCode);
}
