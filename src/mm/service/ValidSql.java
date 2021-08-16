package mm.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import mm.common.RecordSetResultHandler;
import mm.repository.AbstractRepository;
import nexcore.framework.core.data.DataSet;
import nexcore.framework.core.data.IDataSet;
import nexcore.framework.core.data.IRecordSet;
import nexcore.framework.core.data.RecordSet;
import nexcore.framework.core.util.StringUtils;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import py0777.orasql.SQLBeautifier;

public class ValidSql extends AbstractRepository {
  static Logger logger = Logger.getLogger(SqlTranInq.class);
  
  private final String namespace = "mm.repository.mapper.ValidSqlMapper";
  
  public IDataSet s001(IDataSet requestData) {
    SqlSession sqlSession = getSqlSessionFactory().openSession();
    try {
      String statement = namespace+".S001";
      RecordSetResultHandler resultHandler = new RecordSetResultHandler();
      resultHandler.setRecordSetId("RS_MAPLIST");
      sqlSession.select(statement, requestData.getFieldMap(), (ResultHandler)resultHandler);
      DataSet dataSet = new DataSet();
      dataSet.putRecordSet(resultHandler.getRecordSet());
      return (IDataSet)dataSet;
    } finally {
      sqlSession.close();
    } 
  }
  
  public IDataSet s002(IDataSet requestData) {
    SqlSession sqlSession = getSqlSessionFactory().openSession();
    try {
      String statement = namespace+".S002";
      IRecordSet rs = null;
      RecordSetResultHandler resultHandler = new RecordSetResultHandler();
      resultHandler.setRecordSetId("RS_COLLIST");
      logger.debug("strat!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
      sqlSession.select(statement, requestData.getFieldMap(), (ResultHandler)resultHandler);
      DataSet dataSet = new DataSet();
      dataSet.putRecordSet(resultHandler.getRecordSet());
      return (IDataSet)dataSet;
    } finally {
      sqlSession.close();
    } 
  }
  
  public IDataSet validSql(IDataSet requestData) {
    logger.debug("###########  START #########");
    logger.debug(getClass().getName());
    logger.debug(requestData);
    DataSet dataSet = new DataSet();
    IDataSet dsTbl = null;
    IDataSet dsCol = null;
    IRecordSet rsTbl = null;
    IRecordSet rsCol = null;
    RecordSet recordSet = new RecordSet("RETURN_TBL");
    StringBuffer mapIdsB = new StringBuffer();
    String rtnMsg = "";
    StringBuffer sb = new StringBuffer();
    String today = "";
    GregorianCalendar gc = new GregorianCalendar();
    SimpleDateFormat sdformat = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss초 E(a)");
    try {
      Date d = gc.getTime();
      today = sdformat.format(d);
      if (requestData.getField("TABLE_NAME").isEmpty() || "NULL".equals(requestData.getField("TABLE_NAME"))) {
        rtnMsg = "테이블을 입력하세요";
        dataSet.putField("rtnMsg", String.valueOf(today) + " " + rtnMsg);
        return (IDataSet)dataSet;
      } 
      String[] inputMapIdTbl = requestData.getFieldValues("MAP_ID");
      logger.debug("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆");
      logger.debug(inputMapIdTbl[0]);
      logger.debug("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆");
      if (StringUtils.isEmpty(inputMapIdTbl[0]) || "null".equals(inputMapIdTbl[0]) || "NULL".equals(inputMapIdTbl[0])) {
    	  logger.debug("MAP_ID %");
        requestData.putField("MAP_ID", "'%'");
      } else {
        for (int j = 0; j < inputMapIdTbl.length; j++) {
          String tobeMapId = inputMapIdTbl[j];
          mapIdsB.append("'");
          mapIdsB.append(tobeMapId);
          mapIdsB.append("'");
          mapIdsB.append(",");
        } 
        if (mapIdsB.length() > 0) {
          requestData.putField("MAP_ID", mapIdsB.delete(mapIdsB.length() - 1, mapIdsB.length()).toString());
        } else {
          requestData.putField("MAP_ID", "'%'");
        } 
      } 
      logger.debug(requestData);
      dsTbl = s001(requestData);
      rsTbl = dsTbl.getRecordSet("RS_MAPLIST");
      logger.debug(rsTbl);
      dataSet.putField("rsCnt", rsTbl.getRecordCount());
      if (rsTbl.getRecordCount() == 0) {
        rtnMsg = "조회내역이 없습니다.";
        dataSet.putField("rtnMsg", String.valueOf(today) + " " + rtnMsg);
        return (IDataSet)dataSet;
      } 
      int i;
      for (i = 0; i < rsTbl.getHeaderCount(); i++)
        recordSet.addHeader(i, rsTbl.getHeader(i)); 
      for (i = 0; i < rsTbl.getRecordCount(); i++) {
        recordSet.addRecord(rsTbl.getRecord(i));
        requestData.putField("MAP_ID", rsTbl.get(i, "MAP_ID"));
        dsCol = s002(requestData);
        rsCol = dsCol.getRecordSet("RS_COLLIST");
        if (rsCol.getRecordCount() > 0) {
          sb.append("/* " + rsCol.get(0, "A_ENG_TABLE_NAME") + "   MINUS 검증 */ \n\n\n");
          sb.append("SELECT ");
          int j;
          for (j = 0; j < rsCol.getRecordCount(); j++)
            sb.append(String.valueOf(rsCol.get(j, "T_ENG_COLUMN_NAME")) + "\n"); 
          sb.append("FROM ");
          sb.append(String.valueOf(rsCol.get(0, "T_ENG_TABLE_NAME")) + "    /*" + rsCol.get(0, "T_KOR_TABLE_NAME") + "*/");
          sb.append("\n");
          sb.append("MINUS\n");
          sb.append("SELECT ");
          for (j = 0; j < rsCol.getRecordCount(); j++)
            sb.append(String.valueOf(rsCol.get(j, "A_ENG_COLUMN_NAME")) + "\n"); 
          sb.append("FROM ");
          sb.append(String.valueOf(rsCol.get(0, "A_ENG_TABLE_NAME")) + "/*" + rsCol.get(0, ", A_KOR_TABLE_NAME") + "*/");
          sb.append("; \n\n\n");
          sb.append("/* " + rsCol.get(0, "A_ENG_TABLE_NAME") + "건수 검증 */ \n\n");
          sb.append("SELECT COUNT(*) AS TOBE_CNT \n");
          sb.append(",0 AS ASIS_CNT  \n");
          sb.append("FROM  ");
          sb.append(String.valueOf(rsCol.get(0, "T_ENG_TABLE_NAME")) + "/*" + rsCol.get(0, "T_KOR_TABLE_NAME") + "*/");
          sb.append("\n");
          sb.append("UNION ALL");
          sb.append("\n");
          sb.append("SELECT 0 AS TOBE_CNT  \n");
          sb.append(",COUNT(*) AS ASIS_CNT  \n");
          sb.append("FROM ");
          sb.append(String.valueOf(rsCol.get(0, "A_ENG_TABLE_NAME")) + "/*" + rsCol.get(0, "A_KOR_TABLE_NAME") + "*/");
          sb.append("\n");
          sb.append(";\n\n\n\n\n");
        } 
      } 
      logger.debug("rsTblRtn : " + recordSet);
      SQLBeautifier sbf = new SQLBeautifier();
      dataSet.putField("RESULT", SQLBeautifier.beautify(String.valueOf(sb)));
      dataSet.putField("rsTblCnt", recordSet.getRecordCount());
      dataSet.putRecordSet("rsTblRtn", (IRecordSet)recordSet);
      dataSet.putRecordSet("rsTbl", rsTbl);
    } catch (Exception e) {
      e.printStackTrace();
    } 
    if (recordSet.getRecordCount() == 0) {
      rtnMsg = "입력한 테이블 또는 MAP_ID가 존재하지 않습니다";
    } else {
      rtnMsg = "조회 완료되었습니다.";
    } 
    dataSet.putField("rtnMsg", String.valueOf(today) + " " + rtnMsg);
    return (IDataSet)dataSet;
  }
}
