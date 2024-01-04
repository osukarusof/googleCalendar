package com.osukarusof.googleCalendar.controller;

import com.osukarusof.googleCalendar.dto.GoogleCalendarDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/google-calendar")
public class GoogleCalendarController {

    @GetMapping("/authorize-url")
    public ResponseEntity<String> authorizeUrl(){
        return new ResponseEntity<String>("hola", HttpStatus.OK);
    }


    @PostMapping("/register-event")
    public ResponseEntity<String> registerEvent(@Valid
                                                @RequestBody GoogleCalendarDto googleCalendar,
                                                @RequestParam("code") String code
    ){
        return new ResponseEntity<String>("hola", HttpStatus.OK);
    }
}
