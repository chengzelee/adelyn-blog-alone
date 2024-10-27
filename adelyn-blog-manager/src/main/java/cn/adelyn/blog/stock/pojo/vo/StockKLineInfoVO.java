package cn.adelyn.blog.stock.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class StockKLineInfoVO {
    private String stockCode;
    private List lineData;
}
