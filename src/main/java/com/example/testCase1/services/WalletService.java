package com.example.testCase1.services;

import com.example.testCase1.entities.OperationRequest;
import com.example.testCase1.entities.Wallet;
import com.example.testCase1.enums.OperationType;
import com.example.testCase1.exceptions.InsufficientFundsException;
import com.example.testCase1.exceptions.WalletNotFoundException;
import com.example.testCase1.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    public Mono<Void> performOperation(OperationRequest request) {
        return walletRepository.findById(request.getWalletId())
                .switchIfEmpty(Mono.error(new WalletNotFoundException("Кошелек не найден")))
                .flatMap(wallet -> {
                    if (request.getOperationType() == OperationType.DEPOSIT) {
                        wallet.setBalance(wallet.getBalance() + request.getAmount());
                    } else if (request.getOperationType() == OperationType.WITHDRAW) {
                        if (wallet.getBalance() < request.getAmount()) {
                            return Mono.error(new InsufficientFundsException("Недостаточно средств"));
                        }
                        wallet.setBalance(wallet.getBalance() - request.getAmount());
                    }
                    return walletRepository.save(wallet);
                })
                .then();
        /*
        TODO сделать этот метод через очередь, т.е. он добавляет в очередь, а из очереди в синхронном режиме меняется бд
         */
    }

    public Mono<Double> getWalletBalance(UUID walletId) {
        return walletRepository.findById(walletId)
                .switchIfEmpty(Mono.error(new WalletNotFoundException("Кошелек не найден")))
                .map(Wallet::getBalance);
    }

}
