package com.pwc.pwcesg.frontoffice.schedule.newstore;

import com.pwc.pwcesg.frontoffice.schedule.newstore.service.ScheduleNewStoreService;
import java.net.InetAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduleNewStoreHandler {

	private final ScheduleNewStoreService service;

	public ScheduleNewStoreHandler(ScheduleNewStoreService service) {
		this.service = service;
	}

	@Scheduled(cron = "1 5 0 * * *")
	public void runNews() {
		try {
			InetAddress ip = InetAddress.getLocalHost();
			if (ip.getHostAddress().equals("4.230.9.118") || ip.getHostAddress()
				.equals("10.0.0.6")) {
				log.info("Start Schedule runNews...");
				this.service.runNews();
			} else {
				log.info("Reject Schedule runNews...");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			e.printStackTrace();
		} finally {
			log.info("Complete Schedule runNews.");
		}
	}

	@Scheduled(cron = "1 8 0 * * *")
	public void runTopic() {
		try {
			InetAddress ip = InetAddress.getLocalHost();
			if (ip.getHostAddress().equals("4.230.9.118") || ip.getHostAddress()
				.equals("10.0.0.6")) {
				log.info("Start Schedule runTopic.");
				this.service.runTopic();
			} else {
				log.info("Reject Schedule runTopic.");
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			log.info("Complete Schedule runTopic.");
		}
	}

}
