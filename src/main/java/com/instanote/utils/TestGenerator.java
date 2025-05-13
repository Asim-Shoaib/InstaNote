package com.instanote.utils;


public class TestGenerator {
    private static String prompt = 
            """
                You are a helpful assistant. 
                Your task is to generate multiple-choice questions (MCQs) based on the provided transcript of a YouTube video. 
                The MCQs should be designed to test the viewer's understanding of the video's content.
                Each question should have one correct answer and three distractors. 
                The questions should be clear, concise, and relevant to the video's topic. 
                Questions should be of intermediate difficulty level.
                All MCQs must be in English, no matter the language of the transcript.
                The MCQs are to be shown in a webapp so provide a structured answer in JSON format
                [
                    {"question":"1. ...", "choices":[{"id":"a", "text":"...", "isCorrect":"true"}, {"id":"B",. .....}], ....
                ]
            """
    ;

    private String videoUrl;
    private int numQuestions;
    private String transcriptText;

    private GeminiSessionHandler geminiSessionHandler = new GeminiSessionHandler();
    

    public TestGenerator(String videoUrl, int numQuestions, boolean isPlaylist) {
        this.videoUrl = videoUrl;
        this.numQuestions = numQuestions;
        if (isPlaylist) {
            try {
                transcriptText = TranscriptionHandler.getPlaylistTranscript(videoUrl);
            } catch (Exception e) {
                System.out.println("Error fetching playlist transcript: " + e.getMessage());
                e.printStackTrace();
            }
        }
        else {
            try {
                transcriptText = TranscriptionHandler.getTranscript(videoUrl);
            } catch (Exception e) {
                System.out.println("Error fetching transcript: " + e.getMessage());
                e.printStackTrace();
            }
        }
        if (transcriptText == null || transcriptText.isEmpty()) {
            System.out.println("Transcript is empty or null.");
            return;
        }
        prompt = prompt.concat(String.format("Transcript: %s\n", transcriptText));
        prompt = prompt.concat(String.format("Generate %d MCQs based on the transcript and make sure to follow the above instructions.\n", numQuestions));
    }

    public String generateMCQs() {
        String response = geminiSessionHandler.sendMessage(prompt);
        if (response == null || response.isEmpty()) {
            System.out.println("Error generating MCQs.");
            return null;
        }
        return this.extractExactJson(response);
    }

    private String extractExactJson (String text){
        String validJson = text
                            .replace("\\\"", "\"")
                            .replace("\\n", "\n")
                            .replace("\\t", "\t");

        validJson = validJson.replace("```json", "");
        validJson = validJson.replace("`", "");
        return validJson;
    }
    
}
