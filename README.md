# Appoitment
This project is rest api which  provides the possibility to schedule appointments for facility customers.

## Getting Started
### Project set up
1. Set up maven project
2. Clone Appointment project from git repo.
3. Set up Postgresql database credentials of database you can find in hibernate.xml.
4. Create database requiered db tables facility, customer,appointment, facility_free_schedule (see in DB Queries section)

### DB Queries
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
Run Application class and set up Postman or other requester tool
 
####First Request Congiuration
1. Confiure POST request with url: http://127.0.0.1:8080/api/metadata
2. Configure headers for basic authentification: user- admin, pass-admin
3. Configure heasers to Accept application/json.
4. Configure Body params:
		key: customers, value: list-of-custmers.csv (csv file located in /csv/list-of-custmers.csv )
		key: facilities, value: list-of-facilities.csv (csv file located in /csv/list-of-facilities.csv)
5. After submiting request you should receive smth like this:
	{
	    "customers": [
		{
		    "id": 80,
		    "name": "Alex",
		    "email": "alex@somewhere.com"
		},
		{
		    "id": 81,
		    "name": "Anna",
		    "email": "anna@somewhere.com"
		},
		{
		    "id": 82,
		    "name": "David",
		    "email": "david@somewhere.com"
		},
		{
		    "id": 83,
		    "name": "Ben",
		    "email": "ben@somewhere.com"
		},
		{
		    "id": 84,
		    "name": "Bee",
		    "email": "bee@somewhere.com"
		},
		{
		    "id": 85,
		    "name": "Dan",
		    "email": "dan@somewhere.com"
		}
	    ],
	    "facilities": [
		{
		    "id": 38,
		    "email": "facility1@somewhere.com",
		    "address": "Armenia,Yerevan Saryan1 street 28",
		    "workingHours": "09:00:00-18:00:00",
		    "emptySpot": 10,
		    "perHour": 5
		},
		{
		    "id": 39,
		    "email": "facility2@somewhere.com",
		    "address": "Armenia,Yerevan Saryan2 street 28",
		    "workingHours": "10:00:00-19:00:00",
		    "emptySpot": 5,
		    "perHour": 8
		},
		{
		    "id": 40,
		    "email": "facility3@somewhere.com",
		    "address": "Armenia,Yerevan Saryan3 street 28",
		    "workingHours": "11:00:00-18:00:00",
		    "emptySpot": 2,
		    "perHour": 3
		}
	    ]
	}
		
