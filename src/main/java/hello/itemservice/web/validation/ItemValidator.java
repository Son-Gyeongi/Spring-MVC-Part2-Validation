package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

// Validation 분리1
@Component // 컴포넌트 스캔이 되어서 스프링 빈에 등록이 된다.
public class ItemValidator implements Validator {

    // 해당 검증기를 지원하는 여부 확인
   @Override
    public boolean supports(Class<?> clazz) {
       // 파라미터로 넘어오는 클래스가 Item에 지원이 되는가
        return Item.class.isAssignableFrom(clazz);
       // 클래스로 넘어오는 타입이랑 item타입이 같은가. item == clazz
       // 또는 isAssignableFrom 쓰는 이유 만약에 item 에 자식이 있다면 자식도 item == subItem
    }

    // 실제 검증, 검증 로직, (검증 대상 객체와 BindingResult)
    @Override
    public void validate(Object target, Errors errors) { // Errors는 BindingResult의 부모 클래스이다.
        Item item = (Item) target; // Item 으로 형변환
        
        // 상품명 필드 오류 시 (필드 오류 검증)
        if (!StringUtils.hasText(item.getItemName())) { // 글자가 없으면, hasText: 글자가 있는가
//            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));
            // bindingResult는 이미 objectName("item")을 알고 있다.
            errors.rejectValue("itemName", "required");
        }
//        위와 같은 코드를 나타낼 수 있는 유틸리티 - 제공하는 기능은 Empty , 공백 같은 단순한 기능만 제공
//        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");

        // 상품가격 필드 오류 시
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            // 상품 가격이 없거나 천원 미만이거나 백만원 초과인 경우
//            bindingResult.addError(new FieldError("item", "price",item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
            errors.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }
        // 상품수량 필드 오류 시
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            // 수량이 없거나 9999개 넘는 경우
//            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
            errors.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        // 특정 필드의 범위를 넘어서는 검증 - 가격 * 수량의 합은 10,000원 이상
        // 특정 필드가 아닌 복합 룰 검증 (복합 룰 검증)
        if (item.getPrice() != null && item.getQuantity() != null) {
            // 가격, 수량 둘 다 null이 아니어야 한다.
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) { // 가격*수량의 가격이 10,000원 미만이면 오류가 난다.
//                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice}, null));
                // bindingResult는 이미 objectName("item")을 알고 있다.
                errors.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
    }
}
