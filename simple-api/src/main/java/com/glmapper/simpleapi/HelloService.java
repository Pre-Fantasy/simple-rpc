package com.glmapper.simpleapi;

/**

 * @author dell

 * @create 2018-06-24 14:31

 * @desc 接口api模块，其中sonsumer和provider模块都需要引用到

 **/
public interface HelloService {

    /**
     *  @author Pre_fantasy
     *  @create 2018/6/24 14:34
     *  @param  {Sting name}
     *  @return {String}
     *  @desc   service function
     */
    String Hello(String name);
}
