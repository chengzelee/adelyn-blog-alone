package cn.adelyn.blog.search.controller;

import cn.adelyn.blog.search.pojo.dto.SearchBlogDTO;
import cn.adelyn.blog.search.service.BlogSearchService;
import cn.adelyn.framework.core.context.UserInfoContext;
import cn.adelyn.framework.core.pojo.vo.PageVO;
import cn.adelyn.framework.core.response.ServerResponse;
import cn.adelyn.framework.core.util.BasePageUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/search")
public class BlogSearchController {

    private final BlogSearchService blogSearchService;

    @GetMapping("/public/blogContent")
    public ServerResponse search(@RequestParam("blogId") @NotBlank(message = "blogId 不能为空~") String blogId) {
        return ServerResponse.success(blogSearchService.getBlogById(blogId, null));
    }

    @GetMapping("/blogContent/user")
    public ServerResponse searchUser(@RequestParam("blogId") @NotBlank(message = "blogId 不能为空~") String blogId) {
        Long userId = UserInfoContext.getUserId();
        Assert.notNull(userId, "please login");
        return ServerResponse.success(blogSearchService.getBlogById(blogId, userId));
    }

    @PostMapping("/public/match")
    public ServerResponse<PageVO> match(@RequestBody @Valid SearchBlogDTO searchBlogDTO) {
        int startIndex = BasePageUtil.getStart(searchBlogDTO.getFrom(), searchBlogDTO.getSize());
        searchBlogDTO.setFrom(startIndex);
        return ServerResponse.success(blogSearchService.matchPublic(searchBlogDTO));
    }

    @PostMapping("/match/user")
    public ServerResponse<PageVO> matchUser(@RequestBody @Valid SearchBlogDTO searchBlogDTO) {
        int startIndex = BasePageUtil.getStart(searchBlogDTO.getFrom(), searchBlogDTO.getSize());
        searchBlogDTO.setFrom(startIndex);
        return ServerResponse.success(blogSearchService.matchUser(searchBlogDTO, UserInfoContext.getUserId()));
    }
}
