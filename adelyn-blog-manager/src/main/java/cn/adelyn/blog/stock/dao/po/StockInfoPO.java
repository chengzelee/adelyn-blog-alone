package cn.adelyn.blog.stock.dao.po;

import cn.adelyn.framework.database.pojo.po.BasePO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("stock_info")
public class StockInfoPO extends BasePO {
    @TableId
    private Long stockId;
    private String marketType;
    private String stockCode;
    private Boolean focus;
}
