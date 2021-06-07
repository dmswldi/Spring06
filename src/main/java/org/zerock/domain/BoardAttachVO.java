package org.zerock.domain;

import lombok.Data;

@Data
public class BoardAttachVO {
    private String uuid;// pk
    private String uploadPath;
    private String fileName;
    private boolean fileType;// db: 1은 이미지, 0은 일반파일

    private Long bno;
}
