package com.piyalexercise.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import com.piyalexercise.model.LogFileParameters;



@Repository
public class EventLogDaoImpl implements EventLogDao {

	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}
	
	@Override
	public LogFileParameters findByEventId(String eventid) {
		
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("eventid", eventid);
        
		String selectQueryString = "SELECT * FROM eventlog WHERE eventid=:eventid";
		
        LogFileParameters result = namedParameterJdbcTemplate.queryForObject(
        		selectQueryString,
                    params,
                    new EventMapper());
        return result;
	}

	@Override
	public void  logEvents(LogFileParameters logFileParameters, String alert) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		String insertQueryString = "INSERT INTO eventlog (eventid,duration,typeofevent,host,alert) VALUES (:eventid,:duration,:typeofevent,:host,:alert)";
		params.put("eventid", logFileParameters.getId());
		params.put("duration", logFileParameters.getDuration());
		params.put("typeofevent", logFileParameters.getType());
		params.put("host", logFileParameters.getHost());
		params.put("alert",alert );
        namedParameterJdbcTemplate.update(insertQueryString, params);
	}
	
	private static final class EventMapper implements RowMapper<LogFileParameters> {
		@Override
		public LogFileParameters mapRow(ResultSet rs, int rowNum) throws SQLException {
			LogFileParameters event = new LogFileParameters();
			event.setId(rs.getString("eventid"));
			event.setDuration(rs.getString("duration"));
			event.setType(rs.getString("typeofevent"));
			event.setHost("host");
			
			return event;
		}
	}
	

}