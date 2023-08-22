package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    // 스프링에서 빈 주입
    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    // 실제 저장
//    @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // @ModelAttribute Item item는 model.addAttribute("item", item);이 자동으로 들어간다.
        // BindingResult - item에 binding 된 결과가 담긴다. (item에 값이 담길때 오류가 나는 것들이 담긴다)
        // BindingResult bindingResult 파라미터의 위치는 @ModelAttribute Item item 다음에 와야 한다.

        // 검증 실패 시 뭐가 실패했는지 알려주기 위해서 Model에 검증 오류 결과가 포함되어야 한다.
        // 검증 오류 결과를 보관
//        Map<String, String> errors = new HashMap<>();
        // BindingResult가 이전에 했던 errors역할을 해준다.

        // 검증 로직
        // 상품명 필드 오류 시 (필드 오류 검증)
        if (!StringUtils.hasText(item.getItemName())) { // 글자가 없으면, hasText: 글자가 있는가
//            errors.put("itemName", "상품 이름은 필수입니다."); // 키: itemName, 값: 상품 이름은 필수입니다. 화면에 보여줄거다.
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
        }
        // 상품가격 필드 오류 시
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            // 상품 가격이 없거나 천원 미만이거나 백만원 초과인 경우
//            errors.put("price", "가격은 1,000원 ~ 1,000,000원까지 허용합니다.");
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000원 ~ 1,000,000원까지 허용합니다."));

        }
        // 상품수량 필드 오류 시
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            // 수량이 없거나 9999개 넘는 경우
//            errors.put("quantity", "수량은 최대 9,999개까지 허용합니다.");
            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999개까지 허용합니다."));
        }

        // 특정 필드의 범위를 넘어서는 검증 - 가격 * 수량의 합은 10,000원 이상
        // 특정 필드가 아닌 복합 룰 검증 (복합 룰 검증)
        if (item.getPrice() != null && item.getQuantity() != null) {
            // 가격, 수량 둘 다 null이 아니어야 한다.
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) { // 가격*수량의 가격이 10,000원 미만이면 오류가 난다.
//                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice);
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        // 검증에 실패하면 다시 입력 폼으로 이동
//        if (!errors.isEmpty()) { // 에러가 있다면
        if (bindingResult.hasErrors()) { // 에러가 있다면
            log.info("errors = {}", bindingResult);
            // BindingResult는 자동으로 뷰에 같이 넘어가서 modelAttribute에 안 담아도 된다.
//            model.addAttribute("errors", errors);
            return "validation/v2/addForm"; // 입력폼 뷰로 넘어간다.
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
    // 사용자가 입력한 값 저장
//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // @ModelAttribute Item item는 model.addAttribute("item", item);이 자동으로 들어간다.

        // 검증 로직
        // 상품명 필드 오류 시 (필드 오류 검증)
        if (!StringUtils.hasText(item.getItemName())) { // 글자가 없으면, hasText: 글자가 있는가
//            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
            // item.getItemName(): 사용자가 입력한 값(rejectedValue)
            // bindingFailure는 타입 오류 같은 바인딩이 실패했는지 여부를 적어주면 된다.
            // 여기서는 바인딩이 실패한 것은 아니기 때문에 false 를 사용한다.
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(),
                    false, null, null, "상품 이름은 필수입니다."));
        }
        // 상품가격 필드 오류 시
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            // 상품 가격이 없거나 천원 미만이거나 백만원 초과인 경우
//            bindingResult.addError(new FieldError("item", "price", "가격은 1,000원 ~ 1,000,000원까지 허용합니다."));
            bindingResult.addError(new FieldError("item", "price",item.getPrice(),
                    false, null, null, "가격은 1,000원 ~ 1,000,000원까지 허용합니다."));

        }
        // 상품수량 필드 오류 시
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            // 수량이 없거나 9999개 넘는 경우
//            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999개까지 허용합니다."));
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(),
                    false, null, null, "수량은 최대 9,999개까지 허용합니다."));
        }

        // 특정 필드의 범위를 넘어서는 검증 - 가격 * 수량의 합은 10,000원 이상
        // 특정 필드가 아닌 복합 룰 검증 (복합 룰 검증)
        if (item.getPrice() != null && item.getQuantity() != null) {
            // 가격, 수량 둘 다 null이 아니어야 한다.
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) { // 가격*수량의 가격이 10,000원 미만이면 오류가 난다.
//                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice);
                bindingResult.addError(new ObjectError("item", null, null, "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        // 검증에 실패하면 다시 입력 폼으로 이동
//        if (!errors.isEmpty()) { // 에러가 있다면
        if (bindingResult.hasErrors()) { // 에러가 있다면
            log.info("errors = {}", bindingResult);
            // BindingResult는 자동으로 뷰에 같이 넘어가서 modelAttribute에 안 담아도 된다.
//            model.addAttribute("errors", errors);
            return "validation/v2/addForm"; // 입력폼 뷰로 넘어간다.
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
    // 오류 코드와 메시지 처리1
//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // @ModelAttribute Item item는 model.addAttribute("item", item);이 자동으로 들어간다.

        // 검증 로직
        // 상품명 필드 오류 시 (필드 오류 검증)
        if (!StringUtils.hasText(item.getItemName())) { // 글자가 없으면, hasText: 글자가 있는가
//            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
            // item.getItemName(): 사용자가 입력한 값(rejectedValue)
            // bindingFailure는 타입 오류 같은 바인딩이 실패했는지 여부를 적어주면 된다.
            // 여기서는 바인딩이 실패한 것은 아니기 때문에 false 를 사용한다.
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(),
                    false, new String[]{"required.item.itemName"}, null, null));
        }
        // 상품가격 필드 오류 시
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            // 상품 가격이 없거나 천원 미만이거나 백만원 초과인 경우
//            bindingResult.addError(new FieldError("item", "price", "가격은 1,000원 ~ 1,000,000원까지 허용합니다."));
            bindingResult.addError(new FieldError("item", "price",item.getPrice(),
                    false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));

        }
        // 상품수량 필드 오류 시
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            // 수량이 없거나 9999개 넘는 경우
//            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999개까지 허용합니다."));
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(),
                    false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
        }

        // 특정 필드의 범위를 넘어서는 검증 - 가격 * 수량의 합은 10,000원 이상
        // 특정 필드가 아닌 복합 룰 검증 (복합 룰 검증)
        if (item.getPrice() != null && item.getQuantity() != null) {
            // 가격, 수량 둘 다 null이 아니어야 한다.
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) { // 가격*수량의 가격이 10,000원 미만이면 오류가 난다.
//                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice);
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"},
                        new Object[]{10000, resultPrice}, null));
            }
        }

        // 검증에 실패하면 다시 입력 폼으로 이동
//        if (!errors.isEmpty()) { // 에러가 있다면
        if (bindingResult.hasErrors()) { // 에러가 있다면
            log.info("errors = {}", bindingResult);
            // BindingResult는 자동으로 뷰에 같이 넘어가서 modelAttribute에 안 담아도 된다.
//            model.addAttribute("errors", errors);
            return "validation/v2/addForm"; // 입력폼 뷰로 넘어간다.
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
    // 오류 코드와 메시지 처리2 - rejectValue() , reject() 를 사용해서 기존 코드를 단순화해보자.
    // reject()는 object 이고 rejectValue()는 field이다.
//    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // @ModelAttribute Item item는 model.addAttribute("item", item);이 자동으로 들어간다.

        log.info("objectName = {}", bindingResult.getObjectName()); // item
        log.info("target={}", bindingResult.getTarget()); // item객체가 들어있다.

        // 검증 로직
        // 상품명 필드 오류 시 (필드 오류 검증)
        if (!StringUtils.hasText(item.getItemName())) { // 글자가 없으면, hasText: 글자가 있는가
//            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));
            // bindingResult는 이미 objectName("item")을 알고 있다.
            bindingResult.rejectValue("itemName", "required");
        }
//        위와 같은 코드를 나타낼 수 있는 유틸리티 - 제공하는 기능은 Empty , 공백 같은 단순한 기능만 제공
//        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");

        // 상품가격 필드 오류 시
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            // 상품 가격이 없거나 천원 미만이거나 백만원 초과인 경우
//            bindingResult.addError(new FieldError("item", "price",item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
            bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }
        // 상품수량 필드 오류 시
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            // 수량이 없거나 9999개 넘는 경우
//            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        // 특정 필드의 범위를 넘어서는 검증 - 가격 * 수량의 합은 10,000원 이상
        // 특정 필드가 아닌 복합 룰 검증 (복합 룰 검증)
        if (item.getPrice() != null && item.getQuantity() != null) {
            // 가격, 수량 둘 다 null이 아니어야 한다.
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) { // 가격*수량의 가격이 10,000원 미만이면 오류가 난다.
//                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice}, null));
                // bindingResult는 이미 objectName("item")을 알고 있다.
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        // 검증에 실패하면 다시 입력 폼으로 이동, binding 실패 시
//        if (!errors.isEmpty()) { // 에러가 있다면
        if (bindingResult.hasErrors()) { // 에러가 있다면
            log.info("errors = {}", bindingResult);
            // BindingResult는 자동으로 뷰에 같이 넘어가서 modelAttribute에 안 담아도 된다.
//            model.addAttribute("errors", errors);
            return "validation/v2/addForm"; // 입력폼 뷰로 넘어간다.
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    // Validation 분리1
    @PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // @ModelAttribute Item item는 model.addAttribute("item", item);이 자동으로 들어간다.

        // 검증기
        itemValidator.validate(item, bindingResult);

        // 검증에 실패하면 다시 입력 폼으로 이동, binding 실패 시
//        if (!errors.isEmpty()) { // 에러가 있다면
        if (bindingResult.hasErrors()) { // 에러가 있다면
            log.info("errors = {}", bindingResult);
            // BindingResult는 자동으로 뷰에 같이 넘어가서 modelAttribute에 안 담아도 된다.
//            model.addAttribute("errors", errors);
            return "validation/v2/addForm"; // 입력폼 뷰로 넘어간다.
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }
}

