package com.osukarusof.googleCalendar.service;

import com.osukarusof.googleCalendar.dto.GoogleCalendarDto;
import com.osukarusof.googleCalendar.dto.ResponseDto;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface GoogleCalendarService {
    public ResponseDto authorizeUrl() throws GeneralSecurityException, IOException;

    public ResponseDto registerEvent(GoogleCalendarDto googleCalendarDto, String code) throws GeneralSecurityException, IOException;

    public ResponseDto deleteEvent(Long calendarUserId) throws GeneralSecurityException, IOException;
}
