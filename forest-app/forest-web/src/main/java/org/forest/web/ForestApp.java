package org.forest.web;

import org.forest.model.*;
import org.forest.persistence.ForestRepository;
import org.forest.persistence.TreeRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootApplication(scanBasePackages = {"org.forest"}, exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class ForestApp {
    public static void main(String[] args) {
        SpringApplication.run(ForestApp.class, args);
    }


}

@Component
class AppInitializer implements InitializingBean {

    private final TreeRepository treeRepository;
    private final ForestRepository forestRepository;

    @Autowired
    public AppInitializer(TreeRepository treeRepository, ForestRepository forestRepository) {
        this.treeRepository = treeRepository;
        this.forestRepository = forestRepository;
    }

    @Override
    public void afterPropertiesSet() {
        treeRepository.insert(new Tree(UUID.randomUUID(), LocalDate.now(), Species.OAK, Exposure.SHADOW, 40.0));
        treeRepository.insert(new Tree(UUID.randomUUID(), LocalDate.now(), Species.EVERGREEN_OAK, Exposure.SUNNY, 20.0));
        treeRepository.insert(new Tree(UUID.randomUUID(), LocalDate.now(), Species.BEECH, Exposure.MID_SHADOW, 60.0));
        treeRepository.insert(new Tree(UUID.randomUUID(), LocalDate.now(), Species.FIR, Exposure.SUNNY, 10.0));

        forestRepository.insert(new Forest(UUID.randomUUID(), ForestType.TROPICAL, treeRepository.findAll(), 100000.00));
    }
}
