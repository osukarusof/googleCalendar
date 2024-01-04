package com.osukarusof.googleCalendar.service;

import com.osukarusof.googleCalendar.dto.ResponseDto;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface GoogleCalendarService {
    public ResponseDto authorizeUrl() throws GeneralSecurityException, IOException;
}
