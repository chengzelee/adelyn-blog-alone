package cn.adelyn.blog.search.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SearchBlogDTO {

    @NotBlank(message = "查询内容 不能为空~")
    private String queryStr;

    /**
     * 页码
     */
    @NotNull(message = "当前页码 不能为空~")
    private Integer from;

    /**
     * 每页条数
     */
    @NotNull(message = "单页条数 不能为空~")
    private Integer size;
}
