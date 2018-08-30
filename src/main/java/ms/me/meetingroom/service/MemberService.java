package ms.me.meetingroom.service;

import ms.me.meetingroom.entity.Member;
import ms.me.meetingroom.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Member> findByName(String name) {
        return memberRepository.findByName(name);
    }

    public Member createMember(String name, String password){
        Member member = new Member();
        member.setName(name);
        member.setPassword(passwordEncoder.encode(password));
        return memberRepository.save(member);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<Member> byName = memberRepository.findByName(username);
        if (!byName.isPresent()) {
            throw new UsernameNotFoundException("User not found by name: " + username);
        }
        final Member member = byName.get();
        return new User(member.getName(), member.getPassword(), authorities());
    }

    private Collection<? extends GrantedAuthority> authorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
