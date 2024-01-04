package com.osukarusof.googleCalendar.service;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface GoogleCalendarService {
    public String authorizeUrl() throws GeneralSecurityException, IOException;
}
