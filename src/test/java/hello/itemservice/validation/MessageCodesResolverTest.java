package hello.itemservice.validation;

import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

import static org.assertj.core.api.Assertions.*;

public class MessageCodesResolverTest {

    /**
     * 오류 코드와 메시지 처리4
     * MessageCodesResolver는 인터페이스, DefaultMessageCodesResolver는 기본 구현체
     * MessageCodesResolver - 검증 오류 코드로 메시지 코드들을 생성한다. 주로 다음과 함께 사용 ObjectError , FieldError
     * resolveMessageCodes()메서드에 에러코드 하나 넣으면 여러개의 값들을 반환해준다.
     */
    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    // Object관련 에러
    @Test
    void messageCodesResolverObject() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
//        for (String messageCode : messageCodes) {
//            System.out.println("messageCode = " + messageCode);
//            /**
//             * messageCode = required.item - 디테일한 게 먼저
//             * messageCode = required - 그 다음에 범용적인거
//             */
//        }
        assertThat(messageCodes).containsExactly("required.item", "required");
    }

    @Test
    void messageCodesResolverField() {
        /**
         * ValidationItemControllerV2 클래스 235번째줄
         * bindingResult.rejectValue("itemName", "required");
         * rejectValue() 안에서 codesResolver를 호출한다.
         */
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
//        for (String messageCode : messageCodes) {
//            System.out.println("messageCode = " + messageCode);
//            /**
//             * messageCode = required.item.itemName
//             * messageCode = required.itemName
//             * messageCode = required.java.lang.String
//             * messageCode = required
//             */
//        }
        assertThat(messageCodes).containsExactly(
                "required.item.itemName",
                "required.itemName",
                "required.java.lang.String",
                "required");
    }
}
