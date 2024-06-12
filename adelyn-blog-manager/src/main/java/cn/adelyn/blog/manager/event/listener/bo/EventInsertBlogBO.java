package cn.adelyn.blog.manager.event.listener.bo;

import lombok.Data;

@Data
public class EventInsertBlogBO {
    private Long blogId;

    private Long userId;

    private String blogTitle;

    private String blogContent;

    private String blogVisible;
}
