package com.instanote.utils;


public class TestGenerator {
    private static String prompt = 
            """
                You are a helpful assistant. 
                Your task is to generate notes based on the provided transcript of a YouTube video or the notes generated from it. 
                The quiz should be designed to give the viewer the best understanding of the course material.
                All the que should be focused at judging the viewer's understanding of the video not his memorization skills (unless you believe that its necessary).
                Each question should have one correct answer and three distractors. 
                The questions should be clear, concise, and relevant to the video's topic. 
                Questions should be of intermediate difficulty level.
                All MCQs must be in English, no matter the language of the transcript/notes.
                The MCQs are to be shown in a webapp so provide a structured answer in JSON format
                [
                    {"question":"1. ...", "choices":[{"id":"a", "text":"...", "isCorrect":"true"}, {"id":"B",. .....}], ....
                ]
            """
    ;

    private String transcriptText;

    private GeminiSessionHandler geminiSessionHandler;

    public TestGenerator(String notes, int numQuestions) {
        geminiSessionHandler = new GeminiSessionHandler();
        prompt = prompt.concat(String.format("Notes: %s\n", notes));
        prompt = prompt.concat(String.format("For this tasks, generate %d MCQs based on the notes that i generated from the video and make sure to follow the above instructions.\n", numQuestions));
    }
    
    public TestGenerator(String videoUrl, int numQuestions, boolean isPlaylist) {
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
        return extractExactJson(response);
    }

    public static String extractExactJson (String text){
        String validJson = text;
        validJson = validJson.replace("```json", "");
        validJson = validJson.replace("`", "");
        return validJson;
    }
    
}
