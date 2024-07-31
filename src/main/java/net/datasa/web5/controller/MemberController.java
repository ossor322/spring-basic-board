package net.datasa.web5.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.web5.domain.dto.MemberDTO;
import net.datasa.web5.security.AuthenticatedUser;
import net.datasa.web5.service.MemberService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 회원정보관련 콘트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("member")
@Controller
public class MemberController {
    private final MemberService memberService;

    @GetMapping("joinForm")
    public String joinForm() {
        return "memberView/joinForm";
    }

    @PostMapping("join")
    public String join(@ModelAttribute MemberDTO member) {
        log.debug("전달된 회원정보: {}", member);

        memberService.join(member);
        return "memberView/joinForm";
    }

    /**
     * 회원가입페이지에서 "ID중복확인" 버튼을 클릭하면 새창으로 보여줄 검색 페이지로 이동
     *
     * @return ID검색 HTML파일 경로
     */
    @GetMapping("idCheck")
    public String idCheck() {
        return "memberView/idCheck";
    }

    /**
     * ID중복확인 페이지에서 검색 요청했을때 처리
     *
     * @param searchId 검색할 아이디
     * @return ID검색 HTML파일 경로
     */
    @PostMapping("idCheck")
    public String idCheck(
            @RequestParam("searchId") String searchId,
            Model model) {
        //검색할 아이디를 서비스로 보내서 사용 가능한지 조회 (true이면 사용가능)
        boolean result = memberService.isAvailableId(searchId);
        //검색한 아이디와 결과를 모델에 저장
        model.addAttribute("searchId", searchId);
        model.addAttribute("result", result);
        //ID검색 페이지로 포워딩
        return "memberView/idCheck";
    }

    /**
     * 로그인 페이지로 이동
     *
     * @return
     */
    @GetMapping("loginForm")
    public String loginForm() {
        return "memberView/loginForm";
    }

    @GetMapping("info")
    public String info(@AuthenticationPrincipal AuthenticatedUser user
            , Model model) {
        // 현재 사용자의 아이디를 서비스로 전달하여 해당 사용자 정보를 MemberDTO 객체로 리턴 받는다.
        // MemberDTO객체를 모델에 저장하고 HTML 폼으로 이동
        log.debug("user : {}", user);

        MemberDTO memberDTO = memberService.getMember(user.getUsername());
        model.addAttribute("member", memberDTO);
        return "memberView/infoForm";
    }

    @PostMapping("info")
    public String info(@AuthenticationPrincipal AuthenticatedUser user,
                       @ModelAttribute MemberDTO memberDTO) {
        // 수정폼에서 전달한 값들을 MemberDTO로 받는다
        log.debug("user : {}", user);

        // 현재 로그인한 사용자의 아이디를 MemberDTO객체에 추가한다.
        memberDTO.setMemberId(user.getUsername());
        log.debug("memberDTO : {}", memberDTO);

        // MemberDTO 객체를 서비스로 전달하여 DB를 수정한다
        memberService.update(memberDTO);

        // 메인화면으로 리다이렉트 한다.
        return "redirect:/";
    }
}