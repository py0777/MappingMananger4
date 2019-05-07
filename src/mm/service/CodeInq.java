package mm.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import mm.common.RecordSetResultHandler;
import mm.repository.AbstractRepository;
import nexcore.framework.core.data.DataSet;
import nexcore.framework.core.data.IDataSet;
import nexcore.framework.core.data.IRecordSet;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

public class CodeInq extends AbstractRepository {

	static Logger logger = Logger.getLogger(SqlTranInq.class);
	private final String namespace = "mm.repository.mapper.CodeMapper";

	public IDataSet s001(IDataSet requestData) {
		SqlSession sqlSession = getSqlSessionFactory().openSession();

		try {
			String statement = namespace + ".S001";
			
			IRecordSet rs = null;
			
			RecordSetResultHandler resultHandler = new RecordSetResultHandler();
			resultHandler.setRecordSetId("RS_CODELIST");
			
			logger.debug("strat!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			sqlSession.select(statement, requestData.getFieldMap(), resultHandler);
			
			IDataSet ds = new DataSet();
			ds.putRecordSet(resultHandler.getRecordSet());
			
			logger.debug(ds);
			
			return ds;
		} finally {
			sqlSession.close();
			/*test*/
		}
	}
	
	public IDataSet codeInq(IDataSet requestData){
		logger.debug("###########  START #########");
		logger.debug(getClass().getName());
		
		logger.debug(requestData.getField("INPUT"));
				
		/*************************************************************
		 *  Declare Var
		 *************************************************************/
		IDataSet 	responseData 	= new DataSet();
		IDataSet 	dsCode 			= null;
		IRecordSet 	rsCode 			= null;
		
		String rtnMsg = "";
		String today = "";
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss초 E(a)");
	
		try
		{
			Date d = gc.getTime();
			today = sdformat.format(d);
			
			if(requestData.getField("INPUT").isEmpty())
			{
				rtnMsg = "조회할 내용을 입력하세요.";
				responseData.putField("rtnMsg", today + " " + rtnMsg);
				return null;
			}
			
			dsCode	=	s001(requestData);
			
			rsCode = dsCode.getRecordSet("RS_CODELIST");
			
			logger.debug(rsCode);
			
			if(rsCode.getRecordCount() == 0)
			{
				rtnMsg = "조회내역이 없습니다.";
				responseData.putField("rtnMsg", today + " " + rtnMsg);				
				return responseData;
			}
			
			responseData.putField("rsCnt", rsCode.getRecordCount());
			responseData.putRecordSet("rsCode", rsCode);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		rtnMsg = "조회완료되었습니다.";
		responseData.putField("rtnMsg", today + " " + rtnMsg);
		responseData.setOkResultMessage("OK", new String[]{"조회완료되었습니다."});
		
		return responseData;	
	}
	
}
