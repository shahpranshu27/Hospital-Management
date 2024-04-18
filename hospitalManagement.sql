create database hospital;

use hospital;

create table patients(
id int auto_increment primary key,
name varchar(255) not null,
age int not null,
gender varchar(10) not null
);

create table doctors(
id int auto_increment primary key,
name varchar(255) not null,
specialization varchar(255) not null
);

create table appointment(
id int auto_increment primary key,
patient_id int not null,
doctor_id int not null,
appointment_date date not null,
foreign key (patient_id) references patients(id),
foreign key (doctor_id) references doctors(id)
);

show tables;

insert into doctors(name, specialization) values("Siddh Shah", "Physician");

insert into doctors(name, specialization) values("Tathya Kamdar", "Neurosurgeon");

select * from doctors;

insert into appointment(patient_id, doctor_id, appointment_date) values(1, 1, "2024-10-10");

select * from appointment;