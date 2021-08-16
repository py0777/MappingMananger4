<%@page import="nexcore.framework.core.data.IRecordSet"%>
<%@page import="nexcore.framework.core.util.StringUtils"%>
<%@page import= "nexcore.framework.core.data.RecordSet"%>
<%@page import= "mm.service.ValidSql"%>
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
	
	String[] tblChkbox = request.getParameterValues("cb");
	requestData.putFieldValues("MAP_ID", tblChkbox);
	
	ValidSql vs = new ValidSql();
	
	IDataSet responseData = vs.validSql(requestData);
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
<form action="validSql.jsp" id ="myForm" name="myForm" method="POST" >
<label ><strong>TOBE테이블명  : </strong></lable>
<input name ="TABLE_NAME" type ="text" class="uppercase"  oninput="this.value=this.value.toUpperCase()" value =<%=request.getParameter("TABLE_NAME")%>>

<input type="submit" value="검증SQL 생성" />
<br />
<br />


<table width="99%" border="1" cellpadding="4" cellspacing ="0" style="border-collapse:collapse;">
		<%
		  if(rsTblCnt > 0){
		%>
		<tr> 
			<td bgcolor="green" colspan="6"><strong>매핑정보</strong></td>
		</tr>
		<tr>
		    <td bgcolor="yellow"><strong>선택해지</strong></td>			
		    <td style="border:1px gray soild" bgcolor="yellow"><strong>MAP_ID</strong></td>
			<td style="border:1px gray soild" bgcolor="yellow"><strong>TOBE테이블</strong></td>
			<td bgcolor="yellow"><strong>TOBE테이블한글</strong></td>			   
			<td bgcolor="yellow"><strong>ASIS테이블</strong></td>  	
			<td bgcolor="yellow"><strong>ASIS테이블한글</strong></td>
		</tr>	   
		<% 	
		   } 
		%>
<%
	for(int i = 0; i < rsTblCnt ; i++){		
%>
			<tr> 
			    <td><input type="checkbox" id="cb" name="cb" value=<%= resultTblRs.get(i, "MAP_ID") %> checked></td>
			    <td><name=atbl ><%=resultTblRs.get(i, "MAP_ID")%></td>     <!- tobe테이블      ->
				<td><name=atbl ><%=resultTblRs.get(i, "T_ENG_TABLE_NAME")%></td>     <!- tobe테이블      ->
				<td><%=resultTblRs.get(i, "T_KOR_TABLE_NAME")%></td>  <!- tobe테이블한글  ->
				<td><name=tbl><%=resultTblRs.get(i, "A_ENG_TABLE_NAME")%></td>  <!- asis테이블      ->
				<td><%=resultTblRs.get(i, "A_KOR_TABLE_NAME")%></td>    <!- asis테이블한글  ->				
			</tr>
			<%	
	}
%>
		</table>
<br /><%=resultMessage %>
<br />
<label for=validSql><strong >검증 SQL</strong></label>
<br />
<TEXTAREA name="validSql" rows="20" style="WIDTH: 92%" id="validSql">

<%=result%>
</textarea>
<br /><h4><font color ="blue"><strong >※ MAP ID기준으로 기본 검증 SQL을 생성합니다.</strong></font></h4>
<h4><font color ="red"><strong >  - MINUS 검증, COUNT 검증 SQL 생성 합니다.</strong></font></h4>
	</form>
	
</body>
</html>