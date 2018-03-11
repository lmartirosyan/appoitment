# Appoitment
This project is rest api which  provides the possibility to schedule appointments for facility customers.

## Getting Started
1. Set up maven project
2. Clone Appointment project from git repo.
3. The entry point of application is org.app.controller.Application class. So you should run Application class.
4. You should set up Postgresql database credentials of database you can find in hibernate.xml.
5. Create database requiered db tables facility, customer,appointment, facility_free_schedule (see in DB Queries section)

##DB Queries
create table facility
(
	email varchar(200) not null,
	address varchar(200) not null,
	working_hours varchar(200) not null,
	empty_spot integer not null,
	per_hour integer not null,
	id serial not null
		constraint facility_column_6_pk
			primary key
)
;

create unique index facility_column_6_uindex
	on facility (id)
;
##########################################

create table customer
(
	name varchar(200) not null,
	email varchar(200) not null,
	id serial not null
		constraint customer_id_pk
			primary key
)
;

create unique index customer_id_uindex
	on customer (id)
;
########################################
create table appointment
(
	user_name varchar(200) not null,
	user_email varchar(200) not null,
	facility_address varchar(200) not null,
	start_date_time varchar(50) not null,
	end_date_time varchar(50),
	id serial not null
		constraint appointment_id_pk
			primary key
)
;

create unique index appointment_id_uindex
	on appointment (id)
;

################################
create table facility_free_schedule
(
	id serial not null
		constraint facility_free_schedule_pkey
			primary key,
	facility_id integer not null
		constraint facility_free_schedule_facility_id_fk
			references facility (id)
				on delete cascade,
	capacity integer not null,
	empty_spot_timestamp bigint not null,
	week_index integer not null
)
;

create unique index facility_free_schedule_id_uindex
	on facility_free_schedule (id)
;


## Technology stack
1. Java
2. Spring
3. Hibernate
4. Posgresql
5. Maven

### Running the Project
1. Run Application class
2. SE
3.
