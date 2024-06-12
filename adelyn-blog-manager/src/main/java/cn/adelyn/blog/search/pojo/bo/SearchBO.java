package cn.adelyn.blog.search.pojo.bo;

import lombok.Data;

import java.util.List;

@Data
public class SearchBO<T> {

    private List<T> list;

    private long total;
}
