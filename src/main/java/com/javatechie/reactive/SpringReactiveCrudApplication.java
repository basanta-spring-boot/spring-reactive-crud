package com.javatechie.reactive;

import com.javatechie.reactive.dto.ProductDto;
import com.javatechie.reactive.entity.Product;
import com.javatechie.reactive.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/product")
public class SpringReactiveCrudApplication {

	@Autowired
	private ProductService service;

	@GetMapping
	public Flux<ProductDto> getProducts(){
		return service.getProducts();
	}

	@GetMapping("/price-range")
	public Flux<ProductDto> getProductsByPriceRange(@RequestParam("min") double min,@RequestParam("max") double max){
		return service.getProductsInRange(min,max);
	}

	@GetMapping("/get/{id}")
	public Mono<ProductDto> getProduct(@PathVariable String id){
		return service.getProductById(id);
	}

	@PostMapping
	public Mono<ProductDto> saveProduct(@RequestBody Mono<ProductDto> productDtoMono){
		return service.addProduct(productDtoMono);
	}

	@PutMapping("/{id}")
	public Mono<ProductDto> editProduct(@RequestBody Mono<ProductDto> productDtoMono,@PathVariable String id){
		return service.updateProduct(productDtoMono,id);
	}

	@DeleteMapping("/{id}")
	public Mono<Void> deleteProduct(@PathVariable String id){
		return service.deleteProduct(id);
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringReactiveCrudApplication.class, args);
	}

}
