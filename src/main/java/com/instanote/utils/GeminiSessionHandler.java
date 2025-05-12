package com.instanote.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.cdimascio.dotenv.Dotenv;

public class GeminiSessionHandler {
    private JSONObject sessionConvo = new JSONObject(); 
    private HttpClient client = HttpClient.newHttpClient();
    GeminiSessionHandler() {
        sessionConvo.put("contents", new JSONArray());
    }

    private static String loadAPIKey() {
        String apiKey;
        try {
            Dotenv dotenv = Dotenv.load();
            apiKey = dotenv.get("GEMINI_API_KEY");
        } catch (Exception e) {
            apiKey = System.getenv("GEMINI_API_KEY");
        }
        return apiKey;
    }

    public void sendMessage(String message) {
        JSONObject messageObj = new JSONObject();
        messageObj.put("role", "user");
        messageObj.put("parts", new JSONArray());
        messageObj.getJSONArray("parts").put(new JSONObject().put("text", message));
        sessionConvo.getJSONArray("contents").put(messageObj);
        String requestUrl = String.format("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=%s", loadAPIKey());
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(requestUrl))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(sessionConvo.toString()))
            .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject responseObj = new JSONObject(response.body()).getJSONArray("candidates").getJSONObject(0);
            System.out.println("Response: " + responseObj.toString());
            JSONObject content = responseObj.getJSONObject("content");
            sessionConvo.getJSONArray("contents").put(content);
            System.out.println("Response: " + content.getJSONArray("parts").getJSONObject(0).get("text").toString());
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public JSONObject getSessionConvo(){
        return sessionConvo;
    }

}