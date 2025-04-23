package net.zsoo.spring.jsp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class JspController {
    @RequestMapping("/jsp")
    public ModelAndView index(Model model) {
        model.addAttribute("message", "Hello from JSP!");
        return new ModelAndView("sample", "model", model);
    }
}
