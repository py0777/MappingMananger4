<%@ page import="nexcore.framework.core.data.DataSet, nexcore.framework.core.data.IDataSet" language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">


<link rel="stylesheet" type="text/css" href="user/css/user.css" />


</head>


<body>
<!-- 2016.01.04 추가 시작 
이메일추가 
	<div>
        <form action="Main.jsp" method="post">
            <table>
                <tr><th colspan="2">JSP 메일 보내기</th></tr>                
                <tr><td>subject</td><td><input type="text" name="subject" /></td></tr>
                <tr><td>content</td><td><textarea name="content" style="width:170px; height:200px;"></textarea></td></tr>
                <tr><td colspan="2" style="text-align:right"><input type="submit" value="Transmission"/></td></tr>
            </table>
        </form>
    </div>
 2016.01.04 추가 시작 
이메일 종료 -->
	<h5>
<b>This is an email link:</b>
<a href="mailto:creatorpark@gmail.com?subject=MappingManager" target="_top">Send Mail</a>
</h5>
<iframe src="https://docs.google.com/forms/d/1CBUQ9afD4tfNXQCaz6F3CPAdw3DqK71xvFaE0pKAZCQ/viewform?embedded=true" width ="100%" height ="700" scolling="no" frameborder="0" marginheight="0" marginwidth="0">로드 중...</iframe>
<!-- 2016.01.04 추가 시작 
라이브리 시티 설치 코드 -->
<div id="lv-container" data-id="city" data-uid="MTAyMC8yNDk5MS8xNjc1">
	<script type="text/javascript">
   (function(d, s) {
       var j, e = d.getElementsByTagName(s)[0];

       if (typeof LivereTower === 'function') { return; }

       j = d.createElement(s);
       j.src = 'https://cdn-city.livere.com/js/embed.dist.js';
       j.async = true;

       e.parentNode.insertBefore(j, e);
   })(document, 'script');
	</script>
<noscript> 라이브리 댓글 작성을 위해 JavaScript를 활성화 해주세요</noscript>
</div>
<!-- 2016.01.04 추가 종료 
라이브리 시티 설치 코드 -->				
</body>
</html>