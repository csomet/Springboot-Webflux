package com.csomet.springboot.webflux.app;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.csomet.springboot.webflux.app.models.dao.IProductDao;
import com.csomet.springboot.webflux.app.models.documents.Product;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringbootWebfluxApplication implements CommandLineRunner{

	private static final Logger log = LoggerFactory.getLogger(SpringbootWebfluxApplication.class);
	
	@Autowired
	private IProductDao productDao;
	
	@Autowired
	private ReactiveMongoTemplate reactiveMongoTemplate;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringbootWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		//reset table everytime we start app
		reactiveMongoTemplate.dropCollection("products").subscribe();
		
		Flux.just( new Product("iPhone", 1159.0),
					new Product("MacBookPro", 3999.0),
					new Product("Apple TV", 499.50),
					new Product("Apple Watch", 499.0),
					new Product("AirPods", 189.99))
		//Flatmap get the product and process it. It does not return the Flux but a simple object instead of map.
		.flatMap(
				product -> {
					product.setCreatedAt(new Date());
					return productDao.save(product);
		})
		
		//suscribe to the flux
		.subscribe(product -> log.info("INSERT: " + product.getId() + "_" + product.getName()));
		
		
	}
}
