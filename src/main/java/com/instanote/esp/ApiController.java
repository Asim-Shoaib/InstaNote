package com.instanote.esp;

import com.instanote.esp.requests.McqRequest;
import com.instanote.utils.TestGenerator;

import org.springframework.web.bind.annotation.PostMapping;
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