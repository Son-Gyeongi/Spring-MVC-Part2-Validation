package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v1/items")
@RequiredArgsConstructor
public class ValidationItemControllerV1 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v1/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v1/addForm";
    }

    // 실제 저장
    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model) {
        // @ModelAttribute Item item는 model.addAttribute("item", item);이 자동으로 들어간다.

        // 검증 실패 시 뭐가 실패했는지 알려주기 위해서 Model에 검증 오류 결과가 포함되어야 한다.
        // 검증 오류 결과를 보관
        Map<String, String> errors = new HashMap<>();

        // 검증 로직
        // 상품명 필드 오류 시 (필드 오류 검증)
        if (!StringUtils.hasText(item.getItemName())) { // 글자가 없으면, hasText: 글자가 있는가
            errors.put("itemName", "상품 이름은 필수입니다."); // 키: itemName, 값: 상품 이름은 필수입니다. 화면에 보여줄거다.
        }
        // 상품가격 필드 오류 시
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            // 상품 가격이 없거나 천원 미만이거나 백만원 초과인 경우
            errors.put("price", "가격은 1,000원 ~ 1,000,000원까지 허용합니다.");
        }
        // 상품수량 필드 오류 시
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            // 수량이 없거나 9999개 넘는 경우
            errors.put("quantity", "수량은 최대 9,999개까지 허용합니다.");
        }

        // 특정 필드의 범위를 넘어서는 검증 - 가격 * 수량의 합은 10,000원 이상
        // 특정 필드가 아닌 복합 룰 검증 (복합 룰 검증)
        if (item.getPrice() != null && item.getQuantity() != null) {
            // 가격, 수량 둘 다 null이 아니어야 한다.
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) { // 가격*수량의 가격이 10,000원 미만이면 오류가 난다.
                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice);
            }
        }

        // 검증에 실패하면 다시 입력 폼으로 이동
        if (!errors.isEmpty()) { // 에러가 있다면
            log.info("errors = {}", errors);
            model.addAttribute("errors", errors);
            return "validation/v1/addForm"; // 입력폼 뷰로 넘어간다.
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v1/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v1/items/{itemId}";
    }

}

