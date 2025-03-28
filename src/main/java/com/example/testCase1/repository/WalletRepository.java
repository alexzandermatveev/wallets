package com.example.testCase1.repository;



import com.example.testCase1.entities.Wallet;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.sql.LockMode;
import org.springframework.data.relational.repository.Lock;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@org.springframework.stereotype.Repository
public interface WalletRepository extends ReactiveCrudRepository<Wallet, UUID> {

    @Lock(LockMode.PESSIMISTIC_WRITE)  // блокировка строки
    Mono<Wallet> findByWalletId(UUID walletId);

    @Modifying
    @Query("UPDATE wallet SET balance = :balance, version = version + 1 WHERE wallet_id = :walletId AND version = :version")
    Mono<Integer> updateBalance(UUID walletId, double balance, long version);
}
