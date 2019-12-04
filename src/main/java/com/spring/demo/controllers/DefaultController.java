package com.spring.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DefaultController {

//    @GetMapping("/")
//    public ModelAndView index() {
//        return new ModelAndView("index.html");
//    }

//    @GetMapping("/**/{path:[^?\\.]+}/")
//    public ModelAndView path(@PathVariable String path, HttpServletRequest request){
////        Push the path
//        String pushPath = "redirect:/?redirect=" + request.getRequestURI();
////        Check if redirect route
//        pushPath = request.getQueryString() != null ? pushPath.concat("?" + request.getQueryString()) : pushPath;
//        return new ModelAndView(pushPath);
//    }

    @GetMapping("/**/{path:[^?\\.]+}/")
    public ModelAndView path(@PathVariable String path, HttpServletRequest request){
        String path2push = "redirect:/?redirect="+request.getRequestURI();
        System.out.println(request.getRequestURI().substring(1));
        path2push = request.getQueryString() != null ? path2push.concat("?" + request.getQueryString()) : path2push;
        System.out.println(request.getQueryString());
        return new ModelAndView(path2push);
    }


}
