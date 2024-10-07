package cn.adelyn.blog.transFile.pojo.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransFileInfoVO {
    private Long fileId;
    private String name;
    private String url;
}
