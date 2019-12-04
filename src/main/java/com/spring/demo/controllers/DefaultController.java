package com.spring.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DefaultController {

    @GetMapping("/**/{path:[^?\\.]+}/")
    public ModelAndView path(@PathVariable String path, HttpServletRequest request){
        String path2push = "redirect:/?redirect="+request.getRequestURI();
        path2push = request.getQueryString() != null ? path2push.concat("?" + request.getQueryString()) : path2push;
        return new ModelAndView(path2push);
    }
}
