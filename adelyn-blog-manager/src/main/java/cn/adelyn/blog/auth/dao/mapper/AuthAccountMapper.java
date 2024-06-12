package cn.adelyn.blog.auth.dao.mapper;

import cn.adelyn.blog.auth.dao.po.AuthAccountPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chengze
 * @date 2022/12/20
 * @desc auth_account
 */
@Mapper
public interface AuthAccountMapper extends BaseMapper<AuthAccountPO> {
}
