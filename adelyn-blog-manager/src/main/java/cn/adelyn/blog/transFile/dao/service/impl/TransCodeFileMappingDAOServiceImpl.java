package cn.adelyn.blog.transFile.dao.service.impl;

import cn.adelyn.blog.manager.service.SnowflakeService;
import cn.adelyn.blog.transFile.dao.mapper.TransCodeFileMappingMapper;
import cn.adelyn.blog.transFile.dao.po.TransCodeFileMappingPO;
import cn.adelyn.blog.transFile.dao.service.TransCodeFileMappingDAOService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TransCodeFileMappingDAOServiceImpl extends ServiceImpl<TransCodeFileMappingMapper, TransCodeFileMappingPO> implements TransCodeFileMappingDAOService {

    private final SnowflakeService snowflakeService;

    @Override
    public void addCodeFileMapping(Long transCode, Long fileId) {
        TransCodeFileMappingPO transCodeFileMappingPO = new TransCodeFileMappingPO();
        transCodeFileMappingPO.setId(snowflakeService.nextId());
        transCodeFileMappingPO.setTransCode(transCode);
        transCodeFileMappingPO.setFileId(fileId);

        save(transCodeFileMappingPO);
    }

    @Override
    public List<TransCodeFileMappingPO> getTransCodeFileMappingInfoList(Long transCode) {
        LambdaQueryWrapper<TransCodeFileMappingPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TransCodeFileMappingPO::getTransCode, transCode);

        return Optional.ofNullable(list(queryWrapper))
                .orElse(new ArrayList<>());
    }
}
