package com.badwordcheck.webservice.test.helloworld;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("http://localhost:4200")
@RestController
public class HelloWorldController {

    @GetMapping("/hello-world")
    public String helloWorld() {
	return "Hello World";
    }

    @GetMapping("/hello-world-bean")
    public HelloWorldBean helloWorldBean() {
	return new HelloWorldBean("Hello World Bean");
    }

    @GetMapping("/hello-world/path-variable/{name}")
    public HelloWorldBean helloWorldBean(@PathVariable String name) {
	return new HelloWorldBean(String.format("Hello World, %s", name));
    }
}
