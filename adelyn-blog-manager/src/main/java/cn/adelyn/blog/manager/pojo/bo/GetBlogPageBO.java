package cn.adelyn.blog.manager.pojo.bo;

import cn.adelyn.framework.database.pojo.dto.PageDTO;
import lombok.Data;

@Data
public class GetBlogPageBO {

    private String blogVisible;

    private Long userId;

    private Long blogTagId;

    private PageDTO pageDTO;
}
