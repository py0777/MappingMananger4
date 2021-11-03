<%@page import="nexcore.framework.core.data.IRecordSet"%>
<%@page import="nexcore.framework.core.util.StringUtils"%>
<%@page import= "nexcore.framework.core.data.RecordSet"%>
<%@page import= "mm.service.DefaultSql"%>
<%@ page import="nexcore.framework.core.data.DataSet, nexcore.framework.core.data.IDataSet" language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Valid SQL</title>

	<style>
	input.uppercase{
	text-transform: uppercase;
	}
	</style>
	<!-- highlight.js -->
	<script src="./highlight/highlight.min.js"></script>
	<link rel="stylesheet" href="./highlight/styles/atom-one-dark.min.css">
	<script>hljs.highlightAll();</script>
</head>
<body>
  
<%
String result = null;
IRecordSet resultTblRs = new RecordSet("RS_TBL_LIST", new String[]{"MAP_ID","T_ENG_TABLE_NAME","T_KOR_TABLE_NAME","A_ENG_TABLE_NAME","A_KOR_TABLE_NAME"});
int resultRsCnt = 0;
int rsTblCnt = 0;
String resultMessage = "";


request.setCharacterEncoding("UTF-8");/* 한글깨짐 현상 없애기 위함*/
if(!"null".equals(StringUtils.nvl(request.getParameter("TABLE_NAME"), "null"))){
	
	IDataSet requestData = new DataSet();
	requestData.putField("TABLE_NAME", request.getParameter("TABLE_NAME"));
	
	DefaultSql ds = new DefaultSql();
	
	IDataSet responseData = ds.defaultSql(requestData);
	if( responseData != null){
		result = responseData.getField("RESULT");
		resultRsCnt = responseData.getIntField("rsCnt");
		rsTblCnt = responseData.getIntField("rsTblCnt");
		resultMessage = responseData.getField("rtnMsg");
		resultTblRs = responseData.getRecordSet("rsTblRtn");
	}
	
}else{
	result = "";
}

%>
<form action="defaultSql.jsp" id ="myForm" name="myForm" method="POST" >
<label ><strong>TOBE테이블명  : </strong></lable>
<input name ="TABLE_NAME" type ="text" class="uppercase"  oninput="this.value=this.value.toUpperCase()" value =<%=request.getParameter("TABLE_NAME")%>>

<input type="submit" value="기본 SQL 생성" />
<br />
<br />
<br /><%=resultMessage %>
<br />
<label for=validSql><strong >기본 SQL</strong></label>
<br />
<pre><code class="language-sql">
	<TEXTAREA name="defaultSql" rows="80" style="WIDTH: 92%" id="defaultSql">
<%=result%>
</textarea>
</code></pre>
<br /><h4><font color ="blue"><strong >※ 테이블명 기준으로 기본 SQL을 생성합니다.</strong></font></h4>

	</form>
	
</body>
</html>