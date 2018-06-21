DROP TABLE eventlog IF EXISTS;

CREATE TABLE eventlog (
  eventid     VARCHAR(30) PRIMARY KEY,
  duration VARCHAR(30),
  typeofevent  VARCHAR(30),
  host VARCHAR(30),
  alert VARCHAR(10)
);
