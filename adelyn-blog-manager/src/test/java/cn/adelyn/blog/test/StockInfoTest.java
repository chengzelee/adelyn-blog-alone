package cn.adelyn.blog.test;

import cn.adelyn.framework.core.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.util.*;

@Slf4j
public class StockInfoTest {

    @Test
    void getKLine() {
        HttpUtil httpUtil = HttpUtil.builder().build();

        String url = "https://money.finance.sina.com.cn/quotes_service/api/json_v2.php/CN_MarketData.getKLineData?symbol=sh688012&scale=240&ma=5,10,20,30&datalen=120";

        ArrayList lineData = httpUtil.exchange(url, HttpMethod.GET, ArrayList.class);
        log.info("resStr: {}", lineData);
    }
}
