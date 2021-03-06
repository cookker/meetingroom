package ms.me.meetingroom.controller;

import ms.me.meetingroom.entity.Member;
import ms.me.meetingroom.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
    @Autowired
    private MemberService memberService;

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
