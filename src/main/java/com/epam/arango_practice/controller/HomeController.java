package com.epam.arango_practice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for handling the root URL of the application.
 */
@Controller
@RequestMapping("/")
@Api(value = "HomeController", description = "Redirect to SwaggerUI interface.", position = 0)
@ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully redirected.")})
public class HomeController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ApiOperation(value = "Redirection...")
	public String goToSwaggerUI() {
		return "redirect:swagger-ui.html";
	}
}
