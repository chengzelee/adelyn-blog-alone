package cn.adelyn.blog.resource.pojo.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddResourceInfoBO {
    private Long resourceId;
    private String resourceName;
    private String absolutePath;
}
