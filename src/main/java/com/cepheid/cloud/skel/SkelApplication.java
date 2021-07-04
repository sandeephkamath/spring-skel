package com.cepheid.cloud.skel;

import java.util.stream.Stream;

import com.cepheid.cloud.skel.model.Item;
import com.cepheid.cloud.skel.model.Status;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.cepheid.cloud.skel.repository.ItemRepository;

@SpringBootApplication()
@EnableJpaRepositories(basePackageClasses = {ItemRepository.class})
public class SkelApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkelApplication.class, args);
    }

    @Bean
    ApplicationRunner initItems(ItemRepository repository) {
        return args -> {
            Stream.of("Lord of the rings", "Hobbit", "Silmarillion", "Unfinished Tales and The History of Middle-earth")
                    .forEach(name -> {
                        Item item = new Item();
                        item.setName(name);
                        item.setStatus(Status.VALID);
                        repository.save(item);
                    });
            repository.findAll().forEach(System.out::println);
        };
    }

}
