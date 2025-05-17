package com.instanote.esp;

import com.instanote.esp.requests.McqRequest;
import com.instanote.utils.TestGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instanote.requests.NotesRequest;
import com.instanote.utils.JwtUtil;
import com.instanote.utils.NotesGenerator;
import com.instanote.esp.models.Notes;
import com.instanote.esp.repository.NotesRepository;
import com.instanote.esp.repository.UserRepository;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class ApiController {

    @Autowired
    NotesRepository notesRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping(value = "/generate-mcqs", produces = "application/json")
    public String generateMcqs(@RequestBody McqRequest mcqRequest) {
        TestGenerator testGenerator = new TestGenerator(mcqRequest.getUrl(), mcqRequest.getNumQuestions(), mcqRequest.isPlaylist());
        String mcqs = testGenerator.generateMCQs();
        return mcqs;
    }

    @PostMapping(value = "/generate-notes", produces = "application/json")
    public ResponseEntity<?> generateNotes(@RequestBody NotesRequest notesRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String bearerToken = null;
        String username = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            bearerToken = authorizationHeader.substring(7); 
            username = JwtUtil.extractUsername(bearerToken);
        }
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        if (username == null || !userRepository.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized - user not found");
        }
        NotesGenerator notesGenerator = new NotesGenerator(notesRequest.getUrl());
        String notes = notesGenerator.generateNotes();
        Notes note = new Notes();
        note.setUsername(username);
        note.setNotes(notes);
        notesRepository.save(note);
        note = notesRepository.findById(note.getId()).orElse(null);
        return ResponseEntity.ok(note);
    }

    @GetMapping("/notes")
    public ResponseEntity<?> getAllNotes(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String username = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String bearerToken = authorizationHeader.substring(7);
            username = JwtUtil.extractUsername(bearerToken);
        }

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        if (username == null || !userRepository.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized - user not found");
        }

        List<Notes> userNotes = notesRepository.findByUsername(username);
        return ResponseEntity.ok(userNotes);
    }


    @GetMapping("/notes/{id}")
    public ResponseEntity<?> getNoteById(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String username = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String bearerToken = authorizationHeader.substring(7);
            username = JwtUtil.extractUsername(bearerToken);
        }

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        if (username == null || !userRepository.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized - user not found");
        }

        Notes note = notesRepository.findById(id).orElse(null);
        if (note == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note not found");
        }

        if (!note.getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        return ResponseEntity.ok(note);
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<?> deleteNoteById(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String username = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String bearerToken = authorizationHeader.substring(7);
            username = JwtUtil.extractUsername(bearerToken);
        }
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        if (username == null || !userRepository.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized - user not found");
        }

        Notes note = notesRepository.findById(id).orElse(null);
        if (note == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note not found");
        }
        if (!note.getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        notesRepository.delete(note);
        return ResponseEntity.ok("Note deleted successfully");
    }

}