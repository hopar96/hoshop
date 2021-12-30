package hoho.hoshop.web;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter @Setter
public class MemberForm {

    @NotBlank(message = "ID를 입력하세요.")
    private String id;

    @NotBlank(message = "패스워드를 입력하세요.")
    private String password;

    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "이메일 형식으로 입력하세요.")
    private String email;

    @NotBlank(message = "이름을 입력하세요")
    private String name;

    private String do_city_gu_dong;
    private String detail_address;

}
