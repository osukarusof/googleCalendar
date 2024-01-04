package com.osukarusof.googleCalendar.service.impl;

import com.osukarusof.googleCalendar.integration.GoogleCalendar;
import com.osukarusof.googleCalendar.service.GoogleCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
@RequiredArgsConstructor
public class GoogleCalendarServiceImpl implements GoogleCalendarService {
    
    private  final GoogleCalendar googleCalendar;
    @Override
    public String authorizeUrl() throws GeneralSecurityException, IOException {

        return googleCalendar.authorizeUrl();
    }
}
