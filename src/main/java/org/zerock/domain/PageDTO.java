package org.zerock.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PageDTO {
    private int startPage;
    private int endPage;
    private boolean prev;
    private boolean next;

    private int total;
    private Criteria cri;

    public PageDTO(Criteria cri, int total){
        this.cri = cri;
        this.total = total;

        this.endPage = (int) (Math.ceil(cri.getPageNum() / (cri.getAmount() * 1.0))) * cri.getAmount();
        this.startPage = this.endPage - cri.getAmount() + 1;

        int realEnd = (int) (Math.ceil( (total * 1.0) / cri.getAmount() ));
        this.endPage = Math.min(this.endPage, realEnd);

        this.prev = this.startPage > 1;
        this.next = this.endPage < realEnd;
    }
}
