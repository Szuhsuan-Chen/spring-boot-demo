package training.web;
import java.util.List;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;

//加入首頁
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    //處理來自路徑 /getIntArray 的請求
    @GetMapping("/getIntArray")
    public int[] getIntArray() {
        int[] data = {10, 1, 3, 4};
        return data;
    }

    @GetMapping("/getIntList")
    public List<Integer> getIntList() {
        List<Integer> data = List.of(3, 10, 2, 1);
        return data;
    }

    @GetMapping("/getCoordinates")
    public Map getCoordinates() {
        Map data = Map.of("x", 10, "y", 20, "z", 30);
        return data;
    }

    @GetMapping("/getPoint")
    public Point getPoint() {
        Point p1 = new Point(10, 20);
        return p1;
    }

    @GetMapping("/getPointList")
    public List getPointList() {
        Point p1 = new Point(10, 20);
        Point p2 = new Point(30, 40);
        return List.of(p1, p2);
    }

    @GetMapping("/getStudentGrades")
    public Map getStudentGrades() {
        int[] data = {60, 70 , 80, 90};
        return Map.of("name", "Student Grade", "data", data);
    }

    //處理來自路徑 /echo?name=名字 的請求
    @GetMapping("/echo")
    public String echo(@RequestParam("name") String name) {
        return "Echo: " + name;
    }

    //處理來自路徑 /add?n1=整數&n2=整數 的請求
    @GetMapping("/add")
    public Map add(@RequestParam("n1") int n1, @RequestParam("n2") int n2) {
        int result = n1 + n2;
        return Map.of("result", result);
    }

    //處理來自路徑 /test/任意的文字 的請求
    @GetMapping("/test/{name}")
    public String test(@PathVariable String name) {
        return "Hello, " + name + "!";
    }

    //處理來自路徑 /square/任意的數字 的請求
    @GetMapping("/square/{number}")
    public String square(@PathVariable int number) {
        int result = number * number;
        return "The square of " + number + " is " + result + ".";
    }
}