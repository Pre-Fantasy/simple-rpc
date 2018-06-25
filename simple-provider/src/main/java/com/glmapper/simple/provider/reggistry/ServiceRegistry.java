package com.glmapper.simple.provider.reggistry;

import java.nio.charset.Charset;
import java.util.logging.Logger;

/**

 * @author dell

 * @create 2018-06-25 13:46

 * @desc 入口ProviderInitializer调用了ServiceRegistry的registry方法

 **/
public class ServiceRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);
    private ZookeeperProperties zookeeperProperties;


    public ServiceRegistry(ZookeeperProperties zookeeperProperties) {
        this.zookeeperProperties = zookeeperProperties;
    }

    public void register(String data) {
        if (data != null) {
            ZooKeeper zk = ZookeeperUties.connectionServer(zookeeperProperties.getAddress, zookeeperProperties.getTimeout());
            if (zk != null) {
                addRootNode(zk);
                createNode(zk, data);
            }
        }
    }

    private void createNode(ZooKeeper zk, String data) {

        try {
            byte[] bytes = data.getBytes(Charset.forName("UTF-8"));
            String dataPath = zookeeperProperties.getRootPath() + zookeeperProperties.getDataPath();
        } catch (KeeperException | InterruptedException e) {
            LOGGER.error("create zookeeper node error,cause:", e);
        }
    }

    private void addRootNode(ZooKeeper zk) {
        try {
            String registryPath = zookeeperProperties.getRootPath();
            Stat s = zk.exists(registryPath, false);
            if (s == null) {
                zk.create(registryPath, new byte[], Zoodefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException | InterruptedException e) {
            LOGGER.error("zookeeper add root node error, cause:  ", e);
        }
    }



}
