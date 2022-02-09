package hoho.hoshop.service;

import hoho.hoshop.constant.ItemSellStatus;
import hoho.hoshop.constant.OrderStatus;
import hoho.hoshop.domain.Member;
import hoho.hoshop.domain.Order;
import hoho.hoshop.domain.OrderItem;
import hoho.hoshop.domain.item.Item;
import hoho.hoshop.dto.OrderDto;
import hoho.hoshop.repository.ItemRepository;
import hoho.hoshop.repository.MemberRepository;
import hoho.hoshop.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.*;
import static org.junit.Assert.*;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MemberRepository memberRepository;

    public Item saveItem(){
        Item item = new Item();
        item.setName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockQuantity(100);
        return itemRepository.save(item);
    }

    public Member saveMember() {
        Member member = new Member();
        member.setId("testId");
        return memberRepository.save(member);
    }

    @Test
    public void order() throws Exception{
        //given
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());

        Long orderId = orderService.order(orderDto, member.getId());
        //when
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        List<OrderItem> orderItems = order.getOrderItems();
        int totalPrice = orderDto.getCount() * item.getPrice();

        //then
        assertEquals(totalPrice, order.getTotalPrice());

    }

    @Test
    public void 주문취소테스트() throws Exception{
        //given
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());

        Long orderId = orderService.order(orderDto, member.getId());
        //when
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        orderService.cancelOrder(orderId);

        //then
        assertEquals(OrderStatus.CANCEL, order.getStatus());
        assertEquals(100, item.getStockQuantity().intValue());
    }
}