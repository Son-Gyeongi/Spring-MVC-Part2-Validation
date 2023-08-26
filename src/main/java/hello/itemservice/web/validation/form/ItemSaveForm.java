package hello.itemservice.web.validation.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemSaveForm {

    @NotBlank
    private String itemName; // 빈값 + 공백만 있는 경우를 허용하지 않는다.

    @NotNull // null 을 허용하지 않는다.
    @Range(min = 1000, max = 1000000) // 범위 안의 값이어야 한다.
    private Integer price;

    @NotNull
    @Max(value = 9999) // 최대 9999까지만 허용한다.
    private Integer quantity;
}
