/*
 *  File name:  setup.sql
 *  Function:   to create the initial database schema for the CMPUT 391 project,
 *              Winter Term, 2014
 */
DROP TABLE family_doctor;
DROP TABLE pacs_images;
DROP TABLE radiology_record;
DROP TABLE users;
DROP TABLE persons;

/*
 *  To store the personal information
 */
CREATE TABLE persons (
   person_id int,
   first_name varchar(24),
   last_name  varchar(24),
   address    varchar(128),
   email      varchar(128),
   phone      char(10),
   PRIMARY KEY(person_id),
   UNIQUE (email)
);

/*
 *  To store the log-in information
 *  Note that a person may have been assigned different user_name(s), depending
 *  on his/her role in the log-in  
 */
CREATE TABLE users (
   user_name varchar(24),
   password  varchar(24),
   class     char(1),
   person_id int,
   date_registered date,
   CHECK (class in ('a','p','d','r')),
   PRIMARY KEY(user_name),
   FOREIGN KEY (person_id) REFERENCES persons
);

/*
 *  to indicate who is whose family doctor.
 */
CREATE TABLE family_doctor (
   doctor_id    int,
   patient_id   int,
   FOREIGN KEY(doctor_id) REFERENCES persons,
   FOREIGN KEY(patient_id) REFERENCES persons,
   PRIMARY KEY(doctor_id,patient_id)
);

/*
 *  to store the radiology records
 */
CREATE TABLE radiology_record (
   record_id   int,
   patient_id  int,
   doctor_id   int,
   radiologist_id int,
   test_type   varchar(24),
   prescribing_date date,
   test_date    date,
   diagnosis    varchar(128),
   description   varchar(1024),
   PRIMARY KEY(record_id),
   FOREIGN KEY(patient_id) REFERENCES persons,
   FOREIGN KEY(doctor_id) REFERENCES  persons,
   FOREIGN KEY(radiologist_id) REFERENCES  persons
);

/*
 *  to store the pacs images
 */
CREATE TABLE pacs_images (
   record_id   int,
   image_id    int,
   thumbnail   blob,
   regular_size blob,
   full_size    blob,
   PRIMARY KEY(record_id,image_id),
   FOREIGN KEY(record_id) REFERENCES radiology_record
);

/*
 *  to store olap information
 */
CREATE OR REPLACE VIEW olap_view AS
SELECT r.patient_id, r.test_type, r.test_date, pi.image_id
FROM radiology_record r FULL JOIN pacs_images pi ON r.record_id = pi.record_id;


/*
 * create default admin account
 */
INSERT INTO persons (0,  'Administrator', '', '', '', '');
INSERT INTO users ('admin', '111', 'a', '0', sysdate);

/*
 *  to create a sequence for image ids
 */
CREATE SEQUENCE pic_id_sequence;

/*
 * to create indexes for search 
 */
CREATE INDEX description_index ON radiology_record(description) INDEXTYPE IS CTXSYS.CONTEXT;
CREATE INDEX diagnosis_index ON radiology_record(diagnosis) INDEXTYPE IS CTXSYS.CONTEXT; 
CREATE INDEX prescribing_index ON radiology_record(prescribing_date) INDEXTYPE IS CTXSYS.CONTEXT; 
CREATE INDEX test_index ON radiology_record(test_date) INDEXTYPE IS CTXSYS.CONTEXT; 
CREATE INDEX first_name_index ON persons(first_name) INDEXTYPE IS CTXSYS.CONTEXT; 
CREATE INDEX last_name_index ON persons(last_name) INDEXTYPE IS CTXSYS.CONTEXT; 

/*
 * to update indexes whenever possible 
 */
set serveroutput on
declare
  job number;
begin
  dbms_job.submit(job, 'ctx_ddl.sync_index(''description_index'');',
                  interval=>'SYSDATE+1/1440');
  commit;
  dbms_output.put_line('job '||job||' has been submitted.');
end;

set serveroutput on
declare
  job number;
begin
  dbms_job.submit(job, 'ctx_ddl.sync_index(''diagnosis_index'');',
                  interval=>'SYSDATE+1/1440');
  commit;
  dbms_output.put_line('job '||job||' has been submitted.');
end;

set serveroutput on
declare
  job number;
begin
  dbms_job.submit(job, 'ctx_ddl.sync_index(''prescibing_index'');',
                  interval=>'SYSDATE+1/1440');
  commit;
  dbms_output.put_line('job '||job||' has been submitted.');
end;

set serveroutput on
declare
  job number;
begin
  dbms_job.submit(job, 'ctx_ddl.sync_index(''test_index'');',
                  interval=>'SYSDATE+1/1440');
  commit;
  dbms_output.put_line('job '||job||' has been submitted.');
end;

set serveroutput on
declare
  job number;
begin
  dbms_job.submit(job, 'ctx_ddl.sync_index(''first_name_index'');',
                  interval=>'SYSDATE+1/1440');
  commit;
  dbms_output.put_line('job '||job||' has been submitted.');
end;

set serveroutput on
declare
  job number;
begin
  dbms_job.submit(job, 'ctx_ddl.sync_index(''last_name_index'');',
                  interval=>'SYSDATE+1/1440');
  commit;
  dbms_output.put_line('job '||job||' has been submitted.');
end;