package com.thejwal.iConnector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class iConnectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(iConnectorApplication.class, args);
	}
}

@Controller
class DataController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/")
	public String  index(Model model){
		List<Map<String, Object>> statusCounts = jdbcTemplate.queryForList("SELECT STATUS , count(*) AS COUNT FROM ORDMST GROUP BY STATUS");

		for (Map<String, Object> statusCount:statusCounts){
			int count = (int) statusCount.get("COUNT");
			if ("C".equals(statusCount.get("STATUS"))){
				model.addAttribute("completedCount" , statusCount.get("COUNT"));
			}else if ("X".equals(statusCount.get("STATUS"))){
				model.addAttribute("cancelledCount" , statusCount.get("COUNT"));
			}else{
				model.addAttribute("inProgressCount" , statusCount.get("COUNT"));
			}
		}

		List<Map<String, Object>> yearCounts = jdbcTemplate.queryForList("SELECT YEAR(STSDATE) as year, COUNT(*) AS county FROM ORDSTS GROUP BY YEAR(STSDATE)");
		model.addAttribute("yearCount" , yearCounts);

		ArrayList countryCounts = (ArrayList) jdbcTemplate.queryForList("SELECT COUNTRY , COUNT(*) AS COUNTC FROM ORDCNT GROUP BY COUNTRY");
		model.addAttribute("countryCount" , countryCounts);

		long currentTimestamp = System.currentTimeMillis();
		Instant instant = Instant.ofEpochMilli(currentTimestamp);
		LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedDateTime = dateTime.format(formatter);

		model.addAttribute("timeStamp" , formattedDateTime );
		return "index";
	}
}


















