package cn.adelyn.blog.auth.dao.service;

import cn.adelyn.blog.auth.dao.po.AuthAccountPO;
import cn.adelyn.blog.manager.dao.po.BlogContentPO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AuthAccountDAOService extends IService<AuthAccountPO> {

    AuthAccountPO getAuthAccountByUserName(String userName);

    AuthAccountPO getUserInfoByAliPauUserId(String aliPayUserId);

    int insetAccount(AuthAccountPO authAccountPO);
}
