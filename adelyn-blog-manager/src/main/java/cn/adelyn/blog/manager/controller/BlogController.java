package cn.adelyn.blog.manager.controller;

import cn.adelyn.blog.manager.constant.BlogConstant;
import cn.adelyn.blog.manager.pojo.bo.InsertBlogBO;
import cn.adelyn.blog.manager.pojo.bo.UpdateBlogBO;
import cn.adelyn.blog.manager.pojo.dto.GetBlogPageDTO;
import cn.adelyn.blog.manager.pojo.dto.InsertBlogDTO;
import cn.adelyn.blog.manager.pojo.dto.UpdateBlogDTO;
import cn.adelyn.blog.manager.pojo.vo.BlogVO;
import cn.adelyn.blog.manager.service.BlogService;
import cn.adelyn.framework.core.cglib.BeanCopierUtil;
import cn.adelyn.framework.core.context.UserInfoContext;
import cn.adelyn.framework.core.pojo.vo.PageVO;
import cn.adelyn.framework.core.response.ServerResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/article")
public class BlogController {

    private final BlogService blogService;

    @GetMapping()
    public ServerResponse<BlogVO> getBlogInfo(@RequestParam(value = "blogId") @NotNull(message = "blogId 不能为空~") Long blogId) {
        return ServerResponse.success(blogService.getBlogVOList(Collections.singletonList(blogId)).get(0));
    }

    @PostMapping("/public/page")
    public ServerResponse<PageVO<BlogVO>> getBlogPage(@RequestBody @Valid GetBlogPageDTO getBlogPageDTO) {
        getBlogPageDTO.setBlogVisible(BlogConstant.VISIBLE_PUBLIC);
        return ServerResponse.success(blogService.getBlogVOPage(getBlogPageDTO));
    }

    @PostMapping("/user/page")
    public ServerResponse<PageVO<BlogVO>> getUserBlogPage(@RequestBody @Valid GetBlogPageDTO getBlogPageDTO) {
        getBlogPageDTO.setUserId(UserInfoContext.getUserId());
        return ServerResponse.success(blogService.getBlogVOPage(getBlogPageDTO));
    }

    @PostMapping
    public ServerResponse<Long> insertBlog(@RequestBody @Valid InsertBlogDTO insertBlogDTO) {
        InsertBlogBO insertBlogBO = BeanCopierUtil.copy(insertBlogDTO, InsertBlogBO.class);
        insertBlogBO.setUserId(UserInfoContext.getUserId());

        Long blogId = blogService.insertBlog(insertBlogBO);
        return ServerResponse.success(blogId);
    }

    @PutMapping
    public ServerResponse updateBlog(@RequestBody @Valid UpdateBlogDTO updateBlogDTO) {
        UpdateBlogBO updateBlogBO = BeanCopierUtil.copy(updateBlogDTO, UpdateBlogBO.class);
        updateBlogBO.setUserId(UserInfoContext.getUserId());

        blogService.updateBlog(updateBlogBO);
        return ServerResponse.success();
    }

    @DeleteMapping()
    public ServerResponse deleteBlog(@RequestParam(value = "blogId") @NotNull(message = "blogId 不能为空~") Long blogId) {
        blogService.deleteBlog(blogId);
        return ServerResponse.success();
    }
}
