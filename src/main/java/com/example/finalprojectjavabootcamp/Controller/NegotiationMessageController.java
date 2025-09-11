package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.DTOIN.NegotiationMessageDTOIn;
import com.example.finalprojectjavabootcamp.Service.NegotiationMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/negotiation-message")
public class NegotiationMessageController {


    private final NegotiationMessageService service;


    @PostMapping("/negotiations/{negotiationId}/messages/buyer")
    public ResponseEntity<?> sendByBuyer(@PathVariable Integer negotiationId,
                                         @Valid @RequestBody NegotiationMessageDTOIn body) {
        service.sendByBuyer(negotiationId, body);
        return ResponseEntity.ok(new ApiResponse("buyer message sent."));
    }

    @PostMapping("/negotiations/{negotiationId}/messages/seller")
    public ResponseEntity<?> sendBySeller(@PathVariable Integer negotiationId,
                                          @Valid @RequestBody NegotiationMessageDTOIn body) {
        service.sendBySeller(negotiationId, body);
        return ResponseEntity.ok(new ApiResponse("seller message sent."));
    }

    @GetMapping("/negotiations/{negotiationId}/messages/all")
    public ResponseEntity<?> listAll(@PathVariable Integer negotiationId) {
        return ResponseEntity.ok(service.listAscSimple(negotiationId));
    }


    @PostMapping("/{negotiationId}/ai/summarize")
    public ResponseEntity<?> summarize(@PathVariable Integer negotiationId) {
        return ResponseEntity.ok(service.summarizeAndSet(negotiationId));
    }

}
