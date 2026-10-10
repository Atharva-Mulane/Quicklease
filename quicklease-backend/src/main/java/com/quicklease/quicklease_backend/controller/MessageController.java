package com.quicklease.quicklease_backend.controller;

import com.quicklease.quicklease_backend.dto.MessageResponse;
import com.quicklease.quicklease_backend.dto.SendMessageRequest;
import com.quicklease.quicklease_backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody SendMessageRequest request, Authentication authentication) {
        try {
            MessageResponse response = messageService.sendMessage(request, authentication.getName());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/conversation/{otherEmail}")
    public ResponseEntity<List<MessageResponse>> getConversation(@PathVariable String otherEmail, Authentication authentication) {
        return ResponseEntity.ok(messageService.getConversation(authentication.getName(), otherEmail));
    }

    @GetMapping
    public ResponseEntity<List<MessageResponse>> getMyMessages(Authentication authentication) {
        return ResponseEntity.ok(messageService.getMyMessages(authentication.getName()));
    }
}