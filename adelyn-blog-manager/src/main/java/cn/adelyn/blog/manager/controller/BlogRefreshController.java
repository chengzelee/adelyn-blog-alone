package cn.adelyn.blog.manager.controller;

import cn.adelyn.blog.manager.service.BlogRefreshEsService;
import cn.adelyn.framework.core.response.ServerResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/article/refresh")
public class BlogRefreshController {

    private BlogRefreshEsService blogRefreshEsService;

    @GetMapping
    public ServerResponse<String> refreshBlog() {
        log.info("start refresh");
        blogRefreshEsService.refreshEs();
        return ServerResponse.success("success");
    }
}
