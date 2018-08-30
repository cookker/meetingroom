package ms.me.meetingroom.repository;

import ms.me.meetingroom.entity.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void basic_crud_test(){
        Member member = new Member();
        member.setName("users1");
        member.setPassword("12345");
        memberRepository.save(member);
        final List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(1);

        member.setName("update user1");
        memberRepository.flush();
        assertThat(member.getUpdatedAt()).isNotNull();
        assertThat(member.getCreatedAt()).isNotEqualTo(member.getUpdatedAt());
    }

    @Test
    public void 이름으로회원조회(){
        Member m = new Member();
        m.setName("홍길동");
        m.setPassword("1234");
        memberRepository.save(m);
        final Optional<Member> member = memberRepository.findByName("홍길동");
        assertThat(member).isNotEmpty();
    }
}