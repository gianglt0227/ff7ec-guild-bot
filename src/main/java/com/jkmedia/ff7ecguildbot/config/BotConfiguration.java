package com.jkmedia.ff7ecguildbot.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfiguration {

  /**
   * Global instance of the scopes required by this quickstart. If modifying these scopes, delete
   * your previously saved tokens/ folder.
   */
  private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);

  private static final String APPLICATION_NAME = "FF7EC Guild Bot";

  /**
   * Creates an authorized Credential object.
   *
   * @return An authorized Credential object.
   * @throws IOException If the credentials.json file cannot be found.
   */
  @Bean
  public Credential getCredentials(
      NetHttpTransport netHttpTransport,
      JsonFactory jsonFactory,
      @Value("${google.credential}") String googleCredential)
      throws IOException {
    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(
            jsonFactory, new InputStreamReader(IOUtils.toInputStream(googleCredential)));

    GoogleAuthorizationCodeFlow flow =
        new GoogleAuthorizationCodeFlow.Builder(
                netHttpTransport, jsonFactory, clientSecrets, SCOPES)
            .setDataStoreFactory(new MemoryDataStoreFactory())
            .setAccessType("offline")
            .build();
    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
  }

  @Bean
  public Sheets sheet(
      NetHttpTransport netHttpTransport, JsonFactory jsonFactory, Credential googleCredential) {
    return new Sheets.Builder(netHttpTransport, jsonFactory, googleCredential)
        .setApplicationName(APPLICATION_NAME)
        .build();
  }

  @Bean
  public NetHttpTransport netHttpTransport() throws GeneralSecurityException, IOException {
    return GoogleNetHttpTransport.newTrustedTransport();
  }

  @Bean
  public JsonFactory jsonFactory() {
    return GsonFactory.getDefaultInstance();
  }
}
