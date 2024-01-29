package com.pwc.pwcesg.frontoffice.newstrend.controller;

import com.pwc.pwcesg.frontoffice.newstrend.controller.dto.*;
import com.pwc.pwcesg.frontoffice.newstrend.service.NewsTrendService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
@Controller
@RequestMapping(value = "/news-trend")
public class NewsTrendController {

    private final NewsTrendService newsTrendService;

    public NewsTrendController(NewsTrendService newsTrendService) {
        this.newsTrendService = newsTrendService;
    }

    @RequestMapping(value = "/page")
    public ModelAndView index(HttpServletRequest request,
			@RequestParam(value="tabId", required = false) String tabId) {

    	ModelAndView mav = new ModelAndView("frontoffice/news-trend/page");
    	if(tabId == null)
			tabId = "1";
		mav.addObject("tabId", tabId);

        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "/getKeywords", method = {RequestMethod.POST})
    public ResGetKeywords getKeywords() throws Exception {
        return this.newsTrendService.getKeywords();
    }

    @ResponseBody
    @RequestMapping(value = "/getNews", method = {RequestMethod.POST})
    public ResGetNews getNews(@RequestBody ReqGetNews params) throws Exception {
        return this.newsTrendService.getNews(params);
    }

    @ResponseBody
    @RequestMapping(value = "/getTrends", method = {RequestMethod.POST})
    public ResGetTrends getTrends(@RequestBody ReqGetTrends params) throws Exception {
        return this.newsTrendService.getTrends(params);
    }

    @ResponseBody
    @RequestMapping(value = "/getParentTopicTrendHits", method = {RequestMethod.POST})
    public ResGetParentTopicTrendHits getParentTopicTrendHits() throws Exception {
        return this.newsTrendService.getParentTopicTrendHits();
    }

    @ResponseBody
    @RequestMapping(value = "/getParentTopicDetails", method = {RequestMethod.POST})
    public ResGetParentTopicDetails getParentTopicDetails(@RequestBody ReqGetParentTopicDetails params) throws Exception {
        return this.newsTrendService.getParentTopicDetails(params);
    }

}