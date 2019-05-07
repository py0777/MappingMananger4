package mm.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import mm.common.RecordSetResultHandler;
import mm.repository.AbstractRepository;
import nexcore.framework.core.data.DataSet;
import nexcore.framework.core.data.IDataSet;
import nexcore.framework.core.data.IRecord;
import nexcore.framework.core.data.IRecordHeader;
import nexcore.framework.core.data.IRecordSet;
import nexcore.framework.core.data.RecordSet;
import nexcore.framework.core.util.StringUtils;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import py0777.orasql.SQLBeautifier;

public class SqlTranInq_Backup extends AbstractRepository {
	static Logger logger = Logger.getLogger(SqlTranInq_Backup.class);
	private final String namespace = "mm.repository.mapper.SqlTranInqMapper";

	public IDataSet s001(IDataSet requestData) {
		SqlSession sqlSession = getSqlSessionFactory().openSession();
		try {
			String statement = namespace + ".S001";

			IRecordSet rs = null;

			RecordSetResultHandler resultHandler = new RecordSetResultHandler();
			resultHandler.setRecordSetId("RS_TBLLIST");

			logger.debug("strat!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			sqlSession.select(statement, requestData.getFieldMap(), resultHandler);

			IDataSet ds = new DataSet();
			ds.putRecordSet(resultHandler.getRecordSet());

			return ds;
		} finally {
			sqlSession.close();
			/* test */
		}
	}

	public IDataSet s002(IDataSet requestData) {
		SqlSession sqlSession = getSqlSessionFactory().openSession();
		try {

			String statement = namespace + ".S002";

			RecordSetResultHandler resultHandler = new RecordSetResultHandler();
			resultHandler.setRecordSetId("RS_COLLIST");
			sqlSession.select(statement, requestData.getFieldMap(), resultHandler);

			IDataSet ds = new DataSet();
			ds.putRecordSet(resultHandler.getRecordSet());

			return ds;
		} finally {
			sqlSession.close();
		}
	}

	public IDataSet asSqlTrnInq(IDataSet requestData) {

		logger.debug("###########  START #########");
		logger.debug(getClass().getName());
		
		logger.debug(requestData);

		/*************************************************************
		 * Declare Var
		 *************************************************************/
		IDataSet responseData = new DataSet();

		IDataSet dsTbl = null;
		IDataSet dsCol = null;
		IRecordSet inputRsTbl = new RecordSet("INPUT_TBL");
		IRecord inputRcTbl = null;
		
		
		IRecordSet rsTbl = null;
		IRecordSet rsCol = null;
		IRecordSet rsRtn = new RecordSet("RS_LIST");
		String rtnMsg = "";

		String query = "";
		String inputTbl = "";
		String s_asisTbl = "";
		String s_asisCol = "";
		String s_tobeCol = "";
		String s_tobeColNm = "";
		String[] tmp;
		String[] tmp2;
		
		String[] inputTbl1;
		String[] inputTbl2;
		ArrayList<String> al = new ArrayList<String>();
		HashMap<String, String> tblMap = new HashMap<String, String>();

		StringBuffer sb = new StringBuffer();

		String parseReg[] = { "\\.", "\\;", "\\(", "\\)", "\\,", "\\|", "=", " ", "\r\n", "\r", "\n" };
		String parseReg2[] = { "~~\\.~~", "~~\\;~~", "~~\\(~~", "~~\\)~~", "~~\\,~~", "~~|~~", "~~=~~", "~~ ~~",
				"~~\r\n~~", "~~\r~~", "~~\n~~" };
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

			dsTbl = s001(requestData);
			rsTbl = dsTbl.getRecordSet("RS_TBLLIST");

			query = query.toUpperCase();
			query = query.replaceAll("\t", "    ");

			for (int i = 0; i < parseReg.length; i++) {
				query = query.replaceAll(parseReg[i], parseReg2[i]);
			}

			logger.debug(query);

			tmp = query.split("~~");

			for (int i = 0; i < tmp.length; i++) {
				if (!StringUtils.isEmpty(tmp[i].replace(" ", ""))) {
					al.add(tmp[i]);
				}
			}
			logger.debug(al);

//			/* START N:1 or 1:N 테이블 값 처리하기*/
//			if(		!StringUtils.isEmpty(requestData.getField("INPUT_TBL"))
//				&&	!"NULL".equals(requestData.getField("INPUT_TBL").toUpperCase()))
//			{
//				inputTbl = 	requestData.getField("INPUT_TBL").toUpperCase();
//				/*테이블맵핑 해더 설정*/
//				for(int i = 0 ; i < rsTbl.getHeaderCount(); i ++ )
//				{
//					inputRsTbl.addHeader(i, rsTbl.getHeader(i));
//				}
//				inputTbl1 = inputTbl.split(":");
//				
//				for( int i = 0 ; i < inputTbl1.length; i ++  )
//				{
//					inputTbl2 = inputTbl1[i].split(",");
//					
//					inputRcTbl = inputRsTbl.newRecord();
//					inputRcTbl.set("TBL", inputTbl2[0]);	/*ASIS 테이블*/
//					inputRcTbl.set("ATBL", inputTbl2[1]);	/*TOBE 테이블*/
//					inputRsTbl.setRecord(i, inputRcTbl);
//				}
//				
//			}
//			logger.debug("inputTbl:"+ inputTbl);
//			/* END N:1 or 1:N 테이블 값 처리하기*/
			
			for (int i = 0; i < rsTbl.getRecordCount(); i++) {
				s_asisTbl = rsTbl.getRecord(i).get("TBL").trim();

				for (int j = 0; j < tmp.length; j++) {
					if (StringUtils.equals(tmp[j], s_asisTbl) && !StringUtils.equals(tmp[j], "SY")) {
//						boolean b_skipYn = false;
//						/*SKIP 대상테이블 찾기*/
//						for(int k = 0 ; k <inputRsTbl.getRecordCount(); k++)
//						{
//							int l_skipFlag =0;
//							if(s_asisTbl.equals(inputRsTbl.getRecord(k).get("TBL").trim())){
//								l_skipFlag++;
//							}
//							
//							if((rsTbl.getRecord(i).get("ATBL")).equals(inputRsTbl.getRecord(k).get("ATBL").trim())){
//								l_skipFlag++;
//							}
//
//							if(l_skipFlag == 1){
//								b_skipYn = true;
//							}
//						}
//						
//						if(b_skipYn == true)
//						{	
//							continue;  /*대상 테이블에서 제외한다.*/
//						}
						
						
						tmp[j] = tmp[j].replaceAll(s_asisTbl, rsTbl.getRecord(i).get("ATBL").concat("  /*")
								.concat(rsTbl.getRecord(i).get("ATBLNM")).concat("*/"));
						tblMap.put(s_asisTbl, rsTbl.getRecord(i).get("ATBL").trim()); /* ASIS 테이블에 TOBE 테이블 맵핑 */
						if (".".equals(tmp[j - 1])) {
							tmp[j - 2] = "ACADM";
						} else {
							tmp[j] = "ACADM.".concat(tmp[j]);
						}

					}
				}
			}

			logger.debug(tblMap);

			StringBuffer keySB = new StringBuffer();
			Set<String> tblSet = tblMap.keySet();
			Iterator<String> it = tblSet.iterator();


			while (it.hasNext()) {
				String k = it.next();
				keySB.append("'");
				keySB.append(k);
				keySB.append("'");
				keySB.append(",");
			}

			if (keySB.length() > 0) {
				requestData.putField("KEY_LIST", keySB.delete(keySB.length() - 1, keySB.length()).toString());
			} else {
				requestData.putField("KEY_LIST", "''");
			}
			dsCol = s002(requestData);
			rsCol = dsCol.getRecordSet("RS_COLLIST");

			/*테이블맵핑 해더 설정*/
			for(int i = 0 ; i < rsCol.getHeaderCount(); i ++ )
			{
				rsRtn.addHeader(i, rsCol.getHeader(i));
			}
			logger.debug("=========================");
			logger.debug(rsCol);
			logger.debug(tblMap);
			
			for (int i = 0; i < rsCol.getRecordCount(); i++) {
				if (tblMap.containsKey(rsCol.getRecord(i).get("TBL"))) {
//					if(!tblMap.get(rsCol.getRecord(i).get("TBL")).equals(rsCol.getRecord(i).get("ATBL")))
//					{
//						logger.debug(rsCol.getRecord(i).get("ATBL"));
//						continue;
//					}
					s_asisCol = rsCol.getRecord(i).get("COL").trim();
					s_tobeCol = rsCol.getRecord(i).get("ACOL");
					s_tobeColNm = rsCol.getRecord(i).get("ACOLNM");
					for (int k = 0; k < tmp.length; k++) {

						if (StringUtils.equals(tmp[k], s_asisCol)) {
							tmp[k] = tmp[k].replaceAll(s_asisCol, s_tobeCol);
							if ("Y".equals(requestData.getField("COMMENT_YN"))) {
								tmp[k] = tmp[k].concat("/*").concat(s_tobeColNm)
										.concat("*/"); /* 컬럼 뒤에 주석달기 */
							}

							rsRtn.addRecord(rsCol.getRecord(i));
						} else if (tmp[k].contains(":")) {
							tmp[k] = tmp[k].toLowerCase();
						}
					}
				}
			}

			for (int u = 0; u < tmp.length; u++) {
				sb.append(tmp[u]);
			}
			SQLBeautifier sbf = new SQLBeautifier();

			responseData.putField("QUERY", "");
			responseData.putField("RESULT", sbf.beautify(sb.toString()));
			responseData.putField("rsCnt", rsRtn.getRecordCount());
			responseData.putRecordSet("rsRtn", rsRtn);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (tblMap.size() == 0) {
			rtnMsg = "입력한 테이블이 존재하지 않습니다.";
		} else {
			rtnMsg = "조회완료되었습니다.";
		}

		responseData.putField("rtnMsg", today + " " + rtnMsg);
		responseData.setOkResultMessage("NCOM0000", null);
		return responseData;
	}
}
