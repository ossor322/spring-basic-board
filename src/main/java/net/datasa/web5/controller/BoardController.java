package net.datasa.web5.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.web5.domain.dto.BoardDTO;
import net.datasa.web5.security.AuthenticatedUser;
import net.datasa.web5.service.BoardService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("board")
@Controller
public class BoardController {
    //메인화면에서 "board/list"경로를 클릭했을 때 처리하는 메소드
    //templates/boardView/list.html 파일로 포워딩
    //로그인 안한 상태에서 해당 페이지의 "게시판"이라는 제목이 보여야 함

    private final BoardService boardService;

    @GetMapping("list")
    public String list(Model model) {
        // 서비스에서 전체 글목록 전달받음
        // 글목록을 모델에 저장하고 HTML로 포워딩
        model.addAttribute("boardList", boardService.getList());
        return "boardView/list";
    }

    @GetMapping("write")
    public String write() {
        return "boardView/write";
    }

    @PostMapping("write")
    public String write(@AuthenticationPrincipal AuthenticatedUser user
            , @ModelAttribute BoardDTO boardDTO) {
        boardService.write(user.getUsername(), boardDTO);
        return "redirect:/board/list";
    }

    @GetMapping("/{boardNum}")
    public String info(@PathVariable("boardNum") Integer boardNum, Model model) {
        BoardDTO boardDTO = boardService.findByBoardNum(boardNum);
        model.addAttribute("boardList", List.of(boardDTO));
        return "boardView/list";
    }
}
