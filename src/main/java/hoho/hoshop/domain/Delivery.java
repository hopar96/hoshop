package hoho.hoshop.domain;

import hoho.hoshop.constant.DeliveryStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "delivery")
    private Order order;

    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
}
