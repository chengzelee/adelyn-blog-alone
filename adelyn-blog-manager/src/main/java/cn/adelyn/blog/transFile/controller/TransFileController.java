package cn.adelyn.blog.transFile.controller;

import cn.adelyn.blog.transFile.pojo.vo.TransFileInfoVO;
import cn.adelyn.blog.transFile.service.TransFileService;
import cn.adelyn.framework.core.execption.AdelynException;
import cn.adelyn.framework.core.response.ResponseEnum;
import cn.adelyn.framework.core.response.ServerResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/transFile")
@RequiredArgsConstructor
public class TransFileController {

    private final TransFileService transFileService;

    @GetMapping("/transCode")
    public ServerResponse<Long> getTransCode() {
        return ServerResponse.success(transFileService.generateTransCode());
    }

    @GetMapping("/public/list")
    public ServerResponse<List<TransFileInfoVO>> transFile(@RequestParam("transCode") Long transCode) {
        transFileService.transFileFlowControl();

        return ServerResponse.success(transFileService.getTransFileList(transCode));
    }

    @PostMapping("/public/upload")
    public ServerResponse<String> uploadFile(@RequestParam("transCode") Long transCode, @RequestParam("file") MultipartFile file) {
        transFileService.transFileFlowControl();

        if (!transFileService.checkUploadAuth(transCode)) {
            return ServerResponse.fail("not have auth");
        }

        if (file.isEmpty()) {
            return ServerResponse.fail("empty file");
        }

        try (InputStream inputStream = file.getInputStream()) {
            transFileService.uploadFile(transCode, inputStream, file.getOriginalFilename());
            log.info("upload file success, size: {}", file.getSize());
        } catch (IOException e) {
            throw new AdelynException("upload file fail", e);
        }

        return ServerResponse.success();
    }

    @GetMapping("/public/download")
    public void downloadFile(HttpServletResponse httpServletResponse,
                                                           @RequestParam("transCode") Long transCode, @RequestParam("fileId") Long fileId) {
        transFileService.transFileFlowControl();
        log.info("cdn miss, start download, transCode: {}, fileId: {}", transCode, fileId);

        Pair<String, File> resPair = transFileService.getFile(transCode, fileId);

        String encodedFileName = URLEncoder.encode(resPair.getFirst(), StandardCharsets.UTF_8);
        File file = resPair.getSecond();

        httpServletResponse.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
        httpServletResponse.setContentType("application/octet-stream");
        httpServletResponse.setContentLengthLong(file.length());

        // 使用 InputStream 将文件数据写入到响应中
        try (InputStream inputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                httpServletResponse.getOutputStream().write(buffer, 0, bytesRead);
            }
            httpServletResponse.flushBuffer();  // 强制刷新缓冲区
        } catch (IOException e) {
            throw new AdelynException(ResponseEnum.FAIL);
        }
    }

    @PostMapping("/public/delete")
    public ServerResponse<Void> deleteTransFile(@RequestParam("transCode") Long transCode, @RequestParam("fileId") Long fileId) {
        transFileService.transFileFlowControl();

        transFileService.deleteTransFile(transCode, fileId);
        return ServerResponse.success();
    }
}
