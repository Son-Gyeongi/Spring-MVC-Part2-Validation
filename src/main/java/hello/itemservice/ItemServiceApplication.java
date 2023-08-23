package hello.itemservice;

import hello.itemservice.web.validation.ItemValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
//public class ItemServiceApplication implements WebMvcConfigurer { // Validation 글로벌 설정
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

	// Validation 글로벌 설정 - 모든 컨트롤러에서 다 적용
//	@Override
//	public Validator getValidator() {
//		return new ItemValidator();
//	}
}
