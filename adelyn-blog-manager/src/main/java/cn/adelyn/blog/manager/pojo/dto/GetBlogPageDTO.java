package cn.adelyn.blog.manager.pojo.dto;

import cn.adelyn.framework.database.pojo.dto.PageDTO;
import lombok.Data;

@Data
public class GetBlogPageDTO {

    private String blogVisible;

    private Long userId;

    private PageDTO pageDTO;
}
