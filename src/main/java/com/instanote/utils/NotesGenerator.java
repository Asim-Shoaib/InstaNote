package com.instanote.utils;


public class NotesGenerator {
    
    private static String prompt = 
    """
    You are a helpful assistant.
    Your task is to generate clear, concise, and easy-to-understand notes based on the provided transcript of a YouTube video.
    The notes should help a reader understand the core concepts without needing to watch the video.
    
    Structure the notes in a clean, organized format with:
    - A brief **Introduction** summarizing the purpose or topic of the video.
    - Clear **Headings** and **Subheadings** for different sections or concepts.
    - Proper formatting for any **formulas**, **equations**, or **calculations**, if present in the transcript.
    - Use bullet points or numbered lists where appropriate for clarity.
    - A short and thoughtful **Conclusion** summarizing key takeaways or final thoughts.

    The language should be simple and explanatory — avoid technical jargon unless explained.
    The final output must be in English, regardless of the transcript's original language.
    The notes are to be shown in a web app, so please return the output as a structured JSON:
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
    private String videourl;
    private String transcriptText;
    private GeminiSessionHandler geminiSessionHandler = new GeminiSessionHandler();

    public NotesGenerator(String videourl) {

        this.videourl = videourl;

        try {
            transcriptText = TranscriptionHandler.getTranscript(videourl);

        } catch(Exception e) {
            System.out.println("Error fetching transcript: " + e.getMessage());
            e.printStackTrace();
        }

        if(transcriptText == null || transcriptText.isEmpty()) {
            System.out.println("Transcript is empty or null");
        }
        prompt = prompt.concat(String.format("Transcript: %s\n", transcriptText));
        prompt = prompt.concat("Generate notes based on the transcript and make sure to follow the above instructions.\n");

    }

    public String generateNotes() {
        String response = geminiSessionHandler.sendMessage(prompt);
        if (response == null || response.isEmpty()) {
            System.out.println("Error generating notes.");
            return null;
        }
        return TestGenerator.extractExactJson(response);
    }

}