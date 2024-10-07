package cn.adelyn.blog.transFile.dao.service.impl;

import cn.adelyn.blog.transFile.dao.mapper.TransFileInfoMapper;
import cn.adelyn.blog.transFile.dao.po.TransFileInfoPO;
import cn.adelyn.blog.transFile.dao.service.TransFileInfoDAOService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TransFileInfoDAOServiceImpl extends ServiceImpl<TransFileInfoMapper, TransFileInfoPO> implements TransFileInfoDAOService {

    @Override
    public void addTransFileInfo(Long fileId, Long resourceId) {
        TransFileInfoPO transFileInfoPO = new TransFileInfoPO();
        transFileInfoPO.setFileId(fileId);
        transFileInfoPO.setResourceId(resourceId);

        save(transFileInfoPO);
    }

    @Override
    public List<TransFileInfoPO> getTransFileInfoListByFileIdList(List<Long> fileIdList) {
        LambdaQueryWrapper<TransFileInfoPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TransFileInfoPO::getFileId, fileIdList);

        return Optional
                .ofNullable(list(queryWrapper))
                .orElse(new ArrayList<>());
    }

    @Override
    public void deleteTransFileInfoByIdList(List<Long> fileIdList) {
        removeBatchByIds(fileIdList);
    }
}
