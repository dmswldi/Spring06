package org.zerock.domain;

import lombok.Data;

@Data
public class BoardAttachVO {
    private String uuid;// pk
    private String uploadPath;
    private String fileName;
    private boolean fileType;// 1은 이미지

    private Long bno;
}
