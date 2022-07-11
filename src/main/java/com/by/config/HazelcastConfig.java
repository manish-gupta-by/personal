package com.by.config;

import com.by.model.JobDTO;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    @Bean
    public IMap<Integer, JobDTO> getCache(){
        Config hzConfig = new Config();
        hzConfig.getNetworkConfig().setPort(7001).setPortAutoIncrement(true).setPortCount(20);

        hzConfig.setClusterName("employees");

        HazelcastInstance instance = Hazelcast.newHazelcastInstance(hzConfig);
        return instance.getMap("jobs");
    }
}
