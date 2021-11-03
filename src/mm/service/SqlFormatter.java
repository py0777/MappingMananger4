package mm.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import mm.common.RecordSetResultHandler;
import mm.repository.AbstractRepository;
import nexcore.framework.core.data.DataSet;
import nexcore.framework.core.data.IDataSet;
import nexcore.framework.core.data.IRecordSet;
import py0777.orasql.SQLBeautifier;

public class SqlFormatter extends AbstractRepository {
  static Logger logger = Logger.getLogger(SqlFormatter.class);
  
  private final String namespace = "mm.repository.mapper.SqlTranInqMapper";
  
  public IDataSet s003(IDataSet requestData) {
    SqlSession sqlSession = getSqlSessionFactory().openSession();
    try {
      String statement = "mm.repository.mapper.SqlTranInqMapper.S003";
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
  
  public IDataSet sqlFormatter(IDataSet requestData) {
    logger.debug("###########  START #########");
    logger.debug(getClass().getName());
    logger.debug(requestData);
    DataSet dataSet = new DataSet();
    String rtnMsg = "";
    String query = "";
    String camelYn = "N";
    String today = "";
    GregorianCalendar gc = new GregorianCalendar();
    SimpleDateFormat sdformat = new SimpleDateFormat("YYYY년 MM월 dd일 HH:mm:ss초 E(a)");
    try {
      Date d = gc.getTime();
      today = sdformat.format(d);
      query = requestData.getField("QUERY");
      camelYn = requestData.getField("CAMEL_YN");
      logger.debug(query);
      String resultSql = null;
      //SQLBeautifier sbf = new SQLBeautifier();
      
//      resultSql = JSQLFormatter.format(query.toUpperCase());
      resultSql = SQLBeautifier.beautify(query.toUpperCase());
      
      dataSet.putField("RESULT", resultSql);
      
      rtnMsg = "조회 완료되었습니다.";
      dataSet.putField("rtnMsg", String.valueOf(today) + " " + rtnMsg);
    } catch (Exception e) {
      e.printStackTrace();
    } 
    
    return (IDataSet)dataSet;
  }
}
