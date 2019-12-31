package io.apj;

import io.apj.datasources.DynamicDataSourceConfig;
import io.apj.modules.workBook.service.WorkBookService;
import io.apj.modules.workBook.service.impl.WorkBookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Timer;
import java.util.TimerTask;


@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableTransactionManagement
@EnableAutoConfiguration(exclude={org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
@Import({DynamicDataSourceConfig.class})
public class ApjApplication extends SpringBootServletInitializer {

	private static WorkBookService workBookService;
	@Autowired
	public void  setDataService(WorkBookService workBookService){
		ApjApplication.workBookService = workBookService;
	}

	public static void main(String[] args) {
		SpringApplication.run(ApjApplication.class, args);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				workBookService.selectLock();
				// TODO Auto-generated method stub
			}
		}, 10000, 30000);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ApjApplication.class);
	}
}
