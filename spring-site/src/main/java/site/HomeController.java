package site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class HomeController {
    
    private final RestTemplate rest;

    @Autowired
    public HomeController(RestTemplateBuilder restTemplateBuilder, @Value("${api.base.url}") URI apiBaseUri) {
        this.rest = restTemplateBuilder.rootUri(apiBaseUri.toString()).build();
    }

    @RequestMapping(method = GET, path = "/")
    public String home(Model model) {

        Map response = rest.getForObject("/", Map.class);

        model.addAttribute("greeting", response.get("message"));

        return "home";
    }

}
