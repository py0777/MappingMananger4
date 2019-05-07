<%@page import="mm.service.SqlFormatter"%>
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
<title>SQL FORMATER</title>

</script>
</head>
<body>
  
<%
String result = null;
int resultRsCnt = 0;
int rsTblCnt = 0;
String resultMessage = "";


request.setCharacterEncoding("UTF-8");/* 한글깨짐 현상 없애기 위함*/
if(!StringUtils.isEmpty(request.getParameter("asisSql"))){
	
	IDataSet requestData = new DataSet();
	requestData.putField("QUERY", request.getParameter("asisSql"));
		
	SqlFormatter sft = new SqlFormatter();
	
	IDataSet responseData = sft.sqlFormatter(requestData);
	if( responseData != null){
		result = responseData.getField("RESULT");
		resultMessage = responseData.getField("rtnMsg");		
	}
	
}else{
	result = "";
}

%>
<form action="sqlFormatter.jsp" id ="myForm" name="myForm" method="POST" >
<h3><font color ="blue"><strong >1. SQL 포멧을 자동으로 맞춰주고 있습니다.</strong></font></h3>
<h3><font color ="blue"><strong >2. 용어사전과 연동시 표준용어로 자동주석을 달아줄 수 있습니다.</strong></font></h3>
<input type="submit" value="SQL 포멧팅 변환" <% if (StringUtils.isEmpty(request.getParameter("asisSql"))){ resultMessage ="";}%> >
<br />

<br />
<label for="asisSql"><strong >BEFORE SQL</strong></label>
<br />
<TEXTAREA name="asisSql" rows="20" style="WIDTH: 92%" id="asisSql">
<% if (StringUtils.isEmpty(request.getParameter("asisSql"))){ %><%= "" %>
<% }else{ %><%=request.getParameter("asisSql")%>
<% } %>
</textarea> 
<br />
<label for=tobeSql><strong >AFTER SQL</strong></label>
<br />
<TEXTAREA name="tobeSql" rows="20" style="WIDTH: 92%" id="tobeSql">
<%=result%>
</textarea>
	<br /><%=resultMessage %>
	</form>
	<br /><h3><font color ="blue"><strong >※ SQL '('  ,  ')'괄호의 갯수가 맞지 않을 경우 자동포맷팅이 되지 않습니다.</strong></font></h3>
</body>
</html>