package cn.adelyn.blog.stock.service;

import cn.adelyn.blog.manager.pojo.vo.BlogVO;
import cn.adelyn.blog.stock.dao.po.StockInfoPO;
import cn.adelyn.blog.stock.dao.service.StockInfoDAOService;
import cn.adelyn.blog.stock.pojo.vo.StockKLineInfoVO;
import cn.adelyn.framework.core.pojo.vo.PageVO;
import cn.adelyn.framework.core.util.HttpUtil;
import cn.adelyn.framework.database.pojo.dto.PageDTO;
import cn.adelyn.framework.database.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockService {

    private final StockInfoDAOService stockInfoDAOService;

    private static final HttpUtil httpUtil = HttpUtil.builder().build();

    public PageVO<StockKLineInfoVO> getStockKLinePage(PageDTO pageDTO) {
        Page<StockInfoPO> stockInfoPOPage = stockInfoDAOService.getStockCodePage(pageDTO);

        List<String> stockCodeList = stockInfoPOPage.getRecords().stream().map(StockInfoPO::getStockCode).toList();

        List<StockKLineInfoVO> stockKLineInfoVOList = getStockKLineInfoVOList(stockCodeList, "120", "5,10,20,30");

        PageVO<StockKLineInfoVO> pageVO = PageUtil.getPageVO(stockInfoPOPage, StockKLineInfoVO.class);
        pageVO.setList(stockKLineInfoVOList);

        return pageVO;
    }

    public List<StockKLineInfoVO> getStockKLineInfoVOList(List<String> stockCodeList, String dataLength, String maLines) {
        List<StockKLineInfoVO> resList = new ArrayList<>();

        stockCodeList.stream().distinct().forEach(stockCode -> {
            resList.add(getStockKLinkData(stockCode, dataLength, maLines));
        });

        return resList;
    }

    private StockKLineInfoVO getStockKLinkData(String stockCode, String dataLength, String maLines) {
        String url = "https://money.finance.sina.com.cn/quotes_service/api/json_v2.php/CN_MarketData.getKLineData"
                + "?symbol=" + stockCode
                + "&scale=240"
                + "&ma=" + maLines
                + "&datalen=" + dataLength;

        List lineData = httpUtil.exchange(url, HttpMethod.GET, ArrayList.class);

        StockKLineInfoVO stockKLineInfoVO = new StockKLineInfoVO();
        stockKLineInfoVO.setLineData(lineData);
        stockKLineInfoVO.setStockCode(stockCode);

        return stockKLineInfoVO;
    }
}
