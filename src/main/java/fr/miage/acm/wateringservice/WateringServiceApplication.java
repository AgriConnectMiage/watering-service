package fr.miage.acm.wateringservice;

import fr.miage.acm.wateringservice.device.actuator.watering.scheduler.WateringSchedulerServiceTest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;;

@SpringBootApplication
@EnableFeignClients
public class WateringServiceApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(WateringServiceApplication.class, args);

		WateringSchedulerServiceTest wateringSchedulerServiceTest = context.getBean(WateringSchedulerServiceTest.class);


		wateringSchedulerServiceTest.addManualWateringSchedulerToActuator(20);
//		wateringSchedulerServiceTest.addManualWateringSchedulerToAllActuators(5);
//		wateringSchedulerServiceTest.addIntelligentWateringSchedulerToActuator();
//		wateringSchedulerServiceTest.deleteAllWateringSchedulers();
//		wateringSchedulerServiceTest.addManualWateringSchedulerToAllActuators(1000);
//		wateringSchedulerServiceTest.deleteWateringScheduler();
	}

}
