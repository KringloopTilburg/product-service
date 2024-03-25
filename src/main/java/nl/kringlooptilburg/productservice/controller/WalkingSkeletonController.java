package nl.kringlooptilburg.productservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product-service")
public class WalkingSkeletonController {
    @GetMapping("/test")
    public String testGetMethod() {
        return "Hello world from product-service";
    }
}
