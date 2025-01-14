<%@page import="nexcore.framework.core.data.IRecordSet"%>
<%@page import="nexcore.framework.core.util.StringUtils"%>
<%@page import= "nexcore.framework.core.data.RecordSet"%>
<%@page import= "mm.service.SqlTranInq"%>
<%@ page import="nexcore.framework.core.data.DataSet, nexcore.framework.core.data.IDataSet" language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SQL TRAN</title>
<script language="Javascript" type="text/javascript">		
	function allChk(obj){
		var chkObj = document.getElementsByName("cb");
		  var rowCnt = chkObj.length -1;
		  var check = obj.checked;
		  if(check){
			  for(var i =0; i<= rowCnt; i++){
				  if(chkObj[i].type == "checkbox"){
					  chkObj[i].checked = true;
				  }
			  }
		  }else{
			  for(var i = 0 ; i <= rowCnt; i++){
				  if(chkObj[i].type == "checkbox"){
					  chkObj[i].checked = false;
				  }
			  }
		  }	
	}
	
	/*변환SQL 복사*/
	function OnClickBtncopy(obj) {
		var text = document.getElementById("tobeSql").value
		var t = document.createElement("textarea");
		  document.body.appendChild(t);
		  t.value = text;
		  t.select();
		  document.execCommand("copy");
		  document.body.removeChild(t);
	}

	
  
</script>
</head>
<body>
  
<%
String result = null;
IRecordSet resultRs = new RecordSet("RS_LIST", new String[]{"TBL","TBLNM","COL","COLNM","ATBL","ATBLNM","ACOL","ACOLNM"});
IRecordSet resultTblRs = new RecordSet("RS_TBL_LIST", new String[]{"TBL","TBLNM","COL","COLNM","ATBL","ATBLNM","ACOL","ACOLNM"});
int resultRsCnt = 0;
int rsTblCnt = 0;
String resultMessage = "";


request.setCharacterEncoding("UTF-8");/* 한글깨짐 현상 없애기 위함*/
if(!StringUtils.isEmpty(request.getParameter("asisSql"))){
	
	
	String commentYn = request.getParameter("commentYn");
	String sqlFormatYn = request.getParameter("sqlFormatYn");
	
	IDataSet requestData = new DataSet();
	requestData.putField("INQ_CON", request.getParameter("INQ_CON"));
	requestData.putField("QUERY", request.getParameter("asisSql"));
	
	if("Y".equals(commentYn))
	{
		requestData.putField("COMMENT_YN", "Y");	
	}else
	{
		requestData.putField("COMMENT_YN", "N");
	}
	
	if("Y".equals(sqlFormatYn))
	{
		requestData.putField("SQLFORMAT_YN", "Y");	
	}else
	{
		requestData.putField("SQLFORMAT_YN", "N");
	}
	
	
	String[] tblChkbox = request.getParameterValues("cb");
	requestData.putFieldValues("TOBE_TBL_LIST", tblChkbox);
	
	SqlTranInq sti = new SqlTranInq();
	
	IDataSet responseData = sti.asSqlTrnInq(requestData);
	if( responseData != null){
		result = responseData.getField("RESULT");
		resultRs  = responseData.getRecordSet("rsRtn");
		resultRsCnt = responseData.getIntField("rsCnt");
		rsTblCnt = responseData.getIntField("rsTblCnt");
		resultMessage = responseData.getField("rtnMsg");
		resultTblRs = responseData.getRecordSet("rsTblRtn");
	}
	
}else{
	result = "";
}

%>
<form action="sqlTran.jsp" id ="myForm" name="myForm" method="POST" >
	<lable for="INQ_CON"><strong>조회기준 : </strong></lable>
	<input type=radio name="INQ_CON" value="1" <%if("1".equals(request.getParameter("INQ_CON"))){%>checked<%}%> />TOBE->ASIS
	<input type=radio name="INQ_CON" value="2" <%if("2".equals(request.getParameter("INQ_CON"))|| StringUtils.isEmpty(request.getParameter("INQ_CON"))){%>checked<%}%> />ASIS->TOBE
<br />
<label for="commentYn"></label><input type="checkbox" name="commentYn" value="Y" <%if("Y".equals(request.getParameter("commentYn"))){%>checked <%}%> >컬럼주석달기       </lable>

<label for="sqlFormatYn"></label><input type="checkbox" name="sqlFormatYn" value="Y" <%if( "Y".equals(request.getParameter("sqlFormatYn"))){%>checked <%}%> >SQL포멧맞추기</lable>
<input type="submit" value="변환" <% if (StringUtils.isEmpty(request.getParameter("asisSql"))){ resultMessage ="";}%> >
<button id="btn-copy" onclick="OnClickBtncopy(this);" style="position:relative; left:50%"> 변환SQL 복사</button>
<br />
	<TEXTAREA  name="asisSql" id ="asisSql" rows="20" style="WIDTH: 46%">
		<% if (StringUtils.isEmpty(request.getParameter("asisSql"))){ %><%= "" %>
		<% }else{ %><%=request.getParameter("asisSql").trim()%>
		<% } %>
	</textarea> 
	<TEXTAREA  name="tobeSql" id ="tobeSql" rows="20" style="WIDTH: 46%">
	<%=result%>
	</textarea>
<p >
<%=resultMessage %>
<br /><h4><font color ="blue"><strong >※ SQL변환은 테이블 매핑이 1:1인 경우 정확하고,- 1:N, N:1인 경우 정확하지 않을수 있습니다. 아래 조회된 대상 테이블 중 불필요 테이블은 선택해지 후 다시 조회해보세요.</strong></font></h4>
<table width="99%" border="1" cellpadding="4" cellspacing ="0" style="border-collapse:collapse;">
		<%
		  if(rsTblCnt > 0){
		%>
		<tr> 
			<td bgcolor="green" colspan="5"><strong>대상테이블</strong></td>
		</tr>
		<tr>
		    <td bgcolor="yellow"><input id="allCheck" type="checkbox" onclick="allChk(this);" checked><strong>선택해지</strong></td>			
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
			    <td width="90"><input type="checkbox" id="cb" name="cb" value=<%= resultTblRs.get(i, "ATBL") %> checked></td>
				<td><name=atbl ><%=resultTblRs.get(i, "ATBL")%></td>     <!- tobe테이블      ->
				<td><%=resultTblRs.get(i, "ATBLNM")%></td>  <!- tobe테이블한글  ->
				<td><name=tbl><%=resultTblRs.get(i, "TBL")%></td>  <!- asis테이블      ->
				<td><%=resultTblRs.get(i, "TBLNM")%></td>    <!- asis테이블한글  ->				
			</tr>
			<%	
	}
%>
		</table>

		<table width="80%" border="1" cellpadding="4" cellspacing ="0" style="border-collapse:collapse;">
		<%
		  if(resultRsCnt > 0){
		%>
		<tr> 
			<td bgcolor="green" colspan="8"><strong>대상컬럼</strong></td>
		</tr>
		<tr>
			<td style="border:1px gray soild" bgcolor="yellow"><strong>TOBE테이블</strong></td>
			<td bgcolor="yellow"><strong>TOBE테이블한글</strong></td> 
			<td bgcolor="yellow"><strong>TOBE컬럼</strong></td>     	
			<td bgcolor="yellow"><strong>TOBE컬럼한글</strong></td>   
			<td bgcolor="yellow"><strong>ASIS테이블</strong></td>  	
			<td bgcolor="yellow"><strong>ASIS테이블한글</strong></td> 
			<td bgcolor="yellow"><strong>ASIS컬럼</strong></td>   	
			<td bgcolor="yellow"><strong>ASIS컬럼한글</strong></td>
		</tr>	   
		<% 	
		   } 
		%>
<%
	for(int i = 0; i < resultRsCnt ; i++){		
%>
			<tr>
				<td><%=resultRs.get(i, "ATBL")%></td>     <!- tobe테이블      ->
				<td><%=resultRs.get(i, "ATBLNM")%></td>  <!- tobe테이블한글  ->
				<td><%=resultRs.get(i, "ACOLNM")%></td>     <!- tobe컬럼        ->
				<td><%=resultRs.get(i, "ACOL")%></td>   <!- tobe컬럼한글    ->
				<td><%=resultRs.get(i, "TBL")%></td>  <!- asis테이블      ->
				<td><%=resultRs.get(i, "TBLNM")%></td>    <!- asis테이블한글  ->
				<td><%=resultRs.get(i, "COL")%></td>   <!- asis컬럼        ->
				<td><%=resultRs.get(i, "COLNM")%></td>    <!- asis컬럼한글    ->				
			</tr>
			<%	
	}
%>
		</table>
	</form>
	
</body>
</html>