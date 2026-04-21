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
import org.springframework.security.crypto.password.PasswordEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

//標示成Spring Boot應用程式
@SpringBootApplication
@RestController
public class MemberApplication {
    // 由 Spring 注入的密碼編碼器；final 代表建構完成後不再變更
    private final PasswordEncoder passwordEncoder;
    private final DataSource dataSource;

    // 建構子注入：Spring 建立 MemberApplication 時，會自動把 PasswordEncoder 傳進來
    public MemberApplication(PasswordEncoder passwordEncoder, DataSource dataSource) {
        // this.passwordEncoder 是類別欄位；右邊 passwordEncoder 是建構子參數
        this.passwordEncoder = passwordEncoder;
        this.dataSource = dataSource;
    }

    public static void main(String[] args) {
        //啟動網站應用 http://127.0.0.1:8080
        SpringApplication.run(MemberApplication.class, args);
    }

    //註冊會員帳號的API
    private boolean insertMember(String name, String email, String password) {
        String checkSql = "SELECT 1 FROM member WHERE email = ?";
        String insertSql = "INSERT INTO member(name, email, password) VALUES(?, ?, ?)";
        
        try (Connection con = dataSource.getConnection()) {
            // 檢查 Email 是否已存在
            try (PreparedStatement stmt = con.prepareStatement(checkSql)) {
                stmt.setNString(1, email);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) return false;
                }
            }
            
            // 新增會員
            try (PreparedStatement stmt = con.prepareStatement(insertSql)) {
                stmt.setNString(1, name);
                stmt.setNString(2, email);
                stmt.setNString(3, passwordEncoder.encode(password));
                stmt.executeUpdate();
                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @PostMapping("/api/member")
    public Map<String, Object> signUp(@RequestParam String name, @RequestParam String email, @RequestParam String password){
        boolean result = insertMember(name, email, password);
        return Map.of("ok", result);
    }

    //登入會員帳號的API
    public String getMemberName(String email, String password){
        String sql = "SELECT name, password FROM member WHERE email = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
             
            stmt.setNString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && passwordEncoder.matches(password, rs.getString("password"))) {
                    return rs.getString("name");
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PutMapping("/api/member/auth")
    public Map<String, Object> signIn(HttpSession session, @RequestParam String email, @RequestParam String password) {
        // 如果會話中已經有會員名稱，表示用戶已登入，無需再次登入
        if (session.getAttribute("member-name") != null) {
            // 這裡可以返回一個特定的訊息，讓前端判斷並導向會員頁面
            return Map.of("ok", true, "message", "Already logged in");
        }

        String name = getMemberName(email, password);
        if (name == null) {
            return Map.of("ok", false);
        } else {
            session.setAttribute("member-name", name); //設定使用者身份為從資料庫抓取到的姓名
            return Map.of("ok", true);
        }
    }

    //檢查會員登入狀態的API
    @GetMapping("/api/member/auth")
    public Map<String, Object> checkSignIn(HttpSession session){
        String name = (String) session.getAttribute("member-name");
        if (name == null) {
            return Map.of("ok", false);
        } else {
            return Map.of("ok", true, "name", name);
        }
    }

    //登出會員帳號的API
    @PostMapping("/api/member/logout")
    public Map<String, Object> signOut(HttpSession session){
        session.invalidate(); // 使當前會話失效，清除所有會話資料
        return Map.of("ok", true);
    }
}
