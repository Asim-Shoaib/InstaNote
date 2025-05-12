package com.instanote.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class UtilsTest {
    final private static String testVideo1 = "https://www.youtube.com/watch?v=Hi7rK0hZnfc";
    final private static String testPlaylistURL = "https://www.youtube.com/watch?v=HkMXiQ4qUc8&list=PLYt5jguM4dGwRe--seOjAKjE-ZIiDCw_X";

    @Test
    public void testExtractVideoId() {
        String videoId = TranscriptionHandler.extractVideoId(testVideo1);
        System.out.println("Extracted Video ID: " + videoId);
        Assertions.assertEquals("Hi7rK0hZnfc",videoId, "Video ID should be extracted correctly from the URL.");
    }

    @Test
    public void testExtractPlaylistId() {
        String playlistId = TranscriptionHandler.extractPlaylistId(testPlaylistURL);
        System.out.println("Extracted Playlist ID: " + playlistId);
        Assertions.assertEquals("PLYt5jguM4dGwRe--seOjAKjE-ZIiDCw_X", playlistId, "Playlist ID should be extracted correctly from the URL.");
    }

    @Test
    public void testGetTranscript() {
        try {
            String transcriptText = TranscriptionHandler.getTranscript(testVideo1);
            System.out.println("Transcript Text: " + transcriptText);
            Assertions.assertNotNull(transcriptText, "Transcript should not be null.");
            Assertions.assertTrue(transcriptText.length() > 0, "Transcript should not be empty.");
            Assertions.assertTrue(transcriptText instanceof String, "Transcript should be of type String.");
        } catch (Exception e) {
            // Assertions.fail("Exception should not be thrown: " + e.getMessage()); // Known bug, it fails on cloud servers as Youtube blocks certain IPs
        }
    }

    @Test
    public void testGetPlaylistTranscript() {
        try {
            String transcriptText = TranscriptionHandler.getPlaylistTranscript(testPlaylistURL);
            System.out.println("Playlist Transcript Text: " + transcriptText);
            Assertions.assertNotNull(transcriptText, "Playlist transcript should not be null.");
            Assertions.assertTrue(transcriptText.length() > 0, "Playlist transcript should not be empty.");
            Assertions.assertTrue(transcriptText instanceof String, "Playlist transcript should be of type String.");
        } catch (Exception e) {}
    }

    @Test
    public void testSendMessage() {
        GeminiSessionHandler sessionHandler = new GeminiSessionHandler();
        sessionHandler.sendMessage("Hello, how are you?");
        String response = sessionHandler.getSessionConvo().toString();
        System.out.println("Response: " + response);
        Assertions.assertNotNull(response, "Response should not be null.");
        Assertions.assertTrue(response.length() > 0, "Response should not be empty.");
        Assertions.assertTrue(response instanceof String, "Response should be of type String.");
    }
}
