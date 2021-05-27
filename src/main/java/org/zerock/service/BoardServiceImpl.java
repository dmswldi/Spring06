package org.zerock.service;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.mapper.BoardAttachMapper;
import org.zerock.mapper.BoardMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService {

    // 스프링 4.3 이상에서 자동 주입 -> 주입 대상 2개 이상 시 @Setter 설정 ...???
    @Setter(onMethod_ = @Autowired)
    private BoardMapper mapper;

    @Setter(onMethod_ = @Autowired)
    private BoardAttachMapper attachMapper;

    @Override
    public int getTotal(Criteria cri) {
        return mapper.getTotalCount(cri);
    }

    @Override
    public void register(BoardVO board) {
      mapper.insertSelectKey(board);
    }

    @Override
    public BoardVO get(Long bno) {
        return mapper.read(bno);
    }

    @Override
    public boolean modify(BoardVO board) {
        return mapper.update(board) == 1;
    }

    @Override
    public boolean remove(Long bno) {
        return mapper.delete(bno) == 1;
    }

    /*
    @Override
    public List<BoardVO> getList() {
        return mapper.getList();
    }
    */

    @Override
    public List<BoardVO> getList(Criteria cri){
        return mapper.getListWithPaging(cri);
    }
}
