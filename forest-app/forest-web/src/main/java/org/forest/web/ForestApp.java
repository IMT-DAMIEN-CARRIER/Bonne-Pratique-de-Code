package org.forest.web;

import org.forest.model.Exposure;
import org.forest.model.Species;
import org.forest.model.Tree;
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

    @Autowired
    public AppInitializer(TreeRepository treeRepository) {
        this.treeRepository = treeRepository;
    }

    @Override
    public void afterPropertiesSet() {
        treeRepository.insert(new Tree(UUID.randomUUID(), LocalDate.now(), Species.OAK, Exposure.SHADOW, 40.0));
    }
}
