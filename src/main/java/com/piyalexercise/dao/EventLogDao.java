package com.piyalexercise.dao;

import com.piyalexercise.model.LogFileParameters;

public interface EventLogDao {

	public LogFileParameters findByEventId(String name);

	public void logEvents(LogFileParameters loFileParameters,String alert);

}