package hello.itemservice.web.validation.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemUpdateForm {

    @NotNull // null 을 허용하지 않는다.
    private Long id;

    @NotBlank // 빈값 + 공백만 있는 경우를 허용하지 않는다.
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000) // 범위 안의 값이어야 한다.
    private Integer price;

    // 수정에서는 수량은 자유롭게 변경할 수 있다.
    private Integer quantity;
}
