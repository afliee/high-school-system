package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.dto.request.FaultRequest;
import com.highschool.highschoolsystem.service.FaultService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/api/v1/fault")
public class FaultApiController {
    @Autowired
    private FaultService faultService;

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestBody @Valid FaultRequest request
    ) {
        if (request.getFaults().length == 0) {
            throw new RuntimeException("Faults must not be empty");
        }
        
        faultService.create(request);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/delete/{faultDetailId}")
    public ResponseEntity<?> delete(
            @PathVariable String faultDetailId
    ) {
        faultService.delete(faultDetailId);
        return ResponseEntity.ok().build();
    }
}
