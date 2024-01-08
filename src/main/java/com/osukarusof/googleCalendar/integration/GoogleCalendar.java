package com.osukarusof.googleCalendar.integration;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.osukarusof.googleCalendar.dto.GoogleCalendarDto;
import com.osukarusof.googleCalendar.entity.CalendarUser;
import com.osukarusof.googleCalendar.entity.User;
import com.osukarusof.googleCalendar.entity.UserToken;
import com.osukarusof.googleCalendar.exception.BadRequestException;
import com.osukarusof.googleCalendar.exception.InternalServerError;
import com.osukarusof.googleCalendar.exception.NotFoundException;
import com.osukarusof.googleCalendar.repository.CalendaruserRepository;
import com.osukarusof.googleCalendar.repository.UserRepository;
import com.osukarusof.googleCalendar.repository.UserTokenRepostory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.GeneralSecurityException;
import java.time.ZoneId;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleCalendar {

    @Value("${google.calendar.credentials.file.path}")
    private String CREDENTIALS_FILE_PATH;

    @Value("${google.calendar.redirect.url}")
    private String  REDIRECT_URL;

    @Value("${google.calendar.id}")
    private String calendarId;

    @Value("${google.calendar.application.name}")
    private String APPLICATION_NAME;

    @Value("${google.calendar.credentials.acces.token.expire.seconds}")
    private Integer tokenExpiresInSeconds;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_EVENTS);

    private final UserTokenRepostory userTokenRepostory;

    private final UserRepository userRepository;

    private final CalendaruserRepository calendaruserRepository;


    /**
     * It returns the url for authorization
     * @return String
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public String authorizeUrl () throws GeneralSecurityException, IOException {
        return authorizationFlow()
                .newAuthorizationUrl()
                .setRedirectUri(REDIRECT_URL)
                .build();
    }

    public Event registerGoogleCalendarEvent(GoogleCalendarDto googleCalendarDto, String code) throws GeneralSecurityException, IOException {

        User user = getUser(googleCalendarDto.getUserId());

        Event event = new Event()
                .setSummary(googleCalendarDto.getTitle())
                .setLocation(googleCalendarDto.getLocation())
                .setDescription(googleCalendarDto.getDescription());

        Date startDate = Date.from(googleCalendarDto.getStartDateTime().atZone(ZoneId.systemDefault()).toInstant());
        DateTime startDateTime = new DateTime(startDate);
        EventDateTime start = new EventDateTime().setDateTime(startDateTime).setTimeZone("UTC");
        event.setStart(start);

        Date endDate = Date.from(googleCalendarDto.getEndDateTime().atZone(ZoneId.systemDefault()).toInstant());
        DateTime endDateTime = new DateTime(endDate);
        EventDateTime end = new EventDateTime().setDateTime(endDateTime).setTimeZone("UTC");
        event.setEnd(end);

        Event registerEvent = calendarService(code, googleCalendarDto.getUserId())
                .events()
                .insert(calendarId, event)
                .execute();

        CalendarUser calendarUser = new CalendarUser();
        calendarUser.setGoogleCalendarId(registerEvent.getId());
        calendarUser.setStartDateTime(googleCalendarDto.getStartDateTime());
        calendarUser.setEndDateTime(googleCalendarDto.getEndDateTime());
        calendarUser.setUser(user);

        calendaruserRepository.save(calendarUser);

        return registerEvent;
    }


    public  Event updateGoogleCalendarEvent(Long calendarUserId, GoogleCalendarDto googleCalendarDto) throws GeneralSecurityException, IOException {

        Optional<CalendarUser> optCalendarUser = calendaruserRepository.findById(calendarUserId);
        if(optCalendarUser.isEmpty()){
            throw new NotFoundException("Calendar event does not exist");
        }

        Event event = new Event()
                .setSummary(googleCalendarDto.getTitle())
                .setLocation(googleCalendarDto.getLocation())
                .setDescription(googleCalendarDto.getDescription());

        Date startDate = Date.from(googleCalendarDto.getStartDateTime().atZone(ZoneId.systemDefault()).toInstant());
        DateTime startDateTime = new DateTime(startDate);
        EventDateTime start = new EventDateTime().setDateTime(startDateTime).setTimeZone("UTC");
        event.setStart(start);

        Date endDate = Date.from(googleCalendarDto.getEndDateTime().atZone(ZoneId.systemDefault()).toInstant());
        DateTime endDateTime = new DateTime(endDate);
        EventDateTime end = new EventDateTime().setDateTime(endDateTime).setTimeZone("UTC");
        event.setEnd(end);

        Event updateEvent = calendarService(null, googleCalendarDto.getUserId())
                .events()
                .update(calendarId, optCalendarUser.get().getGoogleCalendarId(),  event)
                .execute();

        CalendarUser calendarUser = new CalendarUser();
        calendarUser.setId(optCalendarUser.get().getId());
        calendarUser.setGoogleCalendarId(updateEvent.getId());
        calendarUser.setStartDateTime(googleCalendarDto.getStartDateTime());
        calendarUser.setEndDateTime(googleCalendarDto.getEndDateTime());
        calendarUser.setUser(optCalendarUser.get().getUser());

        calendaruserRepository.save(calendarUser);

        return updateEvent;
    }

    public void deleteGoogleCalendarEvent(Long calendarUserId) throws GeneralSecurityException, IOException {

         Optional<CalendarUser> optCalendarUser = calendaruserRepository.findById(calendarUserId);
         if(optCalendarUser.isEmpty()){
             throw new NotFoundException("Calendar event does not exist");
         }

        GoogleCalendarDto googleCalendarDto = GoogleCalendarDto.builder().userId(optCalendarUser.get().getUser().getId()).build();

        calendarService(null, googleCalendarDto.getUserId())
                .events()
                .delete(calendarId, optCalendarUser.get().getGoogleCalendarId())
                .execute();

        calendaruserRepository.delete(optCalendarUser.get());
    }

    /**************** FUNCTIONS NECESSARY FOR IMPLEMENTATION ****************/

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

    /**
     * Allows us to return the calendar instance
     * @return Calendar
     * @throws GeneralSecurityException
     * @throws IOException
     */
    private Calendar calendarService(String code, Long userId) throws GeneralSecurityException, IOException {
        return new Calendar
                .Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, googleCredential(code, userId))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Allows us to obtain the user's credentials
     * @param code
     * @param userId
     * @return Credential
     * @throws GeneralSecurityException
     * @throws IOException
     */
    private Credential googleCredential(String code, Long userId) throws GeneralSecurityException, IOException {
        Optional<UserToken> optUserToken = userTokenRepostory.findByUserId(userId);
        if (optUserToken.isEmpty()){
            return  generateTokenWithCodeAuthorization(code, userId);
        }

        return generateTokenwithoutCodeAuthorization(optUserToken.get());
    }

    /**
     * The token is generated when the authorization code is sent
     * @param code
     * @return Credential
     * @throws GeneralSecurityException
     * @throws IOException
     */
    private Credential generateTokenWithCodeAuthorization(String code, Long userId) throws GeneralSecurityException, IOException {

        GoogleTokenResponse tokenResponse = null;
        User user = getUser(userId);

        try {
            tokenResponse = authorizationFlow().newTokenRequest(code).setRedirectUri(REDIRECT_URL).execute();
        }catch (TokenResponseException ex){
            log.error("There was a problem generating the token with Google", ex);
            throw new BadRequestException("There was a problem generating the token with Google");
        }catch (Exception ex){
            log.error("Ops sorry there was a mistake", ex);
            throw new InternalServerError("Ops sorry there was a mistake");
        }

        if(tokenResponse == null){
            throw new NotFoundException("No token was generated for google calendar");
        }

        UserToken userToken = new UserToken();
        userToken.setToken(tokenResponse.getAccessToken());
        userToken.setRefreshToken(tokenResponse.getRefreshToken());
        userToken.setExpiryTimeSeconds(tokenResponse.getExpiresInSeconds());
        userToken.setUser(user);

        userTokenRepostory.save(userToken);

        return authorizationFlow().createAndStoreCredential(tokenResponse, null);
    }

    /**
     * It allows us to obtain the credentials through the token
     * @param userToken
     * @return Credential
     */
    private Credential generateTokenwithoutCodeAuthorization(UserToken userToken) {

        Credential credential = null;

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(userToken.getToken());
        tokenResponse.setRefreshToken(userToken.getRefreshToken());
        tokenResponse.setExpiresInSeconds(userToken.getExpiryTimeSeconds());

        try {
            credential = authorizationFlow().createAndStoreCredential(tokenResponse, null);

            if (credential.getExpiresInSeconds() < tokenExpiresInSeconds) {
                credential.refreshToken();
                UserToken userToken_update = new UserToken();
                userToken_update.setId(userToken.getId());
                userToken_update.setToken(credential.getAccessToken());
                userToken_update.setRefreshToken(credential.getRefreshToken());
                userToken_update.setExpiryTimeSeconds(credential.getExpiresInSeconds());
                userToken_update.setUser(userToken.getUser());

                userTokenRepostory.save(userToken_update);
            }
        }catch (Exception ex){
            log.error("oops there was an error generating the credentials", ex);
        }

        if(credential == null){
            throw new NotFoundException("No credential was generated");
        }

        return credential;
    }

    /**
     * It allows us to validate if the user exists
     * @param userId
     * @return User
     * @throws NotFoundException
     */
    private User getUser(Long userId) throws NotFoundException{
        Optional<User> optUser = userRepository.findById(userId);
        if(optUser.isEmpty()){
            throw new NotFoundException("Username does not exist");
        }
        return optUser.get();
    }
}
