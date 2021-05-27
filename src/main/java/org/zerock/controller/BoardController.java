package org.zerock.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageDTO;
import org.zerock.service.BoardService;

@Controller
@RequestMapping("/board/*")
@AllArgsConstructor
@Log4j
public class BoardController {
    private BoardService service;

    @GetMapping("/list")
    public void list(Criteria cri, Model model){
        log.info("list: " + cri);
        model.addAttribute("list", service.getList(cri));
        model.addAttribute("pageMaker", new PageDTO(cri, service.getTotal(cri)));
    }

    @GetMapping("/register")
    public void register(){
        // forwarding to: /views/board/register.jsp
    }

    @PostMapping("/register")
    public String register(BoardVO board, RedirectAttributes rttr){
        log.info("register: " + board);

        if(board.getAttachList() != null){// 첨부파일 존재 시
            board.getAttachList().forEach(attach -> log.info(attach));
        }

        service.register(board);
        rttr.addFlashAttribute("result", board.getBno());
        return "redirect:/board/list";
    }

    @GetMapping({"/get", "/modify"})
    public void get(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri, Model model){
        log.info("/get or modify");
        model.addAttribute("board", service.get(bno));
    }

    @PostMapping("/modify")
    public String modify(BoardVO board, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr){
        log.info("modify: " + board);

        if(service.modify(board)){
            rttr.addFlashAttribute("result", "success");
        }
        /*
        rttr.addAttribute("pageNum", cri.getPageNum());
        rttr.addAttribute("amount", cri.getAmount());
        rttr.addAttribute("type", cri.getType());
        rttr.addAttribute("keyword", cri.getKeyword());
        */
        // addAttribute(): map에 추가, url에 값 붙음, RequestParam로 값 전달
        // addFlashAttribute(): flash attribute 추가, 일회성 -> 새로고침 시 데이터 소멸, 2개 이상 써도 소멸

        // return "redirect:/board/list";
        return "redirect:/board/list" + cri.getListLink();
    }

    @PostMapping("/remove")
    public String remove(@RequestParam("bno") Long bno,
                         @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr){
        log.info("remove... " + bno);

        if(service.remove(bno)){
            rttr.addFlashAttribute("result", "success");
        }
        /*
        rttr.addAttribute("pageNum", cri.getPageNum());
        rttr.addAttribute("amount", cri.getAmount());
        rttr.addAttribute("type", cri.getType());
        rttr.addAttribute("keyword", cri.getKeyword());

        return "redirect:/board/list";
        */
        return "redirect:/board/list" + cri.getListLink();
    }
}
