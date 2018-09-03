package com.csomet.springboot.webflux.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csomet.springboot.webflux.app.models.dao.IProductDao;
import com.csomet.springboot.webflux.app.models.documents.Product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {
	
	@Autowired
	private IProductDao productDao;
	
	private static final Logger log = LoggerFactory.getLogger(ProductRestController.class);
	
	
	@GetMapping()
	public Flux<Product> index(){
		
		Flux<Product> productFlux = productDao.findAll()
				
				.map(prod -> {
						prod.setName(prod.getName().toUpperCase());
						return prod;
					})
				.doOnNext(p-> log.info(p.getName()));
		
		return productFlux;
	}
	
	
	@GetMapping("/{id}")
	public Mono<Product> get(@PathVariable String id){
		
		//Mono<Product> product = productDao.findById(id); Unique product flux
		
		//by filter operator getting the selected one by id.
		Mono<Product> product = productDao.findAll()
				.filter(p -> p.getId().equals(id)).next()
				.doOnNext(p-> log.info(p.getName()));
		
		
		return product;
	}

}

