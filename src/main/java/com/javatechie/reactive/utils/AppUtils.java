package com.javatechie.reactive.utils;

import com.javatechie.reactive.dto.ProductDto;
import com.javatechie.reactive.entity.Product;
import org.springframework.beans.BeanUtils;

public class AppUtils {

    public static Product dtoToEntity(ProductDto request) {
        Product product = new Product();
        BeanUtils.copyProperties(request, product);
        return product;
    }

    public static ProductDto entityToDto(Product product) {
        ProductDto request = new ProductDto();
        BeanUtils.copyProperties(product, request);
        return request;
    }
}
