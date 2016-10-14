package site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        ResponseEntity<Map> response = rest.getForEntity("/", Map.class);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("greeting", response.getBody().get("message"));
            return "home";
        } else {
            throw new ApiCallFailedException(response.getStatusCode());
        }
    }

    public static class ApiCallFailedException extends RuntimeException {
        private final HttpStatus statusCode;
    
        public ApiCallFailedException(HttpStatus statusCode) {
            this.statusCode = statusCode;
        }
    
        @Override
        public String getMessage() {
            return statusCode.toString() + " " + statusCode.getReasonPhrase();
        }
    }
}
