package org.zerock.domain;

import lombok.Data;

@Data
public class AttachFileDTO {// 브라우저로 전송해야 하는 데이터
    private String fileName;
    private String uploadPath;// 폴더 경로, yyyy/MM/dd
    private String uuid;
    private boolean image;
}
