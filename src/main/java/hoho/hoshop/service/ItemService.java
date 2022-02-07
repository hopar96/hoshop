package hoho.hoshop.service;

import hoho.hoshop.domain.item.Item;
import hoho.hoshop.domain.item.ItemImg;
import hoho.hoshop.dto.ItemFormDto;
import hoho.hoshop.dto.ItemImgDto;
import hoho.hoshop.dto.ItemSearchDto;
import hoho.hoshop.dto.MainItemDto;
import hoho.hoshop.repository.ItemImgRepository;
import hoho.hoshop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;


    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        // 상품 등록
        Item item = new Item();

        item.setName(itemFormDto.getItemName());
        item.setItemDetail(itemFormDto.getItemDetail());
        item.setBrand(itemFormDto.getBrand());
        item.setItemSellStatus(itemFormDto.getItemSellStatus());
        item.setPrice(itemFormDto.getPrice());
        item.setCategory(itemFormDto.getCategory());
        item.setStockQuantity(itemFormDto.getStockQuantity());
        item.setSize(itemFormDto.getSize());
//        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        //이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if (i == 0) {
                itemImg.setRepimgYn("Y");
            } else {
                itemImg.setRepimgYn("N");
            }
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId) {

        // 상품 id를 기반으로 상품 이미지 엔티티 객체 가져옴
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        // 상품 이미지 DTO 객체 담을 그릇 생성
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        // 상품 이미지 엔티티 객체를 상품 이미지 DTO 객체로 변환
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        // 상품 id를 기반으로 상품 엔티티 객체 가져옴
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityExistsException::new);

        // 상품 엔티티 객체를 상품 DTO 객체로 변환
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList)
            throws Exception {
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityExistsException::new);
        item.updateItem(itemFormDto);

        List<Long> itemImgIds = itemFormDto.getItemImgIds();
        for (int i = 0; i < itemImgFileList.size(); i++) {
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }
        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }
}
