package hoho.hoshop.repository;

import hoho.hoshop.domain.item.Item;
import hoho.hoshop.dto.ItemSearchDto;
import hoho.hoshop.dto.MainItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    // @QueryProjection을 이용하여 바로 Dto 객체 반환
    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
