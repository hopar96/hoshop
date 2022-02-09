package hoho.hoshop.service;

import hoho.hoshop.domain.Cart;
import hoho.hoshop.domain.CartItem;
import hoho.hoshop.domain.Member;
import hoho.hoshop.domain.item.Item;
import hoho.hoshop.dto.CartItemDto;
import hoho.hoshop.repository.CartItemRepository;
import hoho.hoshop.repository.CartRepository;
import hoho.hoshop.repository.ItemRepository;
import hoho.hoshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public Long addCart(CartItemDto cartItemDto, String id) {

        Member member = memberRepository.findById(id);
        Cart cart = cartRepository.findByMemberId(member.getId());

        //장바구니가 존재하지 않는다면 생성
        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        //해당 상품이 장바구니에 존재하지 않는다면 생성 후 추가
        if (cartItem == null) {
            cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
        } else { //해당 상품이 이미 장바구니에 존재한다면 수량을 증가
            cartItem.addCount(cartItemDto.getCount());
        }
        return cartItem.getId();
    }
}
