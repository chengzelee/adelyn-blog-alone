package cn.adelyn.blog.manager.controller;

import cn.adelyn.blog.manager.service.BlogPicService;
import cn.adelyn.framework.core.execption.AdelynException;
import cn.adelyn.framework.core.response.ResponseEnum;
import cn.adelyn.framework.core.response.ServerResponse;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/pic")
public class PicController {

    private final BlogPicService blogPicService;

    @PostMapping()
    public ServerResponse<Long> addPic(@RequestParam("pic") MultipartFile pic) {
        Long picId;
        try {
            picId = blogPicService.addPic(pic.getOriginalFilename(), pic.getInputStream());
        } catch (IOException e) {
            throw new AdelynException("upload pic err", e);
        }
        return ServerResponse.success(picId);
    }

    @GetMapping("/public/{picId}")
    public void getPic(@PathVariable(value = "picId")
                       @NotBlank(message = "picId 不能为空~") Long picId, HttpServletResponse httpServletResponse) throws IOException {
        try(Entry entry = SphU.entry("pic_get")) {
            String redirectUrl = blogPicService.getPicUrl(picId);
            httpServletResponse.sendRedirect(redirectUrl);
        } catch (BlockException e) {
            throw new AdelynException(ResponseEnum.TOO_MANY_REQUEST);
        }
    }
}
