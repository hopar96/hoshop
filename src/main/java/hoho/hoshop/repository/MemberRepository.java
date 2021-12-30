package hoho.hoshop.repository;

import hoho.hoshop.domain.Address;
import hoho.hoshop.domain.Member;
import hoho.hoshop.domain.Role;
import hoho.hoshop.service.MemberService;
import hoho.hoshop.web.MemberForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findById(String id);
}
    /*@PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Member findOne(String id) {
        return em.find(Member.class, id);
    }

    public String findPassword(String id) {
        Member member = em.find(Member.class, id);
        return  member.getPassword();
    }



    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }*/