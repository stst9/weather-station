package me.stst.weatherstation.rest;

import me.stst.weatherstation.domain.ApiToken;
import me.stst.weatherstation.domain.User;
import me.stst.weatherstation.repository.ApiTokenDAO;
import me.stst.weatherstation.repository.UserDAO;
import me.stst.weatherstation.rest.model.RestAuthUser;
import me.stst.weatherstation.rest.model.RestGenericResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RestController
@RequestMapping("/api-public/auth")
public class AuthenticationController {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ApiTokenDAO apiTokenDAO;

    @GetMapping(path = "generate_token")
    public ResponseEntity generateToken(){
        return ResponseEntity.ok(RandomStringUtils.random(200,true,true));
    }

    @PostMapping()
    public ResponseEntity<RestGenericResponse<String>> getToken(@RequestBody RestAuthUser auth){
        System.out.println("+");
        User user=userDAO.findByLogin(auth.getLogin());
        ResponseEntity responseEntity=ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        if (user!=null){
            if (user.checkPassword(auth.getPassword())){
                List<ApiToken> apiTokens= apiTokenDAO.findByUserAndDevice(user,null);
                if (apiTokens==null||apiTokens.size()==0){
                    responseEntity=ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestGenericResponse<Object>(
                            "No general purpose api key found for your user. Please create one."));
                }else {
                    RestGenericResponse<String> response=new RestGenericResponse<>();
                    response.setPayload(apiTokens.get(0).getToken());
                    responseEntity=ResponseEntity.ok(response);
                }
            }else {
                responseEntity=ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new RestGenericResponse<Object>("Bad credentials"));
            }
        }else {
            responseEntity=ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new RestGenericResponse<Object>("Bad credentials"));
        }
        return responseEntity;
    }
}
