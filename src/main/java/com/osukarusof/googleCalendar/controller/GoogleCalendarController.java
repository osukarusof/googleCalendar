package com.osukarusof.googleCalendar.controller;

import com.osukarusof.googleCalendar.dto.GoogleCalendarDto;
import com.osukarusof.googleCalendar.dto.ResponseDto;
import com.osukarusof.googleCalendar.service.GoogleCalendarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("api/google-calendar")
@RequiredArgsConstructor
public class GoogleCalendarController {

    private final GoogleCalendarService googleCalendarService;

    @GetMapping("/authorize-url")
    public ResponseEntity<ResponseDto> authorizeUrl() throws GeneralSecurityException, IOException {
        return new ResponseEntity<ResponseDto>(googleCalendarService.authorizeUrl(), HttpStatus.OK);
    }

    @PostMapping("/register-event")
    public ResponseEntity<ResponseDto> registerEvent(@RequestParam("code") String code,
                                                @RequestBody @Valid GoogleCalendarDto googleCalendar
    ){
        return new ResponseEntity<ResponseDto>(googleCalendarService.registerEvent(googleCalendar, code), HttpStatus.OK);
    }
}
