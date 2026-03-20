package training.web;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller public class Page{
    @GetMapping("/multiply")
    public String multiply(@RequestParam int a, @RequestParam int b, Model model){
        int result = a * b;
        model.addAttribute("result", result);
        return "multiply"; //對應到resources/templates/multiply.html
    }
    @GetMapping("/back-template")
        public String backTemplate(Model model, HttpSession session){
            String name = (String)session.getAttribute("user-name");
            model.addAttribute("name", name);
            return "back";
        }
}