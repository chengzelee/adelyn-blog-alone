package cn.adelyn.blog.auth.dao.po;

import cn.adelyn.framework.database.pojo.po.BasePO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("auth_account")
public class AuthAccountPO extends BasePO {
    @TableId
    private Long userId;
    private String aliPayUserId;
    private String userName;
    private String password;
    private Integer status;
}
