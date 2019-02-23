create table fonbet.timeline_table
(
  date_update timestamp not null,
  pair varchar(128),
  href varchar(256),
  start_time varchar(32),
  c_first double precision,
  c_second double precision,
  f_first double precision,
  cf_first double precision,
  f_second double precision,
  cf_second double precision,
  total double precision,
  b double precision,
  m double precision
);

create table fonbet.live_match_table
(
  date_update timestamp not null,
  pairName varchar(128),
  pair varchar(128),
  href varchar(256),
  dop varchar(128),
  c_first double precision,
  c_second double precision,
  f_first double precision,
  cf_first double precision,
  f_second double precision,
  cf_second double precision,
  total double precision,
  b double precision,
  m double precision
);