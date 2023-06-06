package com.jg.redditjavaproject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class RedditController {
    @Value("${authorizationUrl}")
    private String authorizationUrl;
    @Value("${clientId}")
    private String clientId;
    @Value("${clientSecret}")
    private String clientSecret;
    @Value("${redirectUri}")
    private String redirectUri;
    @Value("${state}")
    private String state;
    @Value("${scope}")
    private String scope;
    @Value("${duration}")
    private String duration;
    @Value("${responseType}")
    private String responseType;
    private String access_token;
    private String access_pass;


    public String getAccess_pass() {
        return access_pass;
    }

    public String getState() {
        return state;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public RedirectView auth_reddit(){
        try {
            String url = new URIBuilder(authorizationUrl)
                    .addParameter("client_id", clientId)
                    .addParameter("redirect_uri", redirectUri)
                    .addParameter("state", state)
                    .addParameter("scope", scope)
                    .addParameter("duration", duration)
                    .addParameter("response_type", responseType)
                    .build()
                    .toString();
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl(url);
            return redirectView;
        }catch(URISyntaxException e){
            System.out.println(e.toString());
            return null;
        }
    }

    public String retrieveAccessToken(){
        try {
            URL url = new URL("https://www.reddit.com/api/v1/access_token");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "com.jg.redditapi");
            connection.setRequestProperty("Authorization", "Basic " + getEncodedCredentials(clientId, clientSecret));
            connection.setDoOutput(true);

            // Set the request body
            String requestBody = String.format("grant_type=authorization_code&code=%s&redirect_uri=http://127.0.0.1:8080/auth_redirect",access_token);
            byte[] requestBodyBytes = requestBody.getBytes(StandardCharsets.UTF_8);
            connection.setRequestProperty("Content-Length", String.valueOf(requestBodyBytes.length));
            connection.getOutputStream().write(requestBodyBytes);

            // Get the response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                System.out.println(response.toString());

                ObjectMapper objectMapper = new ObjectMapper();
                TokenResponse tokenResponse = objectMapper.readValue(response.toString(), TokenResponse.class);
                access_pass =  tokenResponse.getAccessToken();
                connection.disconnect();
                return "Authentication Successful!";

            } else {
                System.err.println("Request failed. Response Code: " + responseCode);
                connection.disconnect();
                return "Request failed. Response Code: " + responseCode;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }
    private static String getEncodedCredentials(String clientId, String clientSecret) {
        String credentials = clientId + ":" + clientSecret;
        byte[] encodedBytes = Base64.getEncoder().encode(credentials.getBytes(StandardCharsets.UTF_8));
        return new String(encodedBytes, StandardCharsets.UTF_8);
    }
    public String getAccess_token() {
        return access_token;
    }

    public String getUpvotedPosts(){
        String endpoint = "https://oauth.reddit.com/user/spartran21/upvoted/.json";
        String accessToken = access_pass;

        HttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet(endpoint);

        request.setHeader("Authorization", "Bearer " + accessToken);
        request.setHeader("User-Agent", "com.jg.redditapi");
        try {
            HttpResponse response = httpClient.execute(request);

            String responseBody = EntityUtils.toString(response.getEntity());

            System.out.println(responseBody);
            return responseBody;

        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }
    public String post(PostClass post) throws UnsupportedEncodingException {
        try {
            String apiUrl = "https://oauth.reddit.com/api/submit";

            String accessToken = access_pass;

            String subreddit = post.getSubreddit();
            String title = post.getTitle();
            String content = post.getText();

            String payload = String.format("sr=%s&title=%s&text=%s&kind=self", subreddit, title, content);

            URL url = new URL(apiUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");

            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("User-Agent", "com.jg.redditapi");

            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(payload.getBytes());
            outputStream.flush();

            int responseCode = connection.getResponseCode();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            System.out.println("Response code: " + responseCode);
            System.out.println("Response body: " + response.toString());
            String res = "Respone code: "+ responseCode + "\nRsponse body: " + response.toString();

            connection.disconnect();
            return res;

        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}
class TokenResponse{
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String token_type;

    @JsonProperty("expires_in")
    private String expires_in;

    @JsonProperty("refresh_token")
    private String refresh_token;

    @JsonProperty("scope")
    private String scope;

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}

