package com.yangqi.recommendedsystem.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @author xiaoer
 * @date 2020/3/2 21:16
 */
@Component
public class CanalClient implements DisposableBean {
    /**
     * 用来连接 canal 的对象
     */
    private CanalConnector canalConnector;

    /**
     * 获取 canal 连接对象
     *
     * @return canal 连接
     */
    @Bean
    public CanalConnector getCanalConnector() {
        canalConnector = CanalConnectors.newClusterConnector(Lists.newArrayList(
           new InetSocketAddress("192.168.21.89", 11111)), "example", "yangqi", "xiaoer");
        canalConnector.connect();
        // 指定 filter，格式 {database}.{table}
        canalConnector.subscribe();
        // 回滚寻找上次中断的位置
        canalConnector.rollback();

        return canalConnector;
    }

    @Override
    public void destroy() throws Exception {
        if (canalConnector != null) {
            canalConnector.disconnect();
        }
    }
}
