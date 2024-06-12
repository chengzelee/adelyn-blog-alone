package cn.adelyn.blog.resource.controller;

import cn.adelyn.blog.resource.service.ResourceService;
import cn.adelyn.framework.core.execption.AdelynException;
import cn.adelyn.framework.core.response.ServerResponse;
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
@RequestMapping("/resource")
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping("/pic")
    public ServerResponse<Long> addPic(@RequestParam("pic") MultipartFile pic) {
        Long picId;
        try {
            picId = resourceService.addPic(pic.getOriginalFilename(), pic.getInputStream());
        } catch (IOException e) {
            throw new AdelynException("upload pic err", e);
        }
        return ServerResponse.success(picId);
    }

    @GetMapping("/public/pic/{picId}")
    public void getPic(@PathVariable(value = "picId")
                       @NotBlank(message = "picId 不能为空~") Long picId, HttpServletResponse httpServletResponse) throws IOException {
        String redirectUrl = resourceService.getPicUrl(picId);
        httpServletResponse.sendRedirect(redirectUrl);
    }
}
