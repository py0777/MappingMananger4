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
import nexcore.framework.core.data.RecordSet;
import py0777.orasql.SQLBeautifier;


public class DefaultSql extends AbstractRepository {
	static Logger logger = Logger.getLogger(DefaultSql.class);
  
	private final String namespace = "mm.repository.mapper.DefaultSqlMapper";
  
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
  
	/**
	 * @param requestData
	 * @return
	 */
	public IDataSet defaultSql(IDataSet requestData) {
		logger.debug("###########  START #########");
		logger.debug(getClass().getName());
		logger.debug(requestData);
		
		DataSet dataSet = new DataSet();
		IDataSet dsTbl = null;
		IRecordSet rsTbl = null;
		RecordSet recordSet = new RecordSet("RETURN_TBL");
		String rtnMsg = "";
		StringBuffer selectSb = new StringBuffer();
		StringBuffer updateSb = new StringBuffer();
		StringBuffer deleteSb = new StringBuffer();
		StringBuffer insertSb = new StringBuffer();
		
		StringBuffer totSb = new StringBuffer();
		
		SQLBeautifier  stf	=	 new SQLBeautifier();
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
      
      
			totSb.append("/********************************************************/\n");
		    totSb.append("/* SELECT                                               */\n");
		    totSb.append("/********************************************************/\n");			
			selectSb.append("SELECT ");
      		
      		for (int j = 0; j < rsTbl.getRecordCount(); j++) {
      			if( j != 0) {
      				selectSb.append(" ,");
      			}
      			selectSb.append(String.valueOf(rsTbl.get(j, "T_ENG_COLUMN_NAME")));
      			selectSb.append(" AS ");
      			selectSb.append(String.valueOf(rsTbl.get(j, "CAMEL_CASE")));
      			selectSb.append(" /* " + String.valueOf(rsTbl.get(j, "T_KOR_COLUMN_NAME")) + " */");      			
      		}
         
      		selectSb.append("FROM ");
      		selectSb.append(String.valueOf(rsTbl.get(0, "T_ENG_TABLE_NAME")) + "    /*" + rsTbl.get(0, "T_KOR_TABLE_NAME") + "*/");
      		selectSb.append("\n");
      		selectSb.append("WHERE ");
		    
		    boolean flag = false;
		    for (int j = 0; j < rsTbl.getRecordCount(); j++) {
		    	if("Y".equals(rsTbl.get(j, "PK"))){
		    		if(flag) {
		    			selectSb.append(" AND ");
		    		}
		    		flag = true;
		    		selectSb.append(String.valueOf(rsTbl.get(j, "T_ENG_COLUMN_NAME")) + "=  ");
		    		selectSb.append(String.valueOf(rsTbl.get(j, "MYBATIS")));
		    	}			    	
		    }
		    selectSb.append("; ");
		    
		    totSb.append(stf.beautify(String.valueOf(selectSb)));		    
		    totSb.append("\n\n\n");
		    totSb.append("/********************************************************/\n");
		    totSb.append("/* DELETE                                               */\n");
		    totSb.append("/********************************************************/\n");
		    
		    deleteSb.append("DELETE FROM ");
		    deleteSb.append(String.valueOf(rsTbl.get(0, "T_ENG_TABLE_NAME")) + "    /*" + rsTbl.get(0, "T_KOR_TABLE_NAME") + "*/");
		    deleteSb.append("WHERE ");
		    
		    flag = false;
		    for (int j = 0; j < rsTbl.getRecordCount(); j++) {
		    	if("Y".equals(rsTbl.get(j, "PK"))){
		    		if(flag) {
		    			deleteSb.append(" AND ");
		    		}
		    		flag = true;
		    		deleteSb.append(String.valueOf(rsTbl.get(j, "T_ENG_COLUMN_NAME")) + "= ");
		    		deleteSb.append(String.valueOf(rsTbl.get(j, "MYBATIS")));
		    	}			    	
		    }
		    deleteSb.append("; ");
		    totSb.append(stf.beautify(String.valueOf(deleteSb)));		    
		    totSb.append("\n\n\n");
		    
		    totSb.append("/********************************************************/\n");
		    totSb.append("/* UPDATE                                               */\n");
		    totSb.append("/********************************************************/\n");
		    
		    updateSb.append("UPDATE ");
		    updateSb.append(String.valueOf(rsTbl.get(0, "T_ENG_TABLE_NAME")) + "    /*" + rsTbl.get(0, "T_KOR_TABLE_NAME") + "*/");
		    updateSb.append("SET ");
		    
		    flag = false;
		    for (int j = 0; j < rsTbl.getRecordCount(); j++) {
		    	if(!"Y".equals(rsTbl.get(j, "PK"))){
	      			if( flag) {
	      				updateSb.append(" ,");
	      			}
	      			flag = true;
	      			updateSb.append(String.valueOf(rsTbl.get(j, "T_ENG_COLUMN_NAME")) + "= ");
	      			updateSb.append(String.valueOf(rsTbl.get(j, "MYBATIS")));	      			
	      			updateSb.append(" /* " + String.valueOf(rsTbl.get(j, "T_KOR_COLUMN_NAME")) + " */");
		    	}
		    	
      		}
		    updateSb.append(" WHERE ");
		    
		    flag = false;
		    for (int j = 0; j < rsTbl.getRecordCount(); j++) {
		    	if("Y".equals(rsTbl.get(j, "PK"))){
		    		if(flag) {
		    			updateSb.append(" AND ");
		    		}
		    		flag = true;
		    		updateSb.append(String.valueOf(rsTbl.get(j, "T_ENG_COLUMN_NAME")) + "= ");
		    		updateSb.append(String.valueOf(rsTbl.get(j, "MYBATIS")));
		    	}			    	
		    }
		    updateSb.append("; ");
		    
		    totSb.append(stf.beautify(String.valueOf(updateSb)));		    
		    totSb.append("\n\n\n");
		    
		    totSb.append("/********************************************************/\n");
		    totSb.append("/* INSERT                                               */\n");
		    totSb.append("/********************************************************/\n");
		    
		    insertSb.append("INSERT INTO ");
		    insertSb.append(String.valueOf(rsTbl.get(0, "T_ENG_TABLE_NAME")) + "    /*" + rsTbl.get(0, "T_KOR_TABLE_NAME") + "*/");
		    insertSb.append("( ");
		    for (int j = 0; j < rsTbl.getRecordCount(); j++) {
      			if( j != 0) {
      				insertSb.append(" ,");
      			}
      			insertSb.append(String.valueOf(rsTbl.get(j, "T_ENG_COLUMN_NAME")));  
      			insertSb.append(" /* " + String.valueOf(rsTbl.get(j, "T_KOR_COLUMN_NAME")) + " */");      			
      		}
		    insertSb.append(") VALUES (");
		    
		    for (int j = 0; j < rsTbl.getRecordCount(); j++) {
      			if( j != 0) {
      				insertSb.append(" ,");
      			}      			
      			insertSb.append(String.valueOf(rsTbl.get(j, "MYBATIS")));
      		}
		    
		    insertSb.append(") ;");
		    totSb.append(stf.beautify(String.valueOf(insertSb)));		    
		    totSb.append("\n\n\n");
		    
		    
			logger.debug("rsTblRtn : " + recordSet);      
			dataSet.putField("RESULT", totSb);

			dataSet.putField("rsTblCnt", recordSet.getRecordCount());
			dataSet.putRecordSet("rsTblRtn", (IRecordSet)recordSet);
			dataSet.putRecordSet("rsTbl", rsTbl);
			
		} catch (Exception e) {      
		  e.printStackTrace();		  
		} 
    
		if (rsTbl.getRecordCount() == 0) {
			rtnMsg = "입력한 테이블이 존재하지 않습니다";			
		} else {
			rtnMsg = "조회 완료되었습니다.";			
		}
		dataSet.putField("rtnMsg", String.valueOf(today) + " " + rtnMsg);    
		return (IDataSet)dataSet;
		
	}
}
