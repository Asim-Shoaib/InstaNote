package com.instanote.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.thoroldvix.api.Transcript;
import io.github.thoroldvix.api.TranscriptContent;
import io.github.thoroldvix.api.TranscriptFormatters;
import io.github.thoroldvix.api.TranscriptRequest;
import io.github.thoroldvix.api.YoutubeTranscriptApi;
import io.github.thoroldvix.internal.TranscriptApiFactory;
import io.github.thoroldvix.api.TranscriptFormatter;

class TranscriptionHandler {
    private static Dotenv dotenv = Dotenv.load();
    private static final String YOUTUBE_API_KEY = dotenv.get("YOUTUBE_API_KEY");
    private static YoutubeTranscriptApi youtubeTranscriptApi = TranscriptApiFactory.createDefault();
    public static String extractVideoId(String videoUrl) {
        String pattern = "(?<=watch\\?v=|/videos/|embed/|youtu\\.be/|/v/|watch\\?v%3D|%2Fvideos%2F|embed%2F|youtu\\.be%2F|%2Fv%2F)[^#&?\\n]*";
        Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compiledPattern.matcher(videoUrl);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

    public static String extractPlaylistId(String videoUrl) {
        String pattern = "[?&]list=([A-Za-z0-9_-]+)";
        Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compiledPattern.matcher(videoUrl);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    public static String getTranscript(String videoUrl) throws Exception {
        String videoId = extractVideoId(videoUrl);
        String transcriptText = "";
        if (videoId != null) {
            videoId = videoId.strip();
            Transcript transcript = youtubeTranscriptApi.listTranscripts(videoId).findTranscript("en", "hi" );
            TranscriptContent transcriptContent = transcript.fetch();
            TranscriptFormatter formatter = TranscriptFormatters.textFormatter();
            transcriptText = formatter.format(transcriptContent);
            return transcriptText;
        }
        else {
            throw new IllegalArgumentException("Invalid video URL: " + videoUrl);
        }
    }

    public static String getPlaylistTranscript(String playlistUrl) throws Exception {
        String playlistId = extractPlaylistId(playlistUrl);
        String transcriptText = "";
        if (playlistId != null) {
            TranscriptRequest request = new TranscriptRequest(YOUTUBE_API_KEY, false);
            Map<String, TranscriptContent> transcripts = youtubeTranscriptApi.getTranscriptsForPlaylist(playlistId, request, "en", "hi");
            TranscriptFormatter formatter = TranscriptFormatters.textFormatter();
            int videoCount = 1;
            for (Map.Entry<String, TranscriptContent> entry : transcripts.entrySet()) {
                TranscriptContent transcriptContent = entry.getValue();
                transcriptText += "\nPart " + videoCount + ":\n" + formatter.format(transcriptContent) + "\n\n";
                videoCount++;
            }
            return transcriptText;
        }
        else {
            throw new IllegalArgumentException("Invalid playlist URL: " + playlistUrl);
        }
    }
    
}
