package com.osukarusof.googleCalendar.service.impl;

import com.google.api.services.calendar.model.Event;
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

    private  final GoogleCalendar googleCalendar;
    @Override
    public ResponseDto authorizeUrl() throws GeneralSecurityException, IOException {
        Map<String, String> responseMap = new HashMap<String, String>();
        responseMap.put("url", googleCalendar.authorizeUrl());

        return ResponseDto
                .builder()
                .code(HttpStatus.OK.value())
                .message("The authorization url was generated correctly")
                .data(responseMap)
                .build();
    }

    @Override
    public ResponseDto registerEvent(GoogleCalendarDto googleCalendarDto, String code) throws GeneralSecurityException, IOException {

        Event register = googleCalendar.registerGoogleCalendarEvent(googleCalendarDto, code);

        return ResponseDto
                .builder()
                .code(HttpStatus.OK.value())
                .message("The event was successfully registered")
                .data(register)
                .build();
    }

    @Override
    public ResponseDto updateEvent(Long calendarUserId, GoogleCalendarDto googleCalendarDto) throws GeneralSecurityException, IOException {

        Event update = googleCalendar.updateGoogleCalendarEvent(calendarUserId, googleCalendarDto);

        return ResponseDto
                .builder()
                .code(HttpStatus.OK.value())
                .message("The event was successfully updated")
                .data(update)
                .build();
    }

    @Override
    public ResponseDto deleteEvent(Long calendarUserId) throws GeneralSecurityException, IOException {

        googleCalendar.deleteGoogleCalendarEvent(calendarUserId);

        return ResponseDto
                .builder()
                .code(HttpStatus.OK.value())
                .message("event was successfully deleted")
                .build();
    }
}
