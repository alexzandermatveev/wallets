package com.example.testCase1.repository;



import com.example.testCase1.entities.Wallet;
import org.springframework.data.relational.core.sql.LockMode;
import org.springframework.data.relational.repository.Lock;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@org.springframework.stereotype.Repository
public interface WalletRepository extends ReactiveCrudRepository<Wallet, UUID> {

    @Lock(LockMode.PESSIMISTIC_WRITE)  // блокировка строки
    Mono<Wallet> findById(UUID walletId);
}
