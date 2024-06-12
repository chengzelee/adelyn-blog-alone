package cn.adelyn.blog.manager.dao.po;

import cn.adelyn.framework.database.pojo.po.BasePO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("blog_content")
public class BlogContentPO extends BasePO {
    @TableId
    private Long blogId;

    private String blogContent;
}
