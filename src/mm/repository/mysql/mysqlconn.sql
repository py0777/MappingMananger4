SELECT no                           순번, 
       work_name                    업무영역, 
       B.t_eng_column_name          AS TOBE영문코드명, 
       Ifnull(t_code_name, 'NULL')  TOBE코드명, 
       Ifnull(t_code_cd, 'NULL ')   TOBE코드값, 
       Ifnull(t_cmt, 'NULL')        TOBE설명, 
       Ifnull(t_remark, 'NULL')     TOBE비고, 
       Ifnull(a_kcode_name, 'NULL') ASIS한글컬럼명, 
       Ifnull(a_ecode_name, 'NULL') ASIS영문컬럼명, 
       Ifnull(a_code_cd, 'NULL')    ASIS코드값, 
       Ifnull(a_cmt, 'NULL')        ASIS설명, 
       Ifnull(a_remark, 'NULL')     ASIS비고, 
       업무담당자, 
       고객담당자, 
       이행담당자 
FROM   meta_code_map A, 
       (SELECT DISTINCT t_eng_column_name, 
                        t_kor_column_name 
        FROM   meta_table_map) B 
WHERE  A.t_code_name = B.t_kor_column_name 
 AND B.t_eng_column_name LIKE CONCAT('%','JOB','%')
