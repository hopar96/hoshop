package hoho.hoshop.service;

import hoho.hoshop.domain.Member;
import hoho.hoshop.repository.MemberRepository;
import hoho.hoshop.dto.MemberForm;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired private MockMvc mockMvc;
    @PersistenceContext
    EntityManager em;

    public Member createMember(String id, String password) {
        MemberForm form = new MemberForm();
        form.setId(id);
        form.setName("호준");
        form.setEmail("saqwok@asdk.dos");
        form.setPassword(password);

        return Member.createMember(form, passwordEncoder);
    }

    @Test
    @WithMockUser(username = "hojun", roles = "USER")
    public void auditingTest() throws Exception{
        //given
        Member newMember = new Member();
        newMember.setId("hohojun");
        memberRepository.save(newMember);
        //when
        em.flush();
        em.clear();

        Member member = memberRepository.findById(newMember.getId());

        System.out.println("register time : " + member.getCreatedDate());
        System.out.println("update time : " + member.getUpdateTime());
        System.out.println("create member : " + member.getCreatedBy());
        System.out.println("modifiy member : " + member.getModifiedBy());
        //then
    }

    @Test @Rollback(value = false)
    @DisplayName("로그인 성공 테스트")
    public void 로그인_성공() throws Exception{
        //given
        String id = "hojun23";
        String password = "asmdkqwm";
        //when
        Member createM = this.createMember(id, password);
        memberService.join(createM);
        //then


        mockMvc.perform(formLogin().loginProcessingUrl("/member/login")
                .userParameter("id").user(id).password(password))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }




    @Test
    public void 정보_미기입_회원가입_검증() throws Exception{
        //given
        Member member = new Member();
        member.setId("hojun");
        member.setName("호준");
        member.setEmail("saqwok@asdk.dos");

        //when
        try {
            memberService.join(member);
            fail();
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).isEqualTo("패스워드를 입력하세요.");
        }

        //then
    }
    @Test
    public void 중복회원_검증() throws Exception{
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        member1.setId("hojun");
        member2.setId("hojun");

        //when
        memberService.join(member1);
        try {
            memberService.join(member2);
            fail();
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 ID입니다.");
        }


    }
}