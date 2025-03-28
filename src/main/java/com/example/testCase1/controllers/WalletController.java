package com.example.testCase1.controllers;

import com.example.testCase1.entities.OperationRequest;
import com.example.testCase1.entities.WalletResponse;
import com.example.testCase1.services.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;


    @PostMapping("/api/v1/wallet")
    public Mono<ResponseEntity<Object>> newOperarion(@Valid @RequestBody OperationRequest request){
        return walletService.performOperation(request)
                .then(Mono.just(ResponseEntity.ok().build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @GetMapping("/wallets/{walletId}")
    public Mono<ResponseEntity<WalletResponse>> getWalletBalance(@PathVariable UUID walletId) {
        return walletService.getWalletBalance(walletId)
                .map(balance -> ResponseEntity.ok(new WalletResponse(balance)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
