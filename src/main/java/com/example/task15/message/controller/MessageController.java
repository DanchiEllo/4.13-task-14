package com.example.task15.message.controller;

import com.example.task15.message.model.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class MessageController {

    private List<Message> messages = new ArrayList<>(Arrays.asList(
            new Message(1, "Приветствие", "Приветствую вас, ребята!", LocalDateTime.of(2023, 9, 6, 12, 30, 15)),
            new Message(2, "Взаимное приветствие", "И мы вас!", LocalDateTime.of(2023, 9, 6, 12, 31, 37)),
            new Message(3, "Задание", "Сегодня вы должны связать шапку, ведь скоро зима.", LocalDateTime.of(2023, 9, 6, 12, 33, 4)),
            new Message(4, "Прощание", "Вы все молодцы! Умнички!", LocalDateTime.of(2023, 9, 6, 19, 12, 17))
    ));

    // Get requests
    @GetMapping("/messages")
    public Iterable<Message> getMessages() {
        return messages;
    }

    @GetMapping("/message/{id}")
    public Optional<Message> findMessageById(@PathVariable int id) {
        return messages.stream().filter(p -> p.getId() == id).findAny();
    }



    // Post requests
    @PostMapping("/messages")
    public ResponseEntity<Object> addMessage(@RequestBody Message message) {
        int newId = messages.stream().mapToInt(Message::getId).max().orElse(0) + 1;
        message.setId(newId);
        messages.add(message);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }



    // Put requests
    @PutMapping("/messages/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable int id, @RequestBody Message message) {
        int index = -1;
        for (Message m : messages) {
            if (m.getId() == id) {
                index = messages.indexOf(m);
                break;
            }
        }

        if (index == -1) {
            Message newMessage = new Message(id, message.getTitle(), message.getText(), message.getTime());
            messages.add(newMessage);
            return new ResponseEntity<>(newMessage, HttpStatus.CREATED);
        } else {
            Message updatedMessage = new Message(id, message.getTitle(), message.getText(), message.getTime());
            messages.set(index, updatedMessage);
            return new ResponseEntity<>(updatedMessage, HttpStatus.OK);
        }
    }


    // Delete requests
    @DeleteMapping("/messages/{id}")
    public void deleteMessage(@PathVariable int id) {
        messages.removeIf(p -> p.getId() == id);
    }

}