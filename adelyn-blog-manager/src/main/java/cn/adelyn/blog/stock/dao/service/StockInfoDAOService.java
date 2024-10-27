package cn.adelyn.blog.stock.dao.service;

import cn.adelyn.blog.manager.dao.po.BlogInfoPO;
import cn.adelyn.blog.manager.pojo.bo.GetBlogPageBO;
import cn.adelyn.blog.stock.dao.po.StockInfoPO;
import cn.adelyn.framework.database.pojo.dto.PageDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface StockInfoDAOService extends IService<StockInfoPO> {

    Page<StockInfoPO> getStockCodePage(PageDTO pageDTO);
}
