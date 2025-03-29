package com.example.testCase1.services;

import com.example.testCase1.entities.OperationRequest;
import com.example.testCase1.entities.Wallet;
import com.example.testCase1.enums.OperationType;
import com.example.testCase1.exceptions.InsufficientFundsException;
import com.example.testCase1.exceptions.WalletNotFoundException;
import com.example.testCase1.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    private final ConcurrentHashMap<String, Object> walletLockMap = new ConcurrentHashMap<>();

    public Mono<Void> performOperation(OperationRequest request) {
        String walletId = request.getWalletId().toString();

        Object lock = walletLockMap.computeIfAbsent(walletId, k -> new Object());  // Если нет, добавляем новый lock объект

        synchronized (lock) {
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
                        return walletRepository.save(wallet);  // Сохраняем обновленный кошелек
                    })
                    .doFinally(signalType -> walletLockMap.remove(walletId)).then();  // Убираем кошелек из карты после завершения обработки
        }
    }

    public Mono<Double> getWalletBalance(UUID walletId) {
        return walletRepository.findById(walletId)
                .switchIfEmpty(Mono.error(new WalletNotFoundException("Кошелек не найден")))
                .map(Wallet::getBalance);

    }

}
