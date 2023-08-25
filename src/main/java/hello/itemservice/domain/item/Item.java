package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
//@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000", message = "가격 * 수량을 했을 때 총합이 10000원 넘게 입력해주세요.")
// @ScriptAssert - Bean Validation에서 특정 필드( FieldError )가 아닌 해당 오브젝트 관련 오류( ObjectError ) 처리, 권장X
public class Item {

    @NotNull(groups = UpdateCheck.class) // 수정 요구사항 추가
    private Long id;

    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class}) // 빈값 + 공백만 있는 경우를 허용하지 않는다.
//    @NotBlank(message = "공백 X") // 오류가 났을 때 메시지를 직접 쓸 수 있다.
    private String itemName;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class}) // null 을 허용하지 않는다.
    @Range(min = 1000, max = 1000000, groups = {SaveCheck.class, UpdateCheck.class}) // 범위 안의 값이어야 한다.
    private Integer price;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Max(value = 9999, groups = {SaveCheck.class})// 최대 9999까지만 허용한다. 수정 요구사항 추가
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
