package com.pwc.pwcesg.frontoffice.search;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pwc.pwcesg.frontoffice.search.controller.SearchController;

import lombok.extern.slf4j.Slf4j;

/**
 * konantech 
 * test Controller
 */

@Slf4j
@Controller
public class HomeController {

	@RequestMapping("/search/test")
	public String test() {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> HomeController 진입");
		return "frontoffice/search/test";
	}
	
	@RequestMapping("/search/common/topSearch")
	public String topSearch(Model model) {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> topSearch 진입");
		
		return "frontoffice/search/common/topSearch";
	}
}
