package hoho.hoshop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    private String do_city_gu_dong;
    private String detail_address;

    protected Address() {
    }

    public Address(String do_city_gu_dong, String detail_address) {
        this.do_city_gu_dong = do_city_gu_dong;
        this.detail_address = detail_address;
    }
}
