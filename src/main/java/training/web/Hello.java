package training.web;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;

//加入首頁
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//載入 MySQL Driver/Connector
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

//標示成Spring Boot應用程式
@SpringBootApplication
@RestController
public class Hello {
    public static void main(String[] args) {
        //啟動網站應用 http://127.0.0.1:8080
        SpringApplication.run(Hello.class, args);
        //載入 MySQL Driver/Connector
        try{
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //處理來自路徑 /execute 的請求
    @GetMapping("/execute")
    public String[] execute() {
        String[] data = executeSQL();
        return data;
    }

    private String[] executeSQL() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try{
            //主要的資料庫連線邏輯
            //建立連線
            con = DriverManager.getConnection("jdbc:mysql://localhost/website?user=root&password=root123");
            stmt = con.createStatement();
            //更新資料庫的資料
            // stmt.execute("UPDATE member SET name='彭彭' WHERE id=1");
            // System.out.println(stmt.getUpdateCount());
            //取得資料庫的資料
            stmt.executeQuery("SELECT * FROM member");
            rs = stmt.getResultSet();
            ArrayList<String> data = new ArrayList<>();
            while(rs.next()){ //如果還有下一筆資料，在迴圈中抓取資料
                System.out.println(rs.getString("name"));
                data.add(rs.getString("name"));
            }
            return data.toArray(new String[0]); //轉換為字串陣列回傳
        }catch(SQLException e){
            //有錯誤的時候要怎麼辦
            System.out.println(e.getMessage());     
        }finally{
            //確保資料庫連線能夠被關閉
            try{
                if(con != null){
                    con.close();
                }
            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        return new String[0];
    }

    //首頁
    @GetMapping("/")
    public String index() {
        return "Hello, Spring Boot!";
    }

    /* HttpSession 狀態管理 */
    //處理來自路徑 /hello?name=名字 的請求
    @GetMapping("/hello")
    public String hello(HttpSession session, @RequestParam("name") String name) {
        session.setAttribute("user-name", name);
        return "Hello, " + name + "!";
    }

    //處理來自路徑 /back 的請求
    @GetMapping("/back")
    public String back(HttpSession session){
        String name = (String)session.getAttribute("user-name");
        if (name == null) {
            return "Welcome Back, Who Are You?";
        }
        else {
            return "Welcome Back, " + name + "!";
        }
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

    @GetMapping("/getMap")
    public Map getMap() {
        Map data = Map.of("x", 10, "y", 20, "z", 30);
        return data;
    }

    @GetMapping("/getPoint")
    public Point getPoint() {
        Point p1 = new Point(10, 20);
        return p1;
    }

    @GetMapping("/getPointList")
    public List<Point> getPointList() {
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