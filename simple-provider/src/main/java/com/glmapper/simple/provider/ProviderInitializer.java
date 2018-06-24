package com.glmapper.simple.provider;/**
 * @author Pre_fantasy
 * @create 2018-06-24 16:37
 * @desc
 **/

import com.glmapper.simple.properties.SimpleProviderProperties;
import com.glmapper.simple.provider.annotation.SimpleProvider;

import javax.imageio.spi.ServiceRegistry;
import java.beans.AppletInitializer;
import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.nio.channels.SocketChannel;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.SocketHandler;

/**

 * @author dell

 * @create 2018-06-24 16:37

 * @desc 启动并注册服务

 **/
public class ProviderInitializer implements ApplicationContextAware, InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderInitializer.class);

    private SimpleProviderProperties providerProperties;

    /*service regist;*/
    private ServiceRegistry serviceRegistry;


    private Map<String, Object> handlerMap = new HashMap<String, Object>();

    public ProviderInitializer(SimpleProviderProperties providerProperties, ServiceRegistry serviceRegistry) {
            this.providerProperties = providerProperties;
            this.serviceRegistry = serviceRegistry;
    }

    public void setApplicationContext(ApplicationContext crx) throws Exception {

        /*获取被SimpleProvider注解的Bean*/
        Map<String, Object> serviceBeanMap = ctx.getB.keansWithAnnotation(SimpleProvider.class);
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                String interfaceName = serviceBean.getClass().getAnnotation(SimpleProvider.class).value().getName();
                handlerMap.put(interfaceName, serviceBean);
            }
        }
    }

    public void afterPropertiesSet() throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            ChannelHandler channelHandler = new ChannelInitializer<SocketChannel>{
                public void initChannel(SocketChannel channel) throws Exception{
                    channel.pipeline()
                            .addLast(new SimpleDecoder(SimpleRequest.class))
                            .addLast(new SimpleEncoder(SimpleResponse.class))
                            .addLast(new SimpleHandler(handlerMap));
                }
            };
            bootstrap.group(bossGroup, wordGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(channelHandler)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            String host = getLocalHost();
            if (host == null) {
                LOGGER.error("can't get service address,because address is null");
                throw new SimpleException("can't get service address,because address is null");
            }

            int port = providerProperties.getProt();
            ChannelFuter future = bootstrap.bind(host, port).sync();
            LOGGER.debug("server started on port { }", port);
            if (serviceRegistry != null) {
                String serverAddress = host + ":" + port;
                serviceRegistry.register(serverAddress);
            }
            future.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

    private String getLocalHost (){

        Enumeration<NetworkInterface> allNetInterface;
        try {
            allNetInterface = NetworkInterface.getNetworkInterfaces();
        } catch (Exception e) {
            LOGGER.error("get local address error , cause: ", e);
            return null;
        }
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = allNetInterface.nextElement();
            Enumeration<InetAddress> addresses = netIterface.getInetAddress();
            while (addresses.hasMoreElements()) {
                InetAddress ip = addresses.nextElement();
                if (ip instanceof Inet4Address && !ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {
                    return ip.getHostAddress();
                }
            }
        }
        return null;
    }

}









































