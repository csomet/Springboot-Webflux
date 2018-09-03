package com.csomet.springboot.webflux.app.controllers;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import com.csomet.springboot.webflux.app.models.dao.IProductDao;
import com.csomet.springboot.webflux.app.models.documents.Product;

import reactor.core.publisher.Flux;

@Controller
public class ProductController {
	
	@Autowired
	private IProductDao productDao;
	
	private static Logger log = LoggerFactory.getLogger(ProductController.class);

	@GetMapping({"/list", "/"})
	public String list(Model model) {
		
		Flux<Product> productFlux = productDao.findAll()
				
				.map(prod -> {
						prod.setName(prod.getName().toUpperCase());
						return prod;
					});
		
		productFlux.subscribe( p-> log.info(p.getName()));
			
		
		//add to view model
		model.addAttribute("products", productFlux);
		model.addAttribute("title", "List of products");
		
		
		return "list";
	}
	

	/*
	 * Data driver management for buffer quantity of elements.
	 */
	@GetMapping("list-datadriver")
	public String listDataDriver(Model model) {
		
		Flux<Product> productFlux = productDao.findAll()
				
				.map(prod -> {
						prod.setName(prod.getName().toUpperCase());
						return prod;
					})
		.delayElements(Duration.ofSeconds(1));
		
		productFlux.subscribe( p-> log.info(p.getName()));
			
		
		//add to view model
		model.addAttribute("products", new ReactiveDataDriverContextVariable(productFlux, 1));
		model.addAttribute("title", "List of products");
		
		
		return "list";
	}	
	
	/*
	 * Full mode - repeat operator, it repeats the content of the flux for n elements
	 */
	@GetMapping("list-full")
	public String listFull(Model model) {
		
		Flux<Product> productFlux = productDao.findAll()
				
				.map(prod -> {
						prod.setName(prod.getName().toUpperCase());
						return prod;
					})
		.repeat(5000);
		
			
		
		//add to view model
		model.addAttribute("products", productFlux);
		model.addAttribute("title", "List of products");
		
		
		return "list";
	}
	
	/*
	 * chunked mode buffer size (full mode view names)
	 */
	@GetMapping("list-chunked")
	public String listChunked(Model model) {
		
		Flux<Product> productFlux = productDao.findAll()
				
				.map(prod -> {
						prod.setName(prod.getName().toUpperCase());
						return prod;
					})
		.repeat(5000);
		
		//add to view model
		model.addAttribute("products", productFlux);
		model.addAttribute("title", "List of products");
		
		
		return "list-chunked";
	}
}
