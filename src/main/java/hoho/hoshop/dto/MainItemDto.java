package hoho.hoshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainItemDto {

    private Long id;
    private String itemName;
    private String itemDetail;
    private String imgUrl;
    private Integer price;

}
