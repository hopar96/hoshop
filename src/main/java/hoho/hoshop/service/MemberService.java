package hoho.hoshop.service;

import hoho.hoshop.domain.Member;
import hoho.hoshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

   private final MemberRepository memberRepository;

    /*회원가입*/
    @Transactional //readonly = false를 위해 초기화
    public Member join(Member member) {
        validateMember(member);

        return memberRepository.save(member);
    }
    //로그인 요청 검증을 위한 User 객체
   @Override
   public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
       Member member = memberRepository.findById(id);

       if (member == null) {
           throw new UsernameNotFoundException(id);
       }

       return User.builder()
               .username(member.getId())
               .password(member.getPassword())
               .roles(member.getRole().toString())
               .build();
   }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    public Member findOne(String memberId) {
        return memberRepository.findById(memberId);
    }

    private void validateMember(Member member) {
        Member member1 = memberRepository.findById(member.getId());
        if (member1 != null) {
            throw new IllegalStateException("이미 존재하는 ID입니다.");
        }
    }
    private void validateId(Member member) {
        if (member.getId() == null) {
            throw new IllegalStateException("ID를 입력하세요.");
        }
    }
    private void validatePassword(Member member) {
        if (member.getPassword() == null) {
            throw new IllegalStateException("패스워드를 입력하세요.");
        }
    }
    private void validateEmail(Member member) {
        if (member.getEmail() == null) {
            throw new IllegalStateException("이메일을 입력하세요.");
        }
    }
    private void validateName(Member member) {
        if (member.getName() == null) {
            throw new IllegalStateException("이름을 입력하세요.");
        }
    }



}
