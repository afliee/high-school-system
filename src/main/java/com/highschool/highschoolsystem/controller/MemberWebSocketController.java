package com.highschool.highschoolsystem.controller;

import com.highschool.highschoolsystem.dto.request.DoCheckRequest;
import com.highschool.highschoolsystem.dto.request.NavigatorRegisterRequest;
import com.highschool.highschoolsystem.dto.response.NavigatorRegisterResponse;
import com.highschool.highschoolsystem.service.NavigatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberWebSocketController {
    @Autowired
    private NavigatorService navigatorService;

    @MessageMapping("/navigator/register")
    @SendTo("/topic/register")
    public ResponseEntity<NavigatorRegisterResponse> register(NavigatorRegisterRequest navigatorRegisterRequest) throws Exception {
        return ResponseEntity.ok(navigatorService.register(navigatorRegisterRequest));
    }

    @MessageMapping("/navigator/do-check/{id}")
    @SendTo("/topic/do-check/{id}")
    public ResponseEntity<?> doCheck(
            @DestinationVariable("id") String navigatorId,
            DoCheckRequest doCheckRequest
    ) {
        System.out.println("doCheckRequest = " + doCheckRequest);
        return ResponseEntity.ok(navigatorService.doCheck(doCheckRequest, navigatorId));
    }
}
