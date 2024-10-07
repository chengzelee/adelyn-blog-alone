package cn.adelyn.blog.transFile.dao.service.impl;

import cn.adelyn.blog.transFile.dao.mapper.TransCodeInfoMapper;
import cn.adelyn.blog.transFile.dao.po.TransCodeInfoPO;
import cn.adelyn.blog.transFile.dao.service.TransCodeInfoDAOService;
import cn.adelyn.framework.cache.util.CaffeineCacheUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Repository
public class TransCodeInfoDAOServiceImpl extends ServiceImpl<TransCodeInfoMapper, TransCodeInfoPO> implements TransCodeInfoDAOService {

    private final String TRANS_CODE_KEY = "transCode:";

    @Override
    public void insertTransCodeInfo(Long transCode) {
        TransCodeInfoPO transCodeInfoPO = new TransCodeInfoPO();
        transCodeInfoPO.setTransCode(transCode);

        save(transCodeInfoPO);
        CaffeineCacheUtil.put(TRANS_CODE_KEY + transCode, 1, 7, TimeUnit.DAYS);
    }

    public boolean checkTransCodeExist(Long transCode) {

        boolean cacheHit = CaffeineCacheUtil.contains(TRANS_CODE_KEY + transCode);

        if (cacheHit) {
            return true;
        }

        TransCodeInfoPO transCodeInfoPO = getById(transCode);
        if (Objects.nonNull(transCodeInfoPO)) {
            CaffeineCacheUtil.put(TRANS_CODE_KEY + transCode, 1, 7, TimeUnit.DAYS);

            return true;
        } else {
            return false;
        }
    }
}
