
 
drop table META_TABLE_MAP;

/*META_TABLE_MAP*/
create table META_TABLE_MAP(
  ID                       int not null primary key auto_increment               
, MAP_SORT                 int not null 
, MAP_ID                   VARCHAR(50) 
, T_SYSTEM_NAME            VARCHAR(50) 
, T_OWNER                  VARCHAR(50) 
, T_ENG_TABLE_NAME         VARCHAR(50) 
, T_KOR_TABLE_NAME         VARCHAR(50) 
, T_ENG_COLUMN_NAME        VARCHAR(50) 
, T_KOR_COLUMN_NAME        VARCHAR(50) 
, T_DATA_TYPE              VARCHAR(50) 
, T_LENGTH1                VARCHAR(50) 
, T_LENGTH2                VARCHAR(50) 
, T_PK                     VARCHAR(50) 
, A_SYSTEM_NAME            VARCHAR(50) 
, A_OWNER                  VARCHAR(50) 
, A_ENG_TABLE_NAME         VARCHAR(50) 
, A_KOR_TABLE_NAME         VARCHAR(50) 
, A_ENG_COLUMN_NAME        VARCHAR(50) 
, A_KOR_COLUMN_NAME        VARCHAR(50) 
, A_DATA_TYPE              VARCHAR(50) 
, A_LENGTH1                VARCHAR(10) 
, A_LENGTH2                VARCHAR(10) 
, A_PK                     VARCHAR(50) 
, MOVE_DEFAULT             VARCHAR(50) 
, MOVE_YN                  VARCHAR(50) 
, MOVE_RULE                VARCHAR(50) 
, MOVE_SQL                 VARCHAR(50) 
, ALT_EMP_NO               VARCHAR(50) 
, PREE_CDTN                VARCHAR(50) 
, ALT_DT                   VARCHAR(50) 
, JOB_OWNER                VARCHAR(50) 
, CLIENT_OWNER             VARCHAR(50) 
, MOVE_OWNER               VARCHAR(50) 

);
ALTER TABLE META_TABLE_MAP ADD CONSTRAINT pk_META_TABLE_MAP PRIMARY KEY (ID);

GRANT SELECT, INSERT, UPDATE, DELETE ON META_TABLE_MAP TO py0777;

create table META_CODE_MAP(
  ID                     int not null primary key auto_increment               
, NO                     int not null 
, WORK_NAME              VARCHAR(50) 
, T_ENG_COLUMN_NAME      VARCHAR(50) 
, T_CODE_NAME            VARCHAR(50) 
, T_CODE_CD              VARCHAR(50) 
, T_CMT                  VARCHAR(50) 
, T_REMARK               VARCHAR(50) 
, A_KCODE_NAME           VARCHAR(50) 
, A_ECODE_NAME           VARCHAR(50) 
, A_CODE_CD              VARCHAR(50) 
, A_CMT                  VARCHAR(50) 
, A_REMARK               VARCHAR(50) 
, 업무담당자             VARCHAR(50) 
, 고객담당자             VARCHAR(50) 
, 이행담당자             VARCHAR(50) 
);
ALTER TABLE META_CODE_MAP ADD CONSTRAINT pk_META_CODE_MAP PRIMARY KEY (ID);

GRANT SELECT, INSERT, UPDATE, DELETE ON META_CODE_MAP TO ys2613;
