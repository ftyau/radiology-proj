/*
 *  to store olap information
 */
CREATE OR REPLACE VIEW olap_view AS
SELECT r.patient_id, r.test_type, r.test_date, pi.image_id
FROM radiology_record r FULL JOIN pacs_images pi ON r.record_id = pi.record_id;


/*
 * create default admin account
 */
INSERT INTO persons VALUES (0,  'Administrator', '', '', '', '');
INSERT INTO users VALUES ('admin', '111', 'a', '0', sysdate);

/*
 *  to create a sequence for image ids
 */
DROP SEQUENCE pic_id_sequence;
CREATE SEQUENCE pic_id_sequence;

/*
 * to create indexes for search 
 */
CREATE INDEX description_index ON radiology_record(description) INDEXTYPE IS CTXSYS.CONTEXT;
CREATE INDEX diagnosis_index ON radiology_record(diagnosis) INDEXTYPE IS CTXSYS.CONTEXT; 
CREATE INDEX prescribing_index ON radiology_record(prescribing_date); 
CREATE INDEX test_index ON radiology_record(test_date); 
CREATE INDEX first_name_index ON persons(first_name) INDEXTYPE IS CTXSYS.CONTEXT; 
CREATE INDEX last_name_index ON persons(last_name) INDEXTYPE IS CTXSYS.CONTEXT; 

/*
 * to update indexes whenever possible 
 */
declare job number;
begin
  dbms_job.submit(job, 'ctx_ddl.sync_index(''description_index'');',
                  interval=>'SYSDATE+1/1440');
  dbms_job.submit(job, 'ctx_ddl.sync_index(''diagnosis_index'');',
                  interval=>'SYSDATE+1/1440');
  dbms_job.submit(job, 'ctx_ddl.sync_index(''prescribing_index'');',
                  interval=>'SYSDATE+1/1440');
  dbms_job.submit(job, 'ctx_ddl.sync_index(''test_index'');',
                  interval=>'SYSDATE+1/1440');
  dbms_job.submit(job, 'ctx_ddl.sync_index(''first_name_index'');',
                  interval=>'SYSDATE+1/1440');
  dbms_job.submit(job, 'ctx_ddl.sync_index(''last_name_index'');',
                  interval=>'SYSDATE+1/1440');
  commit;
end;
