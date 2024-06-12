package cn.adelyn.blog.manager.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTagDTO {
    @NotNull(message = "tagId 不能为空")
    private Long tagId;

    @NotBlank(message = "tagName 不能为空")
    private String tagName;
}
