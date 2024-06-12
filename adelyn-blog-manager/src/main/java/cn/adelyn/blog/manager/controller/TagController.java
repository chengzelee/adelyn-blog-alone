package cn.adelyn.blog.manager.controller;

import cn.adelyn.blog.manager.pojo.dto.GetTagPageDTO;
import cn.adelyn.blog.manager.pojo.dto.InsertTagDTO;
import cn.adelyn.blog.manager.pojo.dto.UpdateTagDTO;
import cn.adelyn.blog.manager.pojo.vo.TagVO;
import cn.adelyn.blog.manager.service.TagService;
import cn.adelyn.framework.core.context.UserInfoContext;
import cn.adelyn.framework.core.pojo.vo.PageVO;
import cn.adelyn.framework.core.response.ServerResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
@AllArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    public ServerResponse<Long> insertTag(@RequestBody @Validated InsertTagDTO insertTagDTO) {
        Long tagId = tagService.insertTag(insertTagDTO.getTagName(), UserInfoContext.getUserId());
        return ServerResponse.success(tagId);
    }

    @PutMapping
    public ServerResponse updateTag(@RequestBody @Valid UpdateTagDTO updateTagDTO) {
        tagService.updateTag(updateTagDTO.getTagId(), updateTagDTO.getTagName());
        return ServerResponse.success();
    }

    @DeleteMapping
    public ServerResponse deleteTag(@RequestBody List<Long> tagIdList) {
        tagService.deleteTag(tagIdList);
        return ServerResponse.success();
    }

    @PostMapping("/page")
    public ServerResponse<PageVO<TagVO>> getTagPage(@RequestBody @Valid GetTagPageDTO tagSearchDTO) {
        return ServerResponse.success(
                tagService.getTagPage(UserInfoContext.getUserId(), tagSearchDTO.getPageDTO())
        );
    }
}
