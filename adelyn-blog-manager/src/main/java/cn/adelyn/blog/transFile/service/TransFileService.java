package cn.adelyn.blog.transFile.service;

import cn.adelyn.blog.manager.service.SnowflakeService;
import cn.adelyn.blog.resource.dao.po.ResourceInfoPO;
import cn.adelyn.blog.resource.service.LocalFileResourceServiceImpl;
import cn.adelyn.blog.transFile.dao.po.TransCodeFileMappingPO;
import cn.adelyn.blog.transFile.dao.po.TransFileInfoPO;
import cn.adelyn.blog.transFile.dao.service.TransCodeFileMappingDAOService;
import cn.adelyn.blog.transFile.dao.service.TransCodeInfoDAOService;
import cn.adelyn.blog.transFile.dao.service.TransFileInfoDAOService;
import cn.adelyn.blog.transFile.pojo.vo.TransFileInfoVO;
import cn.adelyn.framework.core.execption.AdelynException;
import cn.adelyn.framework.core.response.ResponseEnum;
import cn.adelyn.framework.core.util.RandomIdUtil;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransFileService {

    private final TransFileInfoDAOService transFileInfoDAOService;
    private final TransCodeFileMappingDAOService transCodeFileMappingDAOService;
    private final TransCodeInfoDAOService transCodeInfoDAOService;
    private final LocalFileResourceServiceImpl localFileResourceService;
    private final SnowflakeService snowflakeService;


    public Long generateTransCode() {
        Long transCode = Long.parseLong(RandomIdUtil.getRandomIntString().substring(0, 8));
        log.info("generate trans code: {}", transCode);
        transCodeInfoDAOService.insertTransCodeInfo(transCode);
        return transCode;
    }

    public void uploadFile(Long transCode, InputStream fileInputStream, String fileOriName) {

        Long resourceId = localFileResourceService.addResource(fileOriName, fileInputStream);
        Long fileId = snowflakeService.nextId();

        transFileInfoDAOService.addTransFileInfo(fileId, resourceId);
        transCodeFileMappingDAOService.addCodeFileMapping(transCode, fileId);
    }


    public Pair<String, File> getFile(Long transCode, Long fileId) {
        if (!checkDownloadAuth(transCode, fileId)) {
            throw new AdelynException("not have auth");
        }

        Long resourceId = transFileInfoDAOService.getById(fileId).getResourceId();
        File file = localFileResourceService.getResourceFile(resourceId);
        ResourceInfoPO resourceInfoPO = localFileResourceService.getResourceBaseInfo(resourceId);
        String oriName = resourceInfoPO.getResourceName();
        return Pair.of(oriName, file);
    }

    public List<TransFileInfoVO> getTransFileList(Long transCode) {

        List<Long> fileIdList = transCodeFileMappingDAOService.getTransCodeFileMappingInfoList(transCode)
                .stream().map(TransCodeFileMappingPO::getFileId).toList();

        if (fileIdList.isEmpty()) {
            return new ArrayList<>();
        }

        List<TransFileInfoPO> transFileInfoPOList = transFileInfoDAOService.getTransFileInfoListByFileIdList(fileIdList);

        Map<Long, Long> fileIdToResourceIdMap = transFileInfoPOList.stream()
                .collect(Collectors.toMap(TransFileInfoPO::getFileId, TransFileInfoPO::getResourceId));

        List<Long> resourceIdList = transFileInfoPOList.stream().map(TransFileInfoPO::getResourceId).toList();

        Map<Long, String> resourceIdToResourceNameMap = localFileResourceService.getResourceBaseInfoListByIdList(resourceIdList).stream()
                        .collect(Collectors.toMap(ResourceInfoPO::getResourceId, ResourceInfoPO::getResourceName));


        List<TransFileInfoVO> transFileInfoVOList = new ArrayList<>();

        fileIdList.forEach(fileId -> {
            Long resourceId = fileIdToResourceIdMap.get(fileId);
            String fileOriName = resourceIdToResourceNameMap.get(resourceId);
            String downloadUrl = "https://blog.adelyn.cn/blog-backend/transFile/public/download?transCode=" + transCode + "&fileId=" + fileId;

            TransFileInfoVO transFileInfoVO = TransFileInfoVO.builder()
                    .fileId(fileId)
                    .name(fileOriName)
                    .url(downloadUrl)
                    .build();
            transFileInfoVOList.add(transFileInfoVO);
        });

        return transFileInfoVOList;
    }

    public void deleteTransFile(Long transCode, Long fileId) {
        List<TransCodeFileMappingPO> transCodeFileMappingPOList = transCodeFileMappingDAOService.getTransCodeFileMappingInfoList(transCode);

        List<Long> fileIdList = transCodeFileMappingPOList.stream()
                .map(TransCodeFileMappingPO::getFileId)
                .toList();

        if (fileIdList.isEmpty() || !fileIdList.contains(fileId)) {
            throw new AdelynException("file not exist, file id:" + fileId);
        }

        TransFileInfoPO transFileInfoPO = transFileInfoDAOService.getById(fileId);
        Long resourceId = transFileInfoPO.getResourceId();

        Map<Long, Long> fileIdToMappingIdMap = transCodeFileMappingPOList.stream()
                .collect(Collectors.toMap(TransCodeFileMappingPO::getFileId, TransCodeFileMappingPO::getId));

        Long mappingId = fileIdToMappingIdMap.get(fileId);

        transFileInfoDAOService.deleteTransFileInfoByIdList(List.of(fileId));
        transCodeFileMappingDAOService.removeById(mappingId);
        localFileResourceService.deleteResource(List.of(resourceId));

        log.info("delete trans file success, transCode: {}, fileId: {}", transCode, fileId);
    }

    public void deleteTransFile(Long transCode) {
        List<TransCodeFileMappingPO> transCodeFileMappingPOList = transCodeFileMappingDAOService.getTransCodeFileMappingInfoList(transCode);

        List<Long> fileIdList = transCodeFileMappingPOList.stream()
                .map(TransCodeFileMappingPO::getFileId)
                .toList();

        if (fileIdList.isEmpty()) {
            return;
        }

        List<TransFileInfoPO> transFileInfoPOList = transFileInfoDAOService.getTransFileInfoListByFileIdList(fileIdList);
        List<Long> resourceIdList = transFileInfoPOList.stream()
                .map(TransFileInfoPO::getResourceId)
                .toList();

        List<Long> mappingIdList = transCodeFileMappingPOList.stream()
                .map(TransCodeFileMappingPO::getId)
                .toList();

        transFileInfoDAOService.deleteTransFileInfoByIdList(fileIdList);
        transCodeFileMappingDAOService.removeBatchByIds(mappingIdList);
        localFileResourceService.deleteResource(resourceIdList);

        log.info("delete trans file success, transCode: {}, totalFile: {}", transCode, fileIdList.size());
    }

    public boolean checkUploadAuth(Long transCode) {
        return transCodeInfoDAOService.checkTransCodeExist(transCode);
    }
    
    public boolean checkDownloadAuth(Long transCode, Long fileId) {
        Set<Long> fileIdSet = transCodeFileMappingDAOService.getTransCodeFileMappingInfoList(transCode)
                .stream().map(TransCodeFileMappingPO::getFileId).collect(Collectors.toSet());

        return fileIdSet.contains(fileId);
    }

    public void transFileFlowControl() {
        try(Entry entry = SphU.entry("trans_file_flow_control")) {
            return;
        } catch (BlockException e) {
            throw new AdelynException(ResponseEnum.TOO_MANY_REQUEST);
        }
    }

}
