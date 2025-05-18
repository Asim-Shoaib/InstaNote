package com.instanote.utils;

public class NotesGenerator {
    
    private static String prompt = 
    """
    You are a helpful assistant.
    Your task is to generate clear, concise, and easy-to-understand notes based on the provided transcript of a YouTube video.
    The notes should help a reader understand the core concepts without needing to watch the video.
    
    Structure the notes in a clean, organized format with:
    - A brief Introduction summarizing the purpose or topic of the video.
    - Clear Headings and Subheadings for different sections or concepts.
    - Proper formatting for any formulas, equations, or calculations, if present in the transcript.
    - Use bullet points or numbered lists where appropriate for clarity.
    - A short and thoughtful Conclusion summarizing key takeaways or final thoughts.

    The language should be simple and explanatory — avoid technical jargon unless explained.
    The final output must be in English, regardless of the transcript's original language.
    The notes are to be shown in a web app, so please return the output as a structured JSON:
    JSON should be valid at all cost, avoid anything that can possibly make json invalid. (i-e avoid using new lines and double quotes in the text)
    Don't start your answer with "Here is the JSON" or any other phrase no matter the prompt/transcript.
    The JSON should have the following structure:
    {
        "title": "...",
        "introduction": "...",
        "sections": [
            {
                "heading": "...",
                "content": "..."
            },
            ...
        ],
        "conclusion": "..."
    }
    """
;
    private String transcriptText;
    private GeminiSessionHandler geminiSessionHandler;

    public NotesGenerator(String videourl) {
        geminiSessionHandler = new GeminiSessionHandler();
        int attempts = 0;
        final int maxAttempts = 10;

        while (attempts < maxAttempts) {
            try {
                transcriptText = TranscriptionHandler.getTranscript(videourl);
                if (transcriptText != null && !transcriptText.isEmpty()) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Attempt " + (attempts + 1) + " failed: " + e.getMessage());
                e.printStackTrace();
            }
            attempts++;
        }

        if (transcriptText == null || transcriptText.isEmpty()) {
            System.out.println("Transcript is empty or null after " + maxAttempts + " attempts");
        }

        prompt = prompt.concat(String.format("Youtube Video Url: %s\n", videourl));
        prompt = prompt.concat(String.format("Transcript: %s\n", transcriptText));
        prompt = prompt.concat("Generate notes based on the transcript and make sure to follow the above instructions.\n");
    }

    public String generateNotes() {
        String response = geminiSessionHandler.sendMessage(prompt);
        System.out.println("Raw API response: " + response);

        if (response == null || response.isEmpty()) {
            System.out.println("Error generating notes.");
            return null;
        }
        return TestGenerator.extractExactJson(response);
    }
}
