package cn.adelyn.blog.transFile.dao.po;

import cn.adelyn.framework.database.pojo.po.BasePO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("trans_code_file_mapping")
public class TransCodeFileMappingPO extends BasePO {
    @TableId
    private Long id;
    private Long transCode;
    private Long fileId;
}
