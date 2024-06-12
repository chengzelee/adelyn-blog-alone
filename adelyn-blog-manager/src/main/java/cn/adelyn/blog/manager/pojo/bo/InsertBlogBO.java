package cn.adelyn.blog.manager.pojo.bo;

import lombok.Data;

import java.util.List;

@Data
public class InsertBlogBO {
    private Long blogId;

    private Long userId;

    private String blogTitle;

    private String blogContent;

    private String blogVisible;

    private List<Long> tagIdList;

    private List<Long> picIdList;
}
