package cn.adelyn.blog.manager.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class InsertBlogDTO {

    @NotBlank(message = "blogTitle 不能为空~")
    private String blogTitle;

    @NotBlank(message = "正文 不能为空~")
    private String blogContent;

    @NotBlank(message = "文章可见性 不能为空~")
    private String blogVisible;

    private List<Long> tagIdList;

    private List<Long> picIdList;
}
