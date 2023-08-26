package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import hello.itemservice.web.validation.form.ItemSaveForm;
import hello.itemservice.web.validation.form.ItemUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor
public class ValidationItemControllerV4 {

    // 스프링에서 빈 주입
    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v4/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v4/addForm";
    }

    // 실제 저장
//    @PostMapping("/add")
    /**
    public String addItem(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // @ModelAttribute Item item는 model.addAttribute("item", item);이 자동으로 들어간다.
        // @Validated- item의 beanValidation 적용된다.
        // 어노테이션 기반으로 검증하는 검증기가 자동으로 검증을 하고 결과를 bindingResult에 넣어준다.

        // 특정 필드의 범위를 넘어서는 검증 - 가격 * 수량의 합은 10,000원 이상
        // 특정 필드가 아닌 복합 룰 검증 (복합 룰 검증)
        // 오브젝트 오류 관리할 때 @ScriptAssert 쓰는 거보다 직접 자바코드로 짜는 게 더 낫다.
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
        if (bindingResult.hasErrors()) { // 에러가 있다면
            log.info("errors = {}", bindingResult);
            // BindingResult는 자동으로 뷰에 같이 넘어가서 modelAttribute에 안 담아도 된다.
            return "validation/v4/addForm"; // 입력폼 뷰로 넘어간다.
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }
//    @PostMapping("/add")
    public String addItem2(@Validated(SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // @ModelAttribute Item item는 model.addAttribute("item", item);이 자동으로 들어간다.
        // @Validated- item의 beanValidation 적용된다.
        // 어노테이션 기반으로 검증하는 검증기가 자동으로 검증을 하고 결과를 bindingResult에 넣어준다.

        // 특정 필드의 범위를 넘어서는 검증 - 가격 * 수량의 합은 10,000원 이상
        // 특정 필드가 아닌 복합 룰 검증 (복합 룰 검증)
        // 오브젝트 오류 관리할 때 @ScriptAssert 쓰는 거보다 직접 자바코드로 짜는 게 더 낫다.
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
        if (bindingResult.hasErrors()) { // 에러가 있다면
            log.info("errors = {}", bindingResult);
            // BindingResult는 자동으로 뷰에 같이 넘어가서 modelAttribute에 안 담아도 된다.
            return "validation/v4/addForm"; // 입력폼 뷰로 넘어간다.
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }
     */
    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // @ModelAttribute Item item는 model.addAttribute("item", item);이 자동으로 들어간다.
        // @Validated- item의 beanValidation 적용된다.
        // 어노테이션 기반으로 검증하는 검증기가 자동으로 검증을 하고 결과를 bindingResult에 넣어준다.

        // 특정 필드의 범위를 넘어서는 검증 - 가격 * 수량의 합은 10,000원 이상
        // 특정 필드가 아닌 복합 룰 검증 (복합 룰 검증)
        // 오브젝트 오류 관리할 때 @ScriptAssert 쓰는 거보다 직접 자바코드로 짜는 게 더 낫다.
        if (form.getPrice() != null && form.getQuantity() != null) {
            // 가격, 수량 둘 다 null이 아니어야 한다.
            int resultPrice = form.getPrice() * form.getQuantity();
            if (resultPrice < 10000) { // 가격*수량의 가격이 10,000원 미만이면 오류가 난다.
//                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice}, null));
                // bindingResult는 이미 objectName("item")을 알고 있다.
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        // 검증에 실패하면 다시 입력 폼으로 이동, binding 실패 시
        if (bindingResult.hasErrors()) { // 에러가 있다면
            log.info("errors = {}", bindingResult);
            // BindingResult는 자동으로 뷰에 같이 넘어가서 modelAttribute에 안 담아도 된다.
            return "validation/v4/addForm"; // 입력폼 뷰로 넘어간다.
        }

        // 성공 로직
        // Item을 생성하고 form에서 필요한 값들을 넣어준다.
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/editForm";
    }

//    @PostMapping("/{itemId}/edit")
    /**
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute Item item, BindingResult bindingResult) {

        // 특정 필드의 범위를 넘어서는 검증 - 가격 * 수량의 합은 10,000원 이상
        // 특정 필드가 아닌 복합 룰 검증 (복합 룰 검증), 오브젝트 오류
        if (item.getPrice() != null && item.getQuantity() != null) {
            // 가격, 수량 둘 다 null이 아니어야 한다.
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) { // 가격*수량의 가격이 10,000원 미만이면 오류가 난다.
//                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice}, null));
                // bindingResult는 이미 objectName("item")을 알고 있다.
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v4/editForm"; // 문제가 생기면 다시 editForm으로 간다.
        }

        itemRepository.update(itemId, item);
        return "redirect:/validation/v4/items/{itemId}";
    }
//    @PostMapping("/{itemId}/edit")
    public String editV2(@PathVariable Long itemId, @Validated(UpdateCheck.class) @ModelAttribute Item item, BindingResult bindingResult) {

        // 특정 필드의 범위를 넘어서는 검증 - 가격 * 수량의 합은 10,000원 이상
        // 특정 필드가 아닌 복합 룰 검증 (복합 룰 검증), 오브젝트 오류
        if (item.getPrice() != null && item.getQuantity() != null) {
            // 가격, 수량 둘 다 null이 아니어야 한다.
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) { // 가격*수량의 가격이 10,000원 미만이면 오류가 난다.
//                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice}, null));
                // bindingResult는 이미 objectName("item")을 알고 있다.
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v4/editForm"; // 문제가 생기면 다시 editForm으로 간다.
        }

        itemRepository.update(itemId, item);
        return "redirect:/validation/v4/items/{itemId}";
    }
     */
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult) {

        // 특정 필드의 범위를 넘어서는 검증 - 가격 * 수량의 합은 10,000원 이상
        // 특정 필드가 아닌 복합 룰 검증 (복합 룰 검증), 오브젝트 오류
        if (form.getPrice() != null && form.getQuantity() != null) {
            // 가격, 수량 둘 다 null이 아니어야 한다.
            int resultPrice = form.getPrice() * form.getQuantity();
            if (resultPrice < 10000) { // 가격*수량의 가격이 10,000원 미만이면 오류가 난다.
//                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice}, null));
                // bindingResult는 이미 objectName("item")을 알고 있다.
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v4/editForm"; // 문제가 생기면 다시 editForm으로 간다.
        }

        Item itemParam = new Item();
        itemParam.setItemName(form.getItemName());
        itemParam.setPrice(form.getPrice());
        itemParam.setQuantity(form.getQuantity());

        itemRepository.update(itemId, itemParam);
        return "redirect:/validation/v4/items/{itemId}";
    }
}

