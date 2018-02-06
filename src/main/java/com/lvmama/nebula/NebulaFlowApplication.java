package com.lvmama.nebula;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

@ImportResource(locations={"classpath:application-bean.xml"})
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
public class NebulaFlowApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(NebulaFlowApplication.class, args);
	}
}
