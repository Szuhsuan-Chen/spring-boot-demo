package training.web;
import java.util.Map;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

    //註冊會員帳號的API
    private boolean insertmember(String name, String email, String password) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mywebsite?user=root&password=root123");
            stmt = con.prepareStatement("SELECT * FROM member WHERE email = ?");
            stmt.setNString(1, email);
            rs = stmt.executeQuery();
            if(rs.next()){ //如果有資料，表示此Email已被註冊
                return false;
            }
            //如果沒有資料，表示此Email未被註冊，可以新增會員資料
            stmt = con.prepareStatement("INSERT INTO member(name, email, password) VALUES(?, ?, ?)");
            stmt.setNString(1, name);
            stmt.setNString(2, email);
            stmt.setNString(3, password);
            stmt.execute();
            return true;  //沒有跳到錯誤的區塊，那就是成功
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
        finally {  //確保連線關閉
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    @PostMapping("/api/member")
    public Map signup(@RequestParam String name, @RequestParam String email, @RequestParam String password){
        boolean result = insertmember(name, email, password);
        return Map.of("ok", result);
    }

    //登入會員帳號的API
    public String getMemberName(String email, String password){
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mywebsite?user=root&password=root123");
            stmt = con.prepareStatement("SELECT name FROM member WHERE email = ? AND password = ?");
            stmt.setNString(1, email);
            stmt.setNString(2, password);
            rs = stmt.executeQuery();
            if(rs.next()){  //如果有資料，表示帳號密碼正確，回傳姓名
                return rs.getString("name");
            }
            return null;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    @PutMapping("/api/member/auth")
    public Map signin(HttpSession session, @RequestParam String email, @RequestParam String password){
        // 如果會話中已經有會員名稱，表示用戶已登入，無需再次登入
        if (session.getAttribute("member-name") != null) {
            return Map.of("ok", true, "message", "Already logged in");
        }

        String name = getMemberName(email, password);
        if(name == null){
            return Map.of("ok", false);
        }else{
            session.setAttribute("member-name", name); //設定使用者身份為從資料庫抓取到的姓名
            return Map.of("ok", true);
        }
    }
    //檢查會員登入狀態的API
    @GetMapping("/api/member/auth")
    public Map checksignin(HttpSession session){
        String name = (String) session.getAttribute("member-name");
        if(name == null){
            return Map.of("ok", false);
        }else{
            return Map.of("ok", true, "name", name);
        }
    }
}
