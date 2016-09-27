package com.java.playground.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class MainController {
	@Autowired
	Cache cache;
	private static final String EMAIL = "email";

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home(@RequestParam(value = "userGuid", required = true) UUID userGuid) {
		ModelAndView model = new ModelAndView("index");
		String actualCreditCardInformationKey = "Credit-card-information-" + userGuid.toString();

		model.addObject(EMAIL, cache.get(EMAIL).get().toString());
		model.addObject(actualCreditCardInformationKey, cache.get(actualCreditCardInformationKey).get());

		return model;
	}
	
}