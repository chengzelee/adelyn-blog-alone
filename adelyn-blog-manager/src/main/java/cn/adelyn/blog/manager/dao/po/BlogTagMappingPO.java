package cn.adelyn.blog.manager.dao.po;

import cn.adelyn.framework.database.pojo.po.BasePO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("blog_tag_mapping")
public class BlogTagMappingPO extends BasePO {
    @TableId
    private Long id;

    private Long blogId;
    private Long tagId;
}
