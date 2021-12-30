package hoho.hoshop.controller;

import hoho.hoshop.domain.Address;
import hoho.hoshop.domain.Member;
import hoho.hoshop.repository.MemberRepository;
import hoho.hoshop.service.MemberService;
import hoho.hoshop.web.MemberForm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "member/createMemberForm";
    }

    @PostMapping(value = "/new")
    public String create(@Valid MemberForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "member/createMemberForm";
        }
        try {
            Member member = Member.createMember(form, passwordEncoder);
            memberService.join(member);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/createMemberForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String memberLogin() {
        return "member/loginForm";
    }

    @GetMapping("/login/fail")
    public String memberLoginFail(Model model) {
        model.addAttribute("loginFailMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "member/loginForm";
    }
}
