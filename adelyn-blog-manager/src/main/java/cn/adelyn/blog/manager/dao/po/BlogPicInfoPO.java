package cn.adelyn.blog.manager.dao.po;

import cn.adelyn.framework.database.pojo.po.BasePO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("blog_pic_info")
public class BlogPicInfoPO extends BasePO {

    @TableId
    private Long picId;

    private Long resourceId;
}
