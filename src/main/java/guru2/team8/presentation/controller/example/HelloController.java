package guru2.team8.presentation.controller.example;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "HelloController", description  = "Hello API")
public class HelloController {

    @GetMapping("/hello")
    @Operation(summary = "인사 메시지 반환", description = "hello 반환")
    public String sayHello() {
        return "Hello, World!";
    }
}
