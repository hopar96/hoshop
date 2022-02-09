package hoho.hoshop.repository;

import hoho.hoshop.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMemberId(String memberId);
}
