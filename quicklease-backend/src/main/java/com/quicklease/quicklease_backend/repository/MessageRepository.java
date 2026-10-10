package com.quicklease.quicklease_backend.repository;

import com.quicklease.quicklease_backend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySender_EmailAndReceiver_EmailOrSender_EmailAndReceiver_EmailOrderBySentAtAsc(
            String email1, String email2, String email3, String email4);

    List<Message> findBySender_EmailOrReceiver_EmailOrderBySentAtDesc(String email1, String email2);
}