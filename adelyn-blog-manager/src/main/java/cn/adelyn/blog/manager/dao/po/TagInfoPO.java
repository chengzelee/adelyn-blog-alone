package cn.adelyn.blog.manager.dao.po;

import cn.adelyn.framework.database.pojo.po.BasePO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tag_info")
public class TagInfoPO extends BasePO {
    @TableId
    private Long tagId;
    private String tagName;
    private Long userId;
}
