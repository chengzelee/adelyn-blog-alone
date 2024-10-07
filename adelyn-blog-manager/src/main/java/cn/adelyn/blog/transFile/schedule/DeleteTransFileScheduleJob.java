package cn.adelyn.blog.transFile.schedule;

import cn.adelyn.blog.transFile.dao.po.TransCodeInfoPO;
import cn.adelyn.blog.transFile.dao.service.TransCodeInfoDAOService;
import cn.adelyn.blog.transFile.service.TransFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteTransFileScheduleJob {

    private final TransFileService transFileService;
    private final TransCodeInfoDAOService transCodeInfoDAOService;

    @Scheduled(cron = "0 0 1 * * ? ")
    public void deleteFile() {
        List<TransCodeInfoPO> transCodeInfoPOList = transCodeInfoDAOService.list();
        LocalDateTime currentDate = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();

        List<Long> needDeleteTransCode = transCodeInfoPOList.stream()
                .filter(po -> {
                    LocalDateTime createTime = po.getCreateTime().toInstant().atZone(zoneId).toLocalDateTime();
                    return createTime.plusDays(7).isBefore(currentDate);
                })
                .map(TransCodeInfoPO::getTransCode)
                .toList();

        log.info("trans code {} will be delete", needDeleteTransCode);

        needDeleteTransCode.forEach(transCode -> {
            transFileService.deleteTransFile(transCode);
            log.info("trans code {} delete success", transCode);
        });

    }
}
