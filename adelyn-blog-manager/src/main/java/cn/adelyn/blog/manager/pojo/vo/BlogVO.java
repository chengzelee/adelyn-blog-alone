package cn.adelyn.blog.manager.pojo.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class BlogVO {

    private Long blogId;

    private String blogTitle;

    private String blogContent;

    private String blogVisible;

    private List<TagInfo> blogTagInfoList;

    @Data
    @Builder
    public static class TagInfo {
        private Long tagId;
        private String tagName;
    }
}
