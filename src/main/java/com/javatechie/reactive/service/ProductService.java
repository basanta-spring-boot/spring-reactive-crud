package com.javatechie.reactive.service;

import com.javatechie.reactive.dto.ProductDto;
import com.javatechie.reactive.entity.Product;
import com.javatechie.reactive.repository.ProductRepository;
import com.javatechie.reactive.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public Mono<ProductDto> addProduct(Mono<ProductDto> productDto) {
        return productDto
                .map(AppUtils::dtoToEntity)
                .flatMap(this.repository::insert)
                .map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> updateProduct(Mono<ProductDto> productDto, String id) {
        return repository.findById(id)
                .flatMap(p -> productDto
                        .map(AppUtils::dtoToEntity)
                        .doOnNext(e -> e.setId(id)))
                .flatMap(repository::save)
                .map(AppUtils::entityToDto);

    }

    public Flux<ProductDto> getProducts() {
        return repository.findAll().map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> getProductById(String id) {
        return repository.findById(id).map(AppUtils::entityToDto);
    }

    public Flux<ProductDto> getProductsInRange(double min ,double max) {
        return repository.findByPriceBetween(Range.closed(min,max)).map(AppUtils::entityToDto);
    }

    public Mono<Void> deleteProduct(String id){
        return repository.deleteById(id);
    }

}
