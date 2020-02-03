package me.stst.weatherstation.rest;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Controller
@RestController("/rest/auth")
public class AuthenticationController {

    @GetMapping("generate_token")
    public ResponseEntity generateToken(){
        return ResponseEntity.ok(RandomStringUtils.random(200,true,true));
    }
}
