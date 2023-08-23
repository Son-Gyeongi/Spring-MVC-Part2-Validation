package hello.itemservice.validation;

import hello.itemservice.domain.item.Item;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class BeanValidationTest {

    @Test
    void beanValidation() {
        /** 준비
         * 검증기 생성
         */
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();// fatory공장에서 검증기를 꺼낸다.

        // 사용
        Item item = new Item();
        item.setItemName(" "); // 공백
        item.setPrice(0); // 가격 0원
        item.setQuantity(10000); // 수량 10000개

        /**
         * 검증 실행
         * violations가 빈값이면 오류가 없는거다. 무언가 들어있으면 문제가 있다.
         */
        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        for (ConstraintViolation<Item> violation : violations) {
            System.out.println("violation = " + violation);
            System.out.println("violation.getMessage() = " + violation.getMessage());
        }
    }
}
