package cn.adelyn.blog.search.pojo.vo;

import lombok.Data;

@Data
public class SearchBlogVO {

    private String blogId;

    private String userId;

    private String blogTitle;

    private String blogContent;

    private String visible;
}
