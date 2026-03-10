package training.web;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//加入首頁
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

//標示成Spring Boot應用程式
@SpringBootApplication
@RestController
public class Hello {
    public static void main(String[] args) {
        //啟動網站應用 http://127.0.0.1:8080
        SpringApplication.run(Hello.class, args);
    }

    //首頁
    @GetMapping("/")
    public String index() {
        return "Hello, Spring Boot!";
    }
}