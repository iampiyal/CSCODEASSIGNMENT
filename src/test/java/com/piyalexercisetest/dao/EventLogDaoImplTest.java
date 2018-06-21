package com.piyalexercisetest.dao;

import java.io.IOException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.piyalexercise.dao.EventLogDao;
import com.piyalexercise.dao.EventLogDaoImpl;
import com.piyalexercise.model.LogFileParameters;

/**
 * Test Class to test the Database methods
 * 
 * @author piyal
 *
 */
public class EventLogDaoImplTest {

	private EmbeddedDatabase db;

	EventLogDao eventLogDao;

	@Before
	public void setUp() {

		db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL).addScript("db/sql/create-db.sql")
				.addScript("db/sql/insert-data.sql").build();
	}

	@Test
	public void findByEventIdTest() throws JsonProcessingException, IOException {
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db);
		EventLogDaoImpl eventLogDao = new EventLogDaoImpl();
		eventLogDao.setNamedParameterJdbcTemplate(template);
		LogFileParameters logFileParameters = eventLogDao.findByEventId("testvalue1");
		Assert.assertNotNull(logFileParameters);
		Assert.assertEquals("testvalue1", logFileParameters.getId());
		Assert.assertEquals("5.0", logFileParameters.getDuration());
	}

	@After
	public void tearDown() {
		db.shutdown();
	}

}