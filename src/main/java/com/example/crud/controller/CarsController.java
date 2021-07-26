package com.example.crud.controller;

import com.example.crud.service.CarProvider;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CarsController {

	private final CarProvider carProvider;

	public CarsController(CarProvider carProvider) {
		this.carProvider = carProvider;
	}

	@GetMapping(value = "/cars")
	public String carsList(@RequestParam(value = "limit", required = false, defaultValue = "5") String limit, ModelMap model) {
		model.addAttribute("cars", carProvider.findAll(PageRequest.of(0, Integer.parseInt(limit))));

		return "cars/index";
	}
	
}