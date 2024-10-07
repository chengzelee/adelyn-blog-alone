package cn.adelyn.blog.transFile.dao.po;

import cn.adelyn.framework.database.pojo.po.BasePO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("trans_code_info")
public class TransCodeInfoPO extends BasePO {
    @TableId
    private Long transCode;
}
