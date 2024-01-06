package com.osukarusof.googleCalendar.integration;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.CalendarScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Component
public class GoogleCalendar {

    @Value("${google.calendar.credentials.file.path}")
    private  String CREDENTIALS_FILE_PATH;

    @Value("${google.calendar.redirect.url}")
    private  String  REDIRECT_URL;

    private GoogleAuthorizationCodeFlow googleFlow;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_EVENTS);

    /**
     * It returns the url for authorization
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public String authorizeUrl () throws GeneralSecurityException, IOException {

        return authorizationFlow()
                .newAuthorizationUrl()
                .setRedirectUri(REDIRECT_URL)
                .build();
    }

    /**** FUNCTIONS NECESSARY FOR IMPLEMENTATION ****/

    /**
     * Allows us to return authorization flows
     * @return GoogleAuthorizationCodeFlow
     * @throws IOException
     * @throws GeneralSecurityException
     */
    private GoogleAuthorizationCodeFlow authorizationFlow() throws IOException, GeneralSecurityException {

        InputStream in = GoogleCalendar.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        return new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                clientSecrets,
                SCOPES)
                .setAccessType("offline")
                .build();
    }

}
