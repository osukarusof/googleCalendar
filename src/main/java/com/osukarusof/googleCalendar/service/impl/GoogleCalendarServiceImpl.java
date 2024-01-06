package com.osukarusof.googleCalendar.service.impl;

import com.osukarusof.googleCalendar.dto.GoogleCalendarDto;
import com.osukarusof.googleCalendar.dto.ResponseDto;
import com.osukarusof.googleCalendar.entity.User;
import com.osukarusof.googleCalendar.integration.GoogleCalendar;
import com.osukarusof.googleCalendar.repository.UserRepository;
import com.osukarusof.googleCalendar.service.GoogleCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoogleCalendarServiceImpl implements GoogleCalendarService {

    private final UserRepository userRepository;

    private  final GoogleCalendar googleCalendar;
    @Override
    public ResponseDto authorizeUrl() throws GeneralSecurityException, IOException {

        String url = googleCalendar.authorizeUrl();

        Map<String, String> responseMap = new HashMap<String, String>();
        responseMap.put("url", url);

        return ResponseDto
                .builder()
                .code(HttpStatus.OK.value())
                .message("The authorization url was generated correctly")
                .data(responseMap)
                .build();
    }

    @Override
    public ResponseDto registerEvent(GoogleCalendarDto googleCalendarDto) {

        Optional<User> optUser = userRepository.findById(googleCalendarDto.getUserId());

        if(optUser.isEmpty()){
            return  null;
        }

        googleCalendar.regitserEvent();

        return ResponseDto
                .builder()
                .code(HttpStatus.OK.value())
                .message("The event was successfully registered")
                .data(null)
                .build();
    }
}
