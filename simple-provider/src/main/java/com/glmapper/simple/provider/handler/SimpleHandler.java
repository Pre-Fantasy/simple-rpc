package com.glmapper.simple.provider.handler;

import java.util.Map;
import java.util.logging.Logger;

/**
 * @author dell
 * @create 2018-06-24 18:21
 * @desc SimpleHandler是一个实现了 netty的SimpleChannelInboundHandler的请求处理器类
 **/
public class SimpleHandler extends SimpleChannelInboundHandler<SimpleRequest> {


    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleHandler.class);

    private final Map<String, Object> handlerMap;


    public SimpleHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    public void channelRead0(final ChannelHandlerContext ctx, SimpleRequest request) throws Exception {
        SimpleResponse response = new SimpleResponse();
        response.setRequestId(request.getRequestId());
        try {
            Object result = handler(request);
            response.setResult(request);
        } catch (Throwable t) {
            response.setError(t);
        }
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private Object handle(SimpleRequest request) throws Throwable {
        String className = request.getClassName();
        Object serviceBean = handlerMap.get(className);
        Class<?> sersviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();


        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        FastClass serviceFastClass = FastClass.create(serviceClass);
        FashMehod sersviceFashMehod = serviceFastClass.getMethod(methodName, parameterTypes);
        return sersviceFashMehod.invoke(serviceBean, parameters);

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable causes) {
        LOGGER.error("server caught exception", cause);
        ctx.close();
    }
}
