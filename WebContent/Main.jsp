<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html lang="en" class="no-js">
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge"> 
		<meta name="viewport" content="width=device-width, initial-scale=1"> 
		<title>Room Coding</title>
		<meta name="description" content="Tab Styles Inspiration: A small collection of styles for tabs" />
		<meta name="keywords" content="tabs, inspiration, web design, css, modern, effects, svg" />
		<meta name="author" content="Codrops" />
		<link rel="shortcut icon" href="../favicon.ico">
		<link rel="stylesheet" type="text/css" href="TabStylesInspiration/TabStylesInspiration/css/normalize.css" /> 
		<link rel="stylesheet" type="text/css" href="TabStylesInspiration/TabStylesInspiration/css/demo.css" />
		<link rel="stylesheet" type="text/css" href="TabStylesInspiration/TabStylesInspiration/css/tabs.css" />
		<link rel="stylesheet" type="text/css" href="TabStylesInspiration/TabStylesInspiration/css/tabstyles.css" />
		<link rel="stylesheet" type="text/css" href="user/css/user.css" />
  		<script src="TabStylesInspiration/TabStylesInspiration/js/modernizr.custom.js"></script>
	</head>
	<body>

		<svg class="hidden">
			<defs>
				<path id="tabshape" d="M80,60C34,53.5,64.417,0,0,0v60H80z"/>
			</defs>
		</svg>
		<div class="container">
			<!-- Top Navigation -->
			<!--<div class="codrops-top clearfix">
				<a class="codrops-icon codrops-icon-prev" href="http://tympanus.net/Tutorials/PagePreloadingEffect/"><span>Previous Demo</span></a>
				<span class="right"><a class="codrops-icon codrops-icon-drop" href="http://tympanus.net/codrops/?p=19559"><span>Back to the Codrops Article</span></a></span>
			</div>
			<header class="codrops-header">
				 <h1>Tab Styles Inspiration <span>A small collection of styles for tabs</span></h1> 
				<p class="support">Your browser does not support <strong>flexbox</strong>! <br />Please view this demo with a <strong>modern browser</strong>.</p>
			</header>-->
			
			<section>
				<div class="tabs tabs-style-shape">
					<nav>
						<ul>
							<li>
								<a href="#section-shape-1" > 
									<svg viewBox="0 0 80 60" preserveAspectRatio="none" ><use xlink:href="#tabshape"></use></svg>
									<span  class="icon icon-home">Home</span>
									
								</a>
							</li>
							<li>
								<a href="#section-shape-2">
									<svg viewBox="0 0 80 60" preserveAspectRatio="none"><use xlink:href="#tabshape"></use></svg>
									<svg viewBox="0 0 80 60" preserveAspectRatio="none"><use xlink:href="#tabshape"></use></svg>
									<span class="icon icon-display">Demo</span>
								</a>
							</li>
							<li>
								<a href="#section-shape-3">
									<svg viewBox="0 0 80 60" preserveAspectRatio="none"><use xlink:href="#tabshape"></use></svg>
									<svg viewBox="0 0 80 60" preserveAspectRatio="none"><use xlink:href="#tabshape"></use></svg>
									<span class="icon icon-plug">Say Us</span>
								</a>
							</li>
							
						</ul>
					</nav>
					<div class="content-wrap">
						<section id="section-shape-1">
							<jsp:include page="Home.jsp" flush="true"></jsp:include>						
						</section>
						<section id="section-shape-2">
							 <iframe src ="jsp/MappingManager.jsp" frameborder="0" bordercolor ="" marginwidth ="0" width ="100%" height ="650" scolling="no" align="" name="ifr" hspace="0" vspace="0"></iframe>

						</section>
						<section id="section-shape-3">
							<jsp:include page="SayUs.jsp" flush="true"></jsp:include>
						</section>
					</div><!-- /content -->
				</div><!-- /tabs -->
			</section>
			
<!-- PayPal Donation 2016.01.02 -->
<section class="related">
					
<a>			
<form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top">
<h3>To Developer</h3>
<input type="hidden" name="cmd" value="_s-xclick">
<input type="hidden" name="hosted_button_id" value="9WFEQTQUKHX8G">
<input type="image" src="https://www.paypalobjects.com/en_US/GB/i/btn/btn_donateCC_LG.gif" border="0" name="submit" alt="PayPal – The safer, easier way to pay online.">
<img alt="" border="0" src="https://www.paypalobjects.com/ko_KR/i/scr/pixel.gif" width="1" height="1">

</form>
</a>
			</section>
		</div><!-- /container -->
		<script src="TabStylesInspiration/TabStylesInspiration/js/cbpFWTabs.js"></script>
		<script>
			(function() {

				[].slice.call( document.querySelectorAll( '.tabs' ) ).forEach( function( el ) {
					new CBPFWTabs( el );
				});

			})();
		</script>
	<!-- 화면 맨위로 기능 Start -->
	<a id="toTop" href="#"><img src="user/img/top.png" width="55" height="55" alt="맨 위로"></a>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js"></script>
	<script src="user/js/jquery.scroll.pack.js"></script>
	<script src="user/js/jquery.easing.js"></script>
		<script>
	
		 $(function(){$("#toTop").scrollToTop({speed:1000,ease:"easeOutBack",start:200})});
		 
		 </script>
	<!-- 화면 맨위로 기능 End -->
	</body>
</html>