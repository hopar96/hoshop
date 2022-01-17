package hoho.hoshop.domain;

import hoho.hoshop.domain.auditing.BaseEntity;
import hoho.hoshop.dto.MemberForm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    private String id;

    private String password;
    private String name;

    @Embedded
    private Address address;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public static Member createMember(MemberForm memberForm, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        Address address = new Address(memberForm.getDo_city_gu_dong(), memberForm.getDetail_address());
        member.setId(memberForm.getId());
        member.setName(memberForm.getName());
        member.setEmail(memberForm.getEmail());
        member.setAddress(address);

        String password = passwordEncoder.encode(memberForm.getPassword());
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        return member;
    }

}
