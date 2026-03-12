package training.web;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;

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

    //處理來自路徑 /test 的請求
    @GetMapping("/test")
    public String test() {
        return "Hello, Routing Test!";
    }

    //處理來自路徑 /test/任意的文字 的請求
    @GetMapping("/test/{name}")
    public String test(@PathVariable String name) {
        return "Hello, " + name + "!";
    }

    //處理來自路徑 /square/任意的文字 的請求
    @GetMapping("/square/{number}")
    public String square(@PathVariable int number) {
        int result = number * number;
        return "The square of " + number + " is " + result + ".";
    }
}