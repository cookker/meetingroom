package ms.me.meetingroom.controller;

import lombok.RequiredArgsConstructor;
import ms.me.meetingroom.entity.Member;
import ms.me.meetingroom.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberService memberService;

    @GetMapping("/")
    public String home(){
        return "reservation";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

//    @GetMapping("/login-error")
//    public String loginError(Model model) {
//        model.addAttribute("loginError", true);
//        return "login";
//    }

    @GetMapping("/member")
    public String member(){
        return "signup";
    }

    @PostMapping("/member")
    public String create(Member member) {
        memberService.createMember(member.getName(), member.getPassword());
        return "redirect:/";
    }
}
