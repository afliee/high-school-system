package com.highschool.highschoolsystem.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssignmentWebSocketController {

    @MessageMapping("/assignment/create")
    @SendTo("/topic/assignment/create")
    public String createAssignment(String message) {
        return message;
    }
}
