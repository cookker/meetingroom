package ms.me.meetingroom.controller;

import ms.me.meetingroom.entity.Member;
import ms.me.meetingroom.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class MemberController {
    @Autowired
    private  MemberService memberService;

    @GetMapping("/members/{name}")
    public Member getMember(@PathVariable String name){
        final Optional<Member> optionalMember = memberService.findByName(name);
        return optionalMember.orElseThrow(RuntimeException::new);
    }

}
