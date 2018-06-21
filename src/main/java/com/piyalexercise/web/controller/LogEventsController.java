package com.piyalexercise.web.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.piyalexercise.dao.EventLogDao;
import com.piyalexercise.model.LogFileParameters;

@Controller
@RequestMapping("/server")
public class LogEventsController {

	private static final Logger logger = LoggerFactory.getLogger(LogEventsController.class);
	private static final String FILEPATH = "E:/Exercise_CS/application.log"; // Currently using local path
	private static int THRESHOLD_DURATION = 4;

	@Autowired
	EventLogDao eventLogDao;

	/**
	 * The handler method to instantiate the process of Logging the long running
	 * events
	 */
	@RequestMapping(value = "/logevents", method = RequestMethod.GET)
	public String logLongRunningEvents(Model model) {
		logger.info(" *** Staring the Process ***");
		List<LogFileParameters> listOfLongRunningEents = returnListofEventsToPersist();
		listOfLongRunningEents.forEach(event -> eventLogDao.logEvents(event, "true"));
		model.addAttribute("events", listOfLongRunningEents.toString());
		return "LongEvents";
	}
	
	/**
	 * This method returns the list  of long running events
	 * @return
	 */
	private List<LogFileParameters> returnListofEventsToPersist() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		List<LogFileParameters> param = new ArrayList<LogFileParameters>();
		List<LogFileParameters> finalList = new ArrayList<LogFileParameters>();

		try {
			Lists.newArrayList(Files.readAllLines(Paths.get(FILEPATH))).parallelStream().forEach(line -> {
				try {
					param.add(mapper.readValue(line, LogFileParameters.class));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			Map<String, List<LogFileParameters>> collect = param.parallelStream()
					.collect(Collectors.groupingBy(LogFileParameters::getId));
			collect.forEach((processID, processDeatils) -> {

				BigInteger endTime = new BigInteger(processDeatils.get(1).getTimestamp().trim());
				BigInteger startTime = new BigInteger(processDeatils.get(0).getTimestamp().trim());
				logger.info("The duration of the process  " + processDeatils.get(0).getId() + "  is "
						+ endTime.subtract(startTime));
				if ((endTime.subtract(startTime)).intValue() > THRESHOLD_DURATION) {
					processDeatils.get(0).setDuration(String.valueOf(endTime.subtract(startTime)));
					finalList.add(processDeatils.get(0));
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return finalList;
	}
}