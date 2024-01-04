package com.osukarusof.googleCalendar.controller;

import com.osukarusof.googleCalendar.dto.GoogleCalendarDto;
import com.osukarusof.googleCalendar.service.GoogleCalendarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/google-calendar")
@RequiredArgsConstructor
public class GoogleCalendarController {

    private final GoogleCalendarService googleCalendarService;

    @GetMapping("/authorize-url")
    public ResponseEntity<String> authorizeUrl(){
        return new ResponseEntity<String>(googleCalendarService.authorizeUrl(), HttpStatus.OK);
    }

    @PostMapping("/register-event")
    public ResponseEntity<String> registerEvent(@Valid
                                                @RequestBody GoogleCalendarDto googleCalendar,
                                                @RequestParam("code") String code
    ){
        return new ResponseEntity<String>("hola", HttpStatus.OK);
    }
}
