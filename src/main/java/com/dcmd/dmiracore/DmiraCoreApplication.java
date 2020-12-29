package com.dcmd.dmiracore;

import com.dcmd.dmiracore.repository.RoleRepository;
import com.dcmd.dmiracore.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = {UserRepository.class, RoleRepository.class})
public class DmiraCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(DmiraCoreApplication.class, args);
	}

}
