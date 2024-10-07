package cn.adelyn.blog.transFile.dao.po;

import cn.adelyn.framework.database.pojo.po.BasePO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("trans_file_info")
public class TransFileInfoPO extends BasePO {
    @TableId
    private Long fileId;
    private Long resourceId;
}
