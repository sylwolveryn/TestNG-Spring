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

	public static final String EMAIL = "email";
	public static final String CREDIT_CARD_INFORMATION = "credit-card-information-";
	@Autowired
	Cache cache;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home(@RequestParam(value = "userGuid", required = true) UUID userGuid) {
		ModelAndView model = new ModelAndView("index");
		String actualCreditCardInformationWithGUID = CREDIT_CARD_INFORMATION + userGuid.toString();

		model.addObject(EMAIL, cache.get(EMAIL));
		model.addObject(actualCreditCardInformationWithGUID, cache.get(actualCreditCardInformationWithGUID));

		return model;
	}
	
}