package net.javaguides.springboot.web;

import java.io.IOException;
import java.util.HashMap;
// import java.net.http.HttpHeaders;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MainController {

	private final String FLASK_API_URL = "https://your-ngrok-url.ngrok-free.app/predict-video"; // Change this
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/")
	public String home() {
		return "index";
	}

	@PostMapping("/spam-mail/analyze")
	public String analyzeSpamMail(@RequestParam("file") MultipartFile file, Model model) {
    try {
        // Prepare request to Flask API
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        // Replace with your current ngrok URL
        String flaskApiUrl = "https://1330-35-237-122-247.ngrok-free.app/predict";

        ResponseEntity<Map> response = restTemplate.postForEntity(flaskApiUrl, requestEntity, Map.class);
        String prediction = response.getBody().get("prediction").toString();

        model.addAttribute("result", prediction);

    } catch (Exception e) {
        model.addAttribute("result", "Error: " + e.getMessage());
    }

    return "spam-mail"; // Your Thymeleaf template name (without .html)
	}


	@PostMapping("/deepfake/analyze")
    public String analyzeVideo(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Build the request
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<String, Object>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(FLASK_API_URL, requestEntity, String.class);
        model.addAttribute("result", response.getBody());

        return "deepfake";  // Name of your Thymeleaf HTML page
    }

    @GetMapping
    public String showForm() {
        return "deepfake";
    }


	@PostMapping("/spam-call/analyze")
	public String analyzeSpamCall(@RequestParam("from") int from,
                              @RequestParam("to") int to,
                              @RequestParam("duration") int duration,
                              Model model) {
    	try {
        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Prepare body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("FROM", from);
        requestBody.put("TO", to);
        requestBody.put("DURATION", duration);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        // Replace with your current ngrok or deployed Flask URL
        String flaskApiUrl = "https://your-ngrok-url.ngrok-free.app/predict";

        ResponseEntity<Map> response = restTemplate.postForEntity(flaskApiUrl, requestEntity, Map.class);
        String prediction = response.getBody().get("prediction").toString();

        model.addAttribute("result", prediction);
    	} catch (Exception e) {
        model.addAttribute("result", "Error: " + e.getMessage());
    	}

    	return "spam-call"; // Your Thymeleaf HTML page for spam call result
	}


}




//String url = "https://f969-34-106-211-184.ngrok-free.app/predict";