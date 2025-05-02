package com.instanote.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.thoroldvix.api.Transcript;
import io.github.thoroldvix.api.TranscriptContent;
import io.github.thoroldvix.api.TranscriptFormatters;
import io.github.thoroldvix.api.YoutubeTranscriptApi;
import io.github.thoroldvix.internal.TranscriptApiFactory;
import io.github.thoroldvix.api.TranscriptFormatter;

class TranscriptionHandler {
    private static YoutubeTranscriptApi youtubeTranscriptApi = TranscriptApiFactory.createDefault();
    public static String extractVideoId(String videoUrl) {
        String pattern = "(?<=watch\\?v=|/videos/|embed/|youtu\\.be/|/v/|watch\\?v%3D|%2Fvideos%2F|embed%2F|youtu\\.be%2F|%2Fv%2F)[^#&?\\n]*";
        Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compiledPattern.matcher(videoUrl);
        if (matcher.find()) {
            System.out.println("Video ID: " + matcher.group());
            return matcher.group();
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
            throw new Exception("Invalid video URL: " + videoUrl);
        }
    }
}
