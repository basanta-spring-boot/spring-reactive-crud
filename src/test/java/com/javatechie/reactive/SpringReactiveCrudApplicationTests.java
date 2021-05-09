package com.javatechie.reactive;

import com.javatechie.reactive.dto.ProductDto;
import com.javatechie.reactive.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@WebFluxTest(SpringReactiveCrudApplication.class)
class SpringReactiveCrudApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductService service;


    @Test
    public void addProductTest() {
        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("101", "mobile", 1, 15000));
        when(service.addProduct(productDtoMono)).thenReturn(productDtoMono);

        webTestClient.post()
                .uri("/product")
                .body(Mono.just(productDtoMono), ProductDto.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void getProductsTest() {
        Flux<ProductDto> productDtoFlux = Flux.just(new ProductDto("101", "mobile", 1, 15000),
                new ProductDto("102", "book", 2, 25000));
        when(service.getProducts()).thenReturn(productDtoFlux);

        Flux<ProductDto> responseBody = webTestClient.get()
                .uri("/product")
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductDto.class).getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(new ProductDto("101", "mobile", 1, 15000))
                .expectNext(new ProductDto("102", "book", 2, 25000))
                .verifyComplete();

    }

    @Test
    public void getProductTest() {
        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("101", "mobile", 1, 15000));
        when(service.getProductById(any())).thenReturn(productDtoMono);

        Flux<ProductDto> responseBody = webTestClient.get()
                .uri("/product/get/101")
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductDto.class).getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNextMatches(p -> p.getName().equals("mobile"))
                .verifyComplete();

    }

    @Test
    public void getProductBetweenRangeTest(){
        Flux<ProductDto> productDtoFlux = Flux.just(new ProductDto("101", "mobile", 1, 15000),
                new ProductDto("102", "book", 2, 25000),
                new ProductDto("103", "TV", 1, 8000));
        when(service.getProductsInRange(5000,30000)).thenReturn(productDtoFlux);

        Flux<ProductDto> responseBody = webTestClient.get()
                .uri("/product/price-range?min=5000&max=30000")
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductDto.class).getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
               .expectNextCount(1)
                .expectNextCount(2)
                .expectNextCount(3)
        .expectComplete();
    }

    @Test
    public void updateProductTest() {
        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("101", "mobile", 1, 15000));
        when(service.updateProduct(productDtoMono, "101")).thenReturn(productDtoMono);

        webTestClient.put()
                .uri("/product/101")
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    public void deleteProductTest() {
        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("101", "mobile", 1, 15000));
        given(service.deleteProduct(any())).willReturn(Mono.empty());
        webTestClient.delete()
                .uri("/product/101")
                .exchange()
                .expectStatus().isOk();

    }
}
