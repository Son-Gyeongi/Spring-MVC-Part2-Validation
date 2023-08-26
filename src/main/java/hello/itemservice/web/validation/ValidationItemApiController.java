package hello.itemservice.web.validation;

import hello.itemservice.web.validation.form.ItemSaveForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Bean Validation - HTTP 메시지 컨버터
 * @RequestBody
 */
@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {

//    @ResponseBody - @RestController이면 자동으로 @ResponseBody가 붙는다. JSON객체로 반환한다.
    @PostMapping("/add")
    public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult) {
        log.info("API 컨트롤러 호출");

        if (bindingResult.hasErrors()) { // 오류가 있다면
            log.info("검증 오류 발생 errors = {}", bindingResult);
            return bindingResult.getAllErrors(); // FieldError, ObjectError 다 반환해준다.
        }

        log.info("성공 로직 실행");
        return form;
    }
}
