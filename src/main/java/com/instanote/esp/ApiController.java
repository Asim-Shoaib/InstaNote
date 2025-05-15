package com.instanote.esp;

import com.instanote.esp.requests.McqRequest;
import com.instanote.utils.TestGenerator;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instanote.requests.NotesRequest;
import com.instanote.utils.NotesGenerator;

@RestController
@RequestMapping("/api")
public class ApiController {
    @PostMapping(value = "/generate-mcqs", produces = "application/json")
    public String generateMcqs(@RequestBody McqRequest mcqRequest) {
        TestGenerator testGenerator = new TestGenerator(mcqRequest.getUrl(), mcqRequest.getNumQuestions(), mcqRequest.isPlaylist());
        String mcqs = testGenerator.generateMCQs();
        return mcqs;
    }

    @PostMapping(value = "/generate-notes", produces = "application/json")
    public String generateNotes(@RequestBody NotesRequest notesRequest) {
        NotesGenerator notesGenerator = new NotesGenerator(notesRequest.getUrl());
        String notes = notesGenerator.generateNotes();
        return notes;
    }
}