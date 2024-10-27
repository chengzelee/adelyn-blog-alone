package cn.adelyn.blog.stock.controller;

import cn.adelyn.blog.stock.pojo.dto.GetStockKLinePageDTO;
import cn.adelyn.blog.stock.pojo.dto.SearchKLineDTO;
import cn.adelyn.blog.stock.pojo.vo.StockKLineInfoVO;
import cn.adelyn.blog.stock.service.StockService;
import cn.adelyn.framework.core.pojo.vo.PageVO;
import cn.adelyn.framework.core.response.ServerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
@Slf4j
public class StockController {

    private final StockService stockService;


    @PostMapping("/klinePage")
    public ServerResponse<PageVO<StockKLineInfoVO>> getStockKLinePage(@RequestBody GetStockKLinePageDTO getStockKLinePageDTO) {
        return ServerResponse.success(stockService.getStockKLinePage(getStockKLinePageDTO.getPageDTO()));
    }

    @PostMapping("/searchKline")
    public ServerResponse<List<StockKLineInfoVO>> searchKline(@RequestBody SearchKLineDTO searchKLineDTO) {
        List<String> stockCodeList = Arrays.stream(searchKLineDTO.getStockCodeList().split(",")).toList();
        return ServerResponse.success(stockService.getStockKLineInfoVOList(stockCodeList, "120", "5,10,20,30"));
    }
}
