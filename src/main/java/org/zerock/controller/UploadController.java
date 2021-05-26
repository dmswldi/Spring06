package org.zerock.controller;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.AttachFileDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@Log4j
public class UploadController {

    @GetMapping("/uploadForm")
    public void uploadForm(){
        log.info("upload form");
    }

    @PostMapping("/uploadFormAction")
    public void uploadFormPost(MultipartFile[] uploadFile, Model model){

        String uploadFolder = "/Users/eunjikim/Documents/upload";

        for(MultipartFile multipartFile : uploadFile){
            log.info("---------------");
            log.info("Upload File Name: " + multipartFile.getOriginalFilename());
            log.info("Upload File Size: " + multipartFile.getSize());
            log.info("Upload Param Name: " + multipartFile.getName());// <input> name

            File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());

            try {
                multipartFile.transferTo(saveFile);
            } catch(Exception e){
                log.error(e.getMessage());
            }
        }
    }

    /* Using Ajax */
    @GetMapping("/uploadAjax")
    public void uploadAjax(){
        log.info("upload ajax");
    }

    private String getFolder(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        String str = sdf.format(date);// 현재 시각을 String으로
        return str.replace("-", File.separator);// yyyy/MM/dd
    }

    private boolean checkImageType(File file){
        try {
            String contentType = Files.probeContentType(file.toPath());// using type detectors
            if(contentType != null){
                return contentType.startsWith("image");
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }

    /*
    @PostMapping("/uploadAjaxAction")
    public void uploadAjaxPost(MultipartFile[] uploadFile){
        log.info("update ajax post.........");

        String uploadFolder = "/Users/eunjikim/Documents/upload";

        File uploadPath = new File(uploadFolder, getFolder());// parent + child(yyyy/MM/dd)
        log.info("upload path: " + uploadPath);// /Users/eunjikim/Documents/upload/2021/05/25

        // make directory
        if(!uploadPath.exists()){
            uploadPath.mkdirs();// mkdir()과의 차이
        }

        for(MultipartFile multipartFile : uploadFile){
            log.info("-------------");
            log.info("Upload File Name: " + multipartFile.getOriginalFilename());
            log.info("Upload File Size: " + multipartFile.getSize());

            // 순수 파일명
            String uploadFileName = multipartFile.getOriginalFilename();

            // IE has 전체 경로 not 파일 이름
            uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);// -1
            log.info("only file name: " + uploadFileName);

            // 중복 파일명 처리
            UUID uuid = UUID.randomUUID();
            uploadFileName = uuid.toString() + "_" + uploadFileName;

            try {
                File saveFile = new File(uploadPath, uploadFileName);
                multipartFile.transferTo(saveFile);

                if(checkImageType(saveFile)){
                    FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
                    Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100); // width, height
                    thumbnail.close();
                }
            } catch(Exception e){
                log.error(e.getMessage());
            }
        }
    }
    */

    @PostMapping(value = "/uploadAjaxAction",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile){
        List<AttachFileDTO> list = new ArrayList<>();
        String uploadFolder = "/Users/eunjikim/Documents/upload";

        String uploadFolderPath = getFolder();
        File uploadPath = new File(uploadFolder, uploadFolderPath);

        if(!uploadPath.exists()){
            uploadPath.mkdirs();
        }

        for(MultipartFile multipartFile : uploadFile){
            AttachFileDTO attachDTO = new AttachFileDTO();

            String uploadFileName = multipartFile.getOriginalFilename();

            // IE는 전체경로 반환하므로 수정
            uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
            log.info("original file name: " + uploadFileName);
            attachDTO.setFileName(uploadFileName);

            UUID uuid = UUID.randomUUID();

            uploadFileName = uuid.toString() + "_" + uploadFileName;

            try {
                File saveFile = new File(uploadPath, uploadFileName);
                multipartFile.transferTo(saveFile);// 파일 서버에 저장

                attachDTO.setUuid(uuid.toString());
                attachDTO.setUploadPath(uploadFolderPath);// yyyy/MM/dd

                if(checkImageType(saveFile)){
                    attachDTO.setImage(true);// default false

                    // thumbnail 생성
                    FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
                    Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);
                    thumbnail.close();
                }

                list.add(attachDTO);

            } catch(Exception e){
                e.printStackTrace();
            }
        }

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /* 섬네일 처리 */
    @GetMapping("/display")
    @ResponseBody
    public ResponseEntity<byte[]> getFile(String fileName){
        log.info("fileName: " + fileName);

        File file = new File("/Users/eunjikim/Documents/upload/" + fileName);

        log.info("file: " + file);

        ResponseEntity<byte[]> result = null;

        try {
            HttpHeaders header = new HttpHeaders();

            header.add("Content-Type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        } catch(IOException e){
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping(value = "download",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)// for 다운로드
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent, String fileName){
        log.info("download file: " + fileName);

        Resource resource = new FileSystemResource("/Users/eunjikim/Documents/upload/" + fileName);

        // file [/Users/eunjikim/Documents/upload/tobi_mainPage.png]
        log.info("resource: " + resource);

        if(!resource.exists()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String resourceName = resource.getFilename();
        // remove UUID
        String resourceOriginalName = resourceName.substring(resourceName.indexOf("_") + 1);

        HttpHeaders headers = new HttpHeaders();

        try {
            String downloadName = null;

            if(userAgent.contains("Trident")){
                log.info("IE browser");
                downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8").replaceAll("\\+", " ");
            } else if(userAgent.contains("Edge")) {
                log.info("Edge browser");
                downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8");
                log.info("Edge name: " + downloadName);
            } else {
                log.info("Chrome browser");
                downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1");
            }

            // 다운로드 시 저장되는 이름 지정
            headers.add("Content-Disposition", "attachment; filename=" + downloadName);
            // utf-8로 인코딩 후 iso-8859-1로 디코딩
        } catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        // -> bytes로 응답
    }

    @PostMapping("/deleteFile")
    @ResponseBody
    public ResponseEntity<String> deleteFile(String fileName, String type){
        log.info("deleteFile: " + fileName);

        File file;
        try {
            file = new File("/Users/eunjikim/Documents/upload/" + URLDecoder.decode(fileName, "UTF-8"));

            file.delete();

            if (type.equals("image")) {
                String largeFileName = file.getAbsolutePath().replace("s_", "");

                log.info("largeFileName: " + largeFileName);

                file = new File(largeFileName);
                file.delete();
            }
        } catch(UnsupportedEncodingException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("deleted", HttpStatus.OK);
    }

}
