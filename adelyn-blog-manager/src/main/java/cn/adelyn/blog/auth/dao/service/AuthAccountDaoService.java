package cn.adelyn.blog.auth.dao.service;

import cn.adelyn.blog.auth.dao.mapper.AuthAccountMapper;
import cn.adelyn.blog.auth.dao.po.AuthAccountPO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AuthAccountDaoService {

    @Autowired
    private AuthAccountMapper authAccountMapper;

    public AuthAccountPO getAuthAccountByUserName(String userName) {
        QueryWrapper<AuthAccountPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AuthAccountPO::getUserName, userName);

        return authAccountMapper.selectOne(queryWrapper);
    }

    public int insetAccount(AuthAccountPO authAccountPO) {
        return authAccountMapper.insert(authAccountPO);
    }
}
