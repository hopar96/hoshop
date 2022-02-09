package hoho.hoshop.service;


import hoho.hoshop.domain.Member;
import hoho.hoshop.domain.Order;
import hoho.hoshop.domain.OrderItem;
import hoho.hoshop.domain.item.Item;
import hoho.hoshop.domain.item.ItemImg;
import hoho.hoshop.dto.OrderDto;
import hoho.hoshop.dto.OrderHistDto;
import hoho.hoshop.dto.OrderItemDto;
import hoho.hoshop.repository.ItemImgRepository;
import hoho.hoshop.repository.ItemRepository;
import hoho.hoshop.repository.MemberRepository;
import hoho.hoshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;    //상품을 불러와 재고 변경
    private final MemberRepository memberRepository; // 멤버를 불러와서 연결
    private final OrderRepository orderRepository;  //주문 객체를 저장
    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String id) {
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityExistsException::new);
        Member member = memberRepository.findById(id);

        List<OrderItem> orderItemList = new ArrayList<>();

        // OrderItem.createOrderItem -> Static 메소드
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        // Order.createOrder -> Static 메소드
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String id, Pageable pageable) {

        List<Order> orders = orderRepository.findOrders(id, pageable);
        Long totalCount = orderRepository.countOrder(id);

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.
                        findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            orderHistDtos.add(orderHistDto);
        }
        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String id) {

        //주문 취소 요청 유저
        Member curMember = memberRepository.findById(id);

        //상품을 주문한 유저
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        Member saveMember = order.getMember();

        if (!StringUtils.equals(curMember.getId(), saveMember.getId())) {
            return false;
        }
        return true;
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }
}
