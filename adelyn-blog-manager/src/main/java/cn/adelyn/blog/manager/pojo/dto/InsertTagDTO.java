package cn.adelyn.blog.manager.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InsertTagDTO {
    @NotBlank(message = "tagName 不能为空")
    private String tagName;
}
