package ms.me.meetingroom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ms.me.meetingroom.entity.Member;
import ms.me.meetingroom.service.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/{name}")
    public Member getMember(@PathVariable String name){
        final Optional<Member> optionalMember = memberService.findByName(name);
        return optionalMember.orElseThrow(RuntimeException::new);
    }

}
