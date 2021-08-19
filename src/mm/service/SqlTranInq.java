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

public class SqlTranInq extends AbstractRepository {
  static Logger logger = Logger.getLogger(SqlTranInq.class);
  
  
  public IDataSet s001(IDataSet requestData) {
    SqlSession sqlSession = getSqlSessionFactory().openSession();
    try {
      String statement = "mm.repository.mapper.SqlTranInqMapper.S001";
      IRecordSet rs = null;
      RecordSetResultHandler resultHandler = new RecordSetResultHandler();
      resultHandler.setRecordSetId("RS_TBLLIST");
      logger.debug("strat!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
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
      String statement = "mm.repository.mapper.SqlTranInqMapper.S002";
      RecordSetResultHandler resultHandler = new RecordSetResultHandler();
      resultHandler.setRecordSetId("RS_COLLIST");
      sqlSession.select(statement, requestData.getFieldMap(), (ResultHandler)resultHandler);
      DataSet dataSet = new DataSet();
      dataSet.putRecordSet(resultHandler.getRecordSet());
      return (IDataSet)dataSet;
    } finally {
      sqlSession.close();
    } 
  }
  
  public IDataSet asSqlTrnInq(IDataSet requestData) {
    logger.debug("###########  START #########");
    logger.debug(getClass().getName());
    logger.debug(requestData);
    logger.debug(requestData.getFieldValues("TOBE_TBL_LIST"));
    DataSet dataSet = new DataSet();
    IDataSet dsTbl = null;
    IDataSet dsCol = null;
    IRecordSet rsTbl = null;
    IRecordSet rsCol = null;
    RecordSet recordSet1 = new RecordSet("RS_LIST");
    RecordSet recordSet2 = new RecordSet("RETURN_TBL");
    RecordSet recordSet3 = new RecordSet("RETURN_TBL_COL");
    String rtnMsg = "";
    String query = "";
    String s_asisTbl = "";
    String s_tobeTbl = "";
    String s_asisCol = "";
    String s_tobeCol = "";
    String s_tobeColNm = "";
    
    StringBuffer sb = new StringBuffer();
    String[] parseReg = { 
        "\\.", "\\;", "\\(", "\\)", "\\,", "\\|", "=", " ", "\r\n", "\r", 
        "\n", "@" };
    String[] parseReg2 = { 
        "~~\\.~~", "~~\\;~~", "~~\\(~~", "~~\\)~~", "~~\\,~~", "~~|~~", "~~=~~", "~~ ~~", 
        "~~\r\n~~", "~~\r~~", 
        "~~\n~~", "~~@~~" };
    String today = "";
    GregorianCalendar gc = new GregorianCalendar();
	SimpleDateFormat sdformat = new SimpleDateFormat("YYYY년 MM월 dd일 HH:mm:ss초 E(a)");
    try {
      Date d = gc.getTime();
      today = sdformat.format(d);
      query = requestData.getField("QUERY");
      dsTbl = s001(requestData);
      rsTbl = dsTbl.getRecordSet("RS_TBLLIST");
      query = query.toUpperCase();
      query = query.replaceAll("\t", "    ");
      int i;
      for (i = 0; i < parseReg.length; i++)
        query = query.replaceAll(parseReg[i], parseReg2[i]); 
      logger.debug(query);
      String[] tmp = query.split("~~");
      logger.debug(tmp);
      String[] inputTobeTbl = requestData.getFieldValues("TOBE_TBL_LIST");
      for (i = 0; i < rsTbl.getHeaderCount(); i++)
        recordSet2.addHeader(i, rsTbl.getHeader(i)); 
     
      for (int j = 0; j < rsTbl.getRecordCount(); j++) {
        s_asisTbl = rsTbl.getRecord(j).get("TBL").trim();
        for (int m = 0; m < tmp.length; m++) {
          if (StringUtils.equals(tmp[m], s_asisTbl) && !StringUtils.equals(tmp[m], "SY"))
            if (StringUtils.isEmpty(inputTobeTbl[0])) {
              recordSet2.addRecord(rsTbl.getRecord(j));
            } else {
              for (int n = 0; n < inputTobeTbl.length; n++) {
                if (!StringUtils.isEmpty(inputTobeTbl[n]))
                  if (inputTobeTbl[n].equals(rsTbl.getRecord(j).get("ATBL").trim()))
                    recordSet2.addRecord(rsTbl.getRecord(j));  
              } 
            }  
        } 
      } 
      logger.debug("rsTblRtn : " + recordSet2);
      StringBuffer asisTblSB = new StringBuffer();
      StringBuffer tobeTblSB = new StringBuffer();
      int k;
      for (k = 0; k < recordSet2.getRecordCount(); k++) {
        String asisTbl = recordSet2.get(k, "TBL");
        asisTblSB.append("'");
        asisTblSB.append(asisTbl);
        asisTblSB.append("'");
        asisTblSB.append(",");
        String tobeTbl = recordSet2.get(k, "ATBL");
        tobeTblSB.append("'");
        tobeTblSB.append(tobeTbl);
        tobeTblSB.append("'");
        tobeTblSB.append(",");
      } 
      if (asisTblSB.length() > 0) {
        requestData.putField("ASIS_TBL_LIST", asisTblSB.delete(asisTblSB.length() - 1, asisTblSB.length()).toString());
        requestData.putField("TOBE_TBL_LIST", tobeTblSB.delete(tobeTblSB.length() - 1, tobeTblSB.length()).toString());
      } else {
        requestData.putField("ASIS_TBL_LIST", "''");
        requestData.putField("TOBE_TBL_LIST", "''");
      } 
      dsCol = s002(requestData);
      rsCol = dsCol.getRecordSet("RS_COLLIST");
      for (k = 0; k < rsCol.getHeaderCount(); k++) {
        recordSet1.addHeader(k, rsCol.getHeader(k));
        recordSet3.addHeader(k, rsCol.getHeader(k));
      } 
      logger.debug("=========================");
      logger.debug(rsCol);
      logger.debug("tmp.length : " + tmp.length);
      logger.debug("rsCol.getRecordCount() : " + rsCol.getRecordCount());
      for (k = 0; k < rsCol.getRecordCount(); k++) {
        s_asisCol = rsCol.getRecord(k).get("COL").trim();
        s_tobeCol = rsCol.getRecord(k).get("ACOL");
        s_tobeColNm = rsCol.getRecord(k).get("ACOLNM");
        
        for (int m = 0; m < tmp.length; m++) {
          if (!StringUtils.isEmpty(s_asisCol) && 
            !StringUtils.isEmpty(s_tobeCol))
            if (StringUtils.equals(tmp[m], s_asisCol)) {
              
              logger.debug(s_asisCol);
              logger.debug(s_tobeTbl);
              logger.debug(rsCol.getRecord(k).get("ATBL"));
              tmp[m] = tmp[m].replaceAll(s_asisCol, s_tobeCol);
              if ("Y".equals(requestData.getField("COMMENT_YN")))
                tmp[m] = tmp[m].concat("/*").concat(s_tobeColNm)
                  .concat("*/"); 
              recordSet1.addRecord(rsCol.getRecord(k));
              if (!s_tobeTbl.equals(rsCol.getRecord(k).get("ATBL"))) {
                recordSet3.addRecord(rsCol.getRecord(k));
                s_tobeTbl = rsCol.getRecord(k).get("ATBL");
              } 
            } else if (tmp[m].contains(":")) {
              
              tmp[m] = tmp[m].toLowerCase();
            }  
        } 
      } 
      logger.debug(recordSet2);
      for (k = 0; k < recordSet2.getRecordCount(); k++) {
        s_asisTbl = recordSet2.getRecord(k).get("TBL").trim();
        for (int m = 0; m < tmp.length; m++) {
          if (StringUtils.equals(tmp[m], s_asisTbl))
            tmp[m] = tmp[m].replaceAll(s_asisTbl, recordSet2.getRecord(k).get("ATBL").concat("  /*")
                .concat(recordSet2.getRecord(k).get("ATBLNM")).concat("*/")); 
        } 
      } 
      for (int u = 0; u < tmp.length; u++)
        sb.append(tmp[u]); 
      SQLBeautifier sbf = new SQLBeautifier();
      dataSet.putField("QUERY", "");
      if ("Y".equals(requestData.getField("SQLFORMAT_YN"))) {
        dataSet.putField("RESULT", SQLBeautifier.beautify(String.valueOf(sb)).trim());
      } else {
        dataSet.putField("RESULT", String.valueOf(sb).trim());
      } 
      dataSet.putField("rsCnt", recordSet1.getRecordCount());
      dataSet.putField("rsTblCnt", recordSet2.getRecordCount());
      dataSet.putRecordSet("rsTblRtn", (IRecordSet)recordSet2);
      dataSet.putRecordSet("rsRtn", (IRecordSet)recordSet1);
      
      if (recordSet2.getRecordCount() == 0) {
          rtnMsg = "입력한 테이블이 존재하지 않습니다.";
        } else {
          rtnMsg = "조회 완료되었습니다.";
        }
      dataSet.putField("rtnMsg", String.valueOf(today) + " " + rtnMsg);
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    dataSet.setOkResultMessage("NCOM0000", null);
    return (IDataSet)dataSet;
  }
}
