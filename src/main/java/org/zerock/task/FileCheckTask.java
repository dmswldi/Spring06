package org.zerock.task;

import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zerock.domain.BoardAttachVO;
import org.zerock.mapper.BoardAttachMapper;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Log4j
@Component
public class FileCheckTask {

    @Setter(onMethod_ = @Autowired)
    private BoardAttachMapper attachMapper;

    private String getFolderYesterDay(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);

        String str = sdf.format(cal.getTime());

        return str.replace("-", File.separator);
    }

    /*
    @Scheduled(cron = "0 * * * * *")// cron으로 주기 제어, 매분 0초마다 한 번씩 실행
    public void checkFiles() throws Exception {
        log.warn("File Check Task run...............");// 실행 중에 확인
        log.warn("==================================");
    }
    */

    public void checkFiles() throws Exception {
        log.warn("File Check Task run............");
        log.warn(new Date());

        List<BoardAttachVO> fileList = attachMapper.getOldFiles();

        List<Path> fileListPaths = fileList.stream()
                .map(vo -> Paths.get("/Users/eunjikim/Documents/upload", vo.getUploadPath(), vo.getUuid() + "_" + vo.getFileName()))
                .collect(Collectors.toList());

        fileList.stream().filter(vo -> vo.isFileType() == true)
                .map(vo -> Paths.get("/Users/eunjikim/Documents/upload", vo.getUploadPath(), "s_" + vo.getUuid() + "_" + vo.getFileName()))
                .forEach(p -> fileListPaths.add(p));

        log.warn("===================");

        fileListPaths.forEach(p -> log.warn(p));

        File targetDir = Paths.get("/Users/eunjikim/Documents/upload", getFolderYesterDay()).toFile();

        File[] removeFiles = targetDir.listFiles(file -> fileListPaths.contains(file.toPath()) == false);

        log.warn("===============");
        for(File file : removeFiles){
            log.warn(file.getAbsolutePath());
            file.delete();
        }
    }
}
