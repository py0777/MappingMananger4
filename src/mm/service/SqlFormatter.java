package mm.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import mm.repository.AbstractRepository;
import nexcore.framework.core.data.DataSet;
import nexcore.framework.core.data.IDataSet;

import org.apache.log4j.Logger;

import py0777.orasql.SQLBeautifier;

public class SqlFormatter extends AbstractRepository {
	static Logger logger = Logger.getLogger(SqlFormatter.class);
	
	public IDataSet sqlFormatter(IDataSet requestData) {

		logger.debug("###########  START #########");
		logger.debug(getClass().getName());
		
		logger.debug(requestData);
		
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		IDataSet responseData = new DataSet();
	
		String rtnMsg = "";

		String query = "";
		String today = "";
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss초 E(a)");

		try {
			Date d = gc.getTime();
			/*************************************************************
			 * 1. 모든 테이블 조회
			 *************************************************************/
			today = sdformat.format(d);
			query = requestData.getField("QUERY");

			logger.debug(query);

			SQLBeautifier sbf = new SQLBeautifier();
			
			responseData.putField("QUERY", "");
			
			responseData.putField("RESULT", sbf.beautify(query));	
		
		} catch (Exception e) {
			e.printStackTrace();
		}

		rtnMsg = "조회완료되었습니다.";
		

		responseData.putField("rtnMsg", today + " " + rtnMsg);
		
		return responseData;
	}
}
