package cn.adelyn.blog.stock.dao.service.impl;

import cn.adelyn.blog.manager.dao.po.BlogInfoPO;
import cn.adelyn.blog.stock.dao.mapper.StockInfoMapper;
import cn.adelyn.blog.stock.dao.po.StockInfoPO;
import cn.adelyn.blog.stock.dao.service.StockInfoDAOService;
import cn.adelyn.framework.database.pojo.dto.PageDTO;
import cn.adelyn.framework.database.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class StockInfoDAOServiceImpl extends ServiceImpl<StockInfoMapper, StockInfoPO> implements StockInfoDAOService {

    @Override
    public Page<StockInfoPO> getStockCodePage(PageDTO pageDTO) {
        LambdaQueryWrapper<StockInfoPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(StockInfoPO::getStockCode);

        return baseMapper.selectPage(PageUtil.getPage(pageDTO), wrapper);
    }
}
