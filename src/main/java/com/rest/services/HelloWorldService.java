package com.rest.services;

import com.rest.json.HelloWorld;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by vikasnaiyar on 17/09/17.
 */
@Controller
@RequestMapping("/hello-world")
public class HelloWorldService {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(method= RequestMethod.GET)
    public @ResponseBody
    HelloWorld sayHello(@RequestParam(value="name", required=false, defaultValue="Stranger") String name) {
        return new HelloWorld(counter.incrementAndGet(), String.format(template, name));
    }

}
