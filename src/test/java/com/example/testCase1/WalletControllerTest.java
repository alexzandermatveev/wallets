package com.example.testCase1;

import com.example.testCase1.controllers.WalletController;
import com.example.testCase1.entities.OperationRequest;
import com.example.testCase1.entities.Wallet;
import com.example.testCase1.enums.OperationType;
import com.example.testCase1.repository.WalletRepository;
import com.example.testCase1.services.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@WebFluxTest(controllers = WalletController.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WalletControllerTest {

    @Autowired
    private WebTestClient webTestClient;  // Инжектируется WebTestClient

    @MockitoBean
    private WalletRepository walletRepository;  // Мокируем репозиторий

    @InjectMocks
    private WalletService walletService;  // Мокируем сервис

    private UUID walletId;
    private OperationRequest depositRequest;
    private OperationRequest withdrawRequest;

    @BeforeEach
    public void setUp() {
        walletId = UUID.randomUUID();
        depositRequest = new OperationRequest(walletId, OperationType.DEPOSIT, 100.00);
        withdrawRequest = new OperationRequest(walletId, OperationType.WITHDRAW, 50.00);

        // Настройка мока для performOperation
//        when(walletService.performOperation(depositRequest)).thenReturn(Mono.empty());  // Ожидаем успешное пополнение
//        when(walletService.performOperation(withdrawRequest)).thenReturn(Mono.empty());  // Ожидаем успешное снятие
    }

    @Test
    public void testPerformDepositOperation() {
        Wallet wallet = new Wallet(walletId, 0.0, "John Doe");
        when(walletRepository.findById(walletId)).thenReturn(Mono.just(wallet));
        when(walletRepository.save(wallet)).thenReturn(Mono.just(wallet));

        webTestClient.post()
                .uri("/api/v1/wallet")
                .bodyValue(depositRequest)
                .exchange()
                .expectStatus().isOk();  // Проверяем, что операция прошла успешно

        verify(walletRepository).save(wallet);  // Проверяем, что сохранение произошло
    }

    @Test
    public void testPerformWithdrawOperationSuccess() {
        Wallet wallet = new Wallet(walletId, 100.0, "John Doe");
        when(walletRepository.findById(walletId)).thenReturn(Mono.just(wallet));
        when(walletRepository.save(wallet)).thenReturn(Mono.just(wallet));

        webTestClient.post()
                .uri("/api/v1/wallet")
                .bodyValue(withdrawRequest)
                .exchange()
                .expectStatus().isOk();  // Проверяем, что операция прошла успешно

        verify(walletRepository).save(wallet);  // Проверяем, что сохранение произошло
    }

    @Test
    public void testPerformWithdrawOperationInsufficientFunds() {
        Wallet wallet = new Wallet(walletId, 30.0, "John Doe");
        when(walletRepository.findById(walletId)).thenReturn(Mono.just(wallet));

        webTestClient.post()
                .uri("/api/v1/wallet")
                .bodyValue(withdrawRequest)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)  // Проверка ошибки при недостаточных средствах
                .expectBody()
                .jsonPath("$.errorMessage").isEqualTo("Недостаточно средств");
    }

    @Test
    public void testPerformOperationWalletNotFound() {
        when(walletRepository.findById(walletId)).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/api/v1/wallet")
                .bodyValue(depositRequest)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)  // Проверка, если кошелек не найден
                .expectBody()
                .jsonPath("$.errorMessage").isEqualTo("Кошелек не найден");
    }
}
