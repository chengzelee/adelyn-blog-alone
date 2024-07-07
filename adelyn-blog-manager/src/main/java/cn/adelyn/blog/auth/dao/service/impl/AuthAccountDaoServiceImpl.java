package cn.adelyn.blog.auth.dao.service.impl;

import cn.adelyn.blog.auth.dao.mapper.AuthAccountMapper;
import cn.adelyn.blog.auth.dao.po.AuthAccountPO;
import cn.adelyn.blog.auth.dao.service.AuthAccountDAOService;
import cn.adelyn.blog.manager.dao.mapper.BlogContentMapper;
import cn.adelyn.blog.manager.dao.po.BlogContentPO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AuthAccountDaoServiceImpl extends ServiceImpl<AuthAccountMapper, AuthAccountPO> implements AuthAccountDAOService {

    @Autowired
    private AuthAccountMapper authAccountMapper;

    public AuthAccountPO getAuthAccountByUserName(String userName) {
        QueryWrapper<AuthAccountPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AuthAccountPO::getUserName, userName);

        return authAccountMapper.selectOne(queryWrapper);
    }

    @Override
    public AuthAccountPO getUserInfoByAliPauUserId(String aliPayUserId) {
        QueryWrapper<AuthAccountPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AuthAccountPO::getAliPayUserId, aliPayUserId);

        return authAccountMapper.selectOne(queryWrapper);
    }

    public int insetAccount(AuthAccountPO authAccountPO) {
        return authAccountMapper.insert(authAccountPO);
    }
}
