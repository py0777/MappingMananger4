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

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;


	public class ColInq extends AbstractRepository {

	static Logger logger = Logger.getLogger(SqlTranInq.class);
	private final String namespace = "mm.repository.mapper.ColMapper";

	public IDataSet s001(IDataSet requestData) {
		SqlSession sqlSession = getSqlSessionFactory().openSession();
		try {
			String statement = namespace + ".S001";
			
			RecordSetResultHandler resultHandler = new RecordSetResultHandler();
			resultHandler.setRecordSetId("RS_COLLIST");
			
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
	
	public IDataSet colInq(IDataSet requestData){
		logger.debug("###########  START #########");
		logger.debug(getClass().getName());
		
		logger.debug(requestData.getField("INPUT"));
				
		/*************************************************************
		 *  Declare Var
		 *************************************************************/
		IDataSet 	responseData 	= new DataSet();
		IDataSet 	dsCol 			= null;
		IRecordSet 	rsCol 			= null;
		IRecordSet 	rsTbl 			= new RecordSet("AA");
		String      rtnMsg			= "";
		String today = "";
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss초 E(a)");

		try
		{
			Date d = gc.getTime();
			today = sdformat.format(d);
			
			if( requestData.getField("IN_TBL").isEmpty() || "NULL".equals(requestData.getField("IN_TBL")))
			{
				requestData.putField("IN_TBL", "%");
			}
			
			if( requestData.getField("IN_COL").isEmpty() || "NULL".equals(requestData.getField("IN_COL")))
			{
				requestData.putField("IN_COL", "%");
			}
			/*입력값 null 체크*/
			if(		"%".equals(requestData.getField("IN_TBL"))
				&&  "%".equals(requestData.getField("IN_COL"))	)
			{
				rtnMsg = "테이블 또는 컬럼을 입력하세요.";
				responseData.putField("rtnMsg", today + " " + rtnMsg);
				return responseData;
			}
			dsCol	=	s001(requestData);
			
			rsCol = dsCol.getRecordSet("RS_COLLIST");
			
			logger.debug(rsCol);
			
			responseData.putField("rsCnt", rsCol.getRecordCount());
			
			if(rsCol.getRecordCount() == 0)
			{
				rtnMsg = "조회내역이 없습니다.";
				responseData.putField("rtnMsg", today + " " + rtnMsg);				
				return responseData;
			}
			
			/*헤더값 설정*/
			for(int i = 0 ; i < rsCol.getHeaderCount(); i ++ )
			{
				rsTbl.addHeader(i, rsCol.getHeader(i));
			}
			
			/*벼누 설정*/
			String l_T_ENG_TABLE_NAME = "";
			String l_A_ENG_TABLE_NAME = "";
			String l_MAP_ID = "";
			
			int j = 0;
			for(int i=0; i < rsCol.getRecordCount(); i++)
			{	
				
				if( 	StringUtils.isEmpty(rsCol.getRecord(i).get("T_ENG_TABLE_NAME"))
					||	StringUtils.isEmpty(rsCol.getRecord(i).get("A_ENG_TABLE_NAME"))	)
				{
					continue;
				}
				
				if(l_MAP_ID.equals(rsCol.getRecord(i).get("MAP_ID")))
				{
					continue;
				}
				
				if( 	!l_T_ENG_TABLE_NAME.equals(rsCol.getRecord(i).get("T_ENG_TABLE_NAME"))
					||	!l_A_ENG_TABLE_NAME.equals(rsCol.getRecord(i).get("A_ENG_TABLE_NAME"))	)
				{
					logger.debug( rsCol.getRecord(i).get("T_ENG_TABLE_NAME"));
					logger.debug(rsCol.getRecord(i).get("A_ENG_TABLE_NAME"));
					rsTbl.addRecord(j, rsCol.getRecord(i));
					j++;
				}
				
				l_T_ENG_TABLE_NAME = rsCol.getRecord(i).get("T_ENG_TABLE_NAME");
				l_A_ENG_TABLE_NAME = rsCol.getRecord(i).get("A_ENG_TABLE_NAME");
				l_MAP_ID = rsCol.getRecord(i).get("MAP_ID");
			}
			
			logger.debug(rsTbl);
			responseData.putRecordSet("rsTbl", rsTbl);
			responseData.putRecordSet("rsCol", rsCol);
			
			responseData.putField("rsTblCnt", rsTbl.getRecordCount());
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		rtnMsg = "조회완료되었습니다.";
		responseData.putField("rtnMsg", today + " " + rtnMsg);
		responseData.setOkResultMessage("OK", new String[]{"조회완료되었습니다."});
		
		return responseData;	
	}
}
