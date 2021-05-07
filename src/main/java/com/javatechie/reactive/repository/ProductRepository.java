package com.javatechie.reactive.repository;

import com.javatechie.reactive.entity.Product;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product,String> {
    Flux<Product> findByPriceBetween(Range<Double> doubleRange);
}
