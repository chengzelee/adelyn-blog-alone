package cn.adelyn.blog.resource.dao.po;

import cn.adelyn.framework.database.pojo.po.BasePO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@Data
@TableName("resource_info")
public class ResourceInfoPO extends BasePO {
    @TableId
    private Long resourceId;
    private String resourceName;
    private String absolutePath;
}
