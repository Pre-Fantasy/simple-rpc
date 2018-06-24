package com.glmapper.simple.provider.annotation.com.glmapper.simple.provider.service;/**
 * @author Pre_fantasy
 * @create 2018-06-24 14:52
 * @desc
 **/

import com.glmapper.simple.provider.annotation.SimpleProvider;
import com.glmapper.simpleapi.HelloService;

/**

 * @author dell

 * @create 2018-06-24 14:52

 * @desc service implements

 **/
@SimpleProvider(HelloServiceImpl.class)
public class HelloServiceImpl implements HelloService {


    @Override
    public String Hello(String name) {
        return "hello" + name;
    }
}
