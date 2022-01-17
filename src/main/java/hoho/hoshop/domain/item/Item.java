package hoho.hoshop.domain.item;

import hoho.hoshop.constant.Category;
import hoho.hoshop.constant.ItemSellStatus;
import hoho.hoshop.domain.auditing.BaseEntity;
import hoho.hoshop.dto.ItemFormDto;
import hoho.hoshop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Item extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Lob
    @Column
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String brand;

    private String size;
    
    private Integer stockQuantity;


    public void updateItem(ItemFormDto itemFormDto) {
        this.name = itemFormDto.getItemName();
        this.price = itemFormDto.getPrice();
        this.stockQuantity = itemFormDto.getStockQuantity();
        this.itemDetail = itemFormDto.getItemDetail();
        this.brand = itemFormDto.getBrand();
        this.category = itemFormDto.getCategory();
        this.size = itemFormDto.getSize();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("상품의 재고가 부족합니다. (현재 재고 수량: " + this.stockQuantity + ")");
        }
        this.stockQuantity = restStock;
    }


}
