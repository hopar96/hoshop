package hoho.hoshop.domain;

import hoho.hoshop.repository.CartRepository;
import hoho.hoshop.repository.MemberRepository;
import hoho.hoshop.dto.MemberForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CartTest {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    public Member createMember() {
        MemberForm memberForm = new MemberForm();
        memberForm.setEmail("ask@asd.cs");
        memberForm.setId("hojun22");
        memberForm.setName("hojun");
        memberForm.setPassword("asd");
        return Member.createMember(memberForm, passwordEncoder);
    }

    @Test
    public void 장바구니_회원_매핑_조회_테스트() throws Exception{
        //given
        Member member = createMember();
        this.memberRepository.save(member);

        //when
        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);


        //then

        Cart saveCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(saveCart.getMember().getId(), member.getId());

    }
}