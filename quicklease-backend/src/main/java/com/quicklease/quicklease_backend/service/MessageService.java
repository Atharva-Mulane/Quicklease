package com.quicklease.quicklease_backend.service;

import com.quicklease.quicklease_backend.dto.MessageResponse;
import com.quicklease.quicklease_backend.dto.SendMessageRequest;
import com.quicklease.quicklease_backend.model.Message;
import com.quicklease.quicklease_backend.model.User;
import com.quicklease.quicklease_backend.repository.MessageRepository;
import com.quicklease.quicklease_backend.repository.UserRepository;
import com.quicklease.quicklease_backend.model.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    NotificationService notificationService;

    public MessageResponse sendMessage(SendMessageRequest request, String senderEmail) {

        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

        User receiver = userRepository.findByEmail(request.getReceiverEmail())
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        if (sender.getEmail().equals(receiver.getEmail())) {
            throw new IllegalArgumentException("You cannot message yourself");
        }

        Message message = new Message(sender, receiver, request.getContent());
        Message saved = messageRepository.save(message);

        notificationService.createNotification(receiver, NotificationType.NEW_MESSAGE,
            sender.getFullName() + " sent you a message");

        return mapToResponse(saved);
    }

    public List<MessageResponse> getConversation(String myEmail, String otherEmail) {
        return messageRepository.findBySender_EmailAndReceiver_EmailOrSender_EmailAndReceiver_EmailOrderBySentAtAsc(
                        myEmail, otherEmail, otherEmail, myEmail)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<MessageResponse> getMyMessages(String myEmail) {
        return messageRepository.findBySender_EmailOrReceiver_EmailOrderBySentAtDesc(myEmail, myEmail)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private MessageResponse mapToResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getSender().getEmail(),
                message.getReceiver().getEmail(),
                message.getContent(),
                message.getIsRead(),
                message.getSentAt()
        );
    }
}