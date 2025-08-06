package net.zsoo.spring.properties;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/properties")
@RequiredArgsConstructor
public class SamplePropertyController {

    private final SampleProperties sampleProperties;

    @RequestMapping("/info")
    public ModelAndView info(Model model) {
        model.addAttribute("name", sampleProperties.getName());
        model.addAttribute("description", sampleProperties.getDescription());
        model.addAttribute("version", sampleProperties.getVersion());
        return new ModelAndView("properties/info", "model", model);
    }
}
