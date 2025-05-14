package com.instanote.esp;

import com.instanote.utils.TestGenerator;

import com.instanote.requests.McqRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {
    @PostMapping(value = "/generate-mcqs", produces = "application/json")
    public String generateMcqs(@RequestBody McqRequest mcqRequest) {
        TestGenerator testGenerator = new TestGenerator(mcqRequest.getUrl(), mcqRequest.getNumQuestions(), mcqRequest.isPlaylist());
        String mcqs = testGenerator.generateMCQs();
        return mcqs;
    }
}