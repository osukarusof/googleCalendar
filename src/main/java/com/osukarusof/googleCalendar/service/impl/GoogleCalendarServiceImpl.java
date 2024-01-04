package com.osukarusof.googleCalendar.service.impl;

import com.osukarusof.googleCalendar.integration.GoogleCalendar;
import com.osukarusof.googleCalendar.service.GoogleCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleCalendarServiceImpl implements GoogleCalendarService {
    
    private  final GoogleCalendar googleCalendar;
    @Override
    public String authorizeUrl() {
        return null;
    }
}
