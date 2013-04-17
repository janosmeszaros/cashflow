<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<c:url var="url" value="/resources/" />
<c:url var="home" value="/" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Ooops...</title>

<!-- Le styles -->
<link href="${url}css/bootstrap.min.css" rel="stylesheet">
<style type="text/css">
body {
	padding-top: 60px;
	padding-bottom: 40px;
}

.center {
	text-align: center;
	margin-left: auto;
	margin-right: auto;
	margin-bottom: auto;
	margin-top: auto;
}
</style>
</head>

<body>
    <div class="container">
        <div class="hero-unit center">
            <h1>
                Page Not Found <small><font face="Tahoma"
                    color="red">Error 404</font></small>
            </h1>
            <br />
            <p>
                The page you requested could not be found, either
                contact your webmaster or try again. Use your browsers <b>Back</b>
                button to navigate to the page you have previously come
                from
            </p>
            <p>
                <b>Or you could just press this neat little button:</b>
            </p>
            <a href="${home}" class="btn btn-large btn-info"><i
                class="icon-home icon-white"></i> Take Me Home</a>
        </div>
        <br />
        <div class="thumbnail">
            <center>
                <h2>Recent Content :</h2>
            </center>
        </div>
        <br />
        <div class="thumbnail span3 center">
            <h3>Try This...</h3>
            <p>write about your error page content here and give
                some fool a good load of information or not</p>
            <div class="hero-unit">
                <img src="http://placehold.it/100x100">
                <!--Why Not Put a Picture To Celebrate Your 404-->
                <p></p>
            </div>
            <a href="#" class="btn btn-danger btn-large"><i
                class="icon-share icon-white"></i> Take Me There...</a>
        </div>
        <div class="thumbnail span3 center">
            <h3>Try This...</h3>
            <p>write about your error page content here and give
                some fool a good load of information or not</p>
            <div class="hero-unit">
                <img src="http://placehold.it/100x100">
                <!--Why Not Put a Picture To Celebrate Your 404-->
                <p></p>
            </div>
            <a href="#" class="btn btn-danger btn-large"><i
                class="icon-share icon-white"></i> Take Me There...</a>
        </div>
        <div class="thumbnail span3 center">
            <h3>Try This...</h3>
            <p>write about your error page content here and give
                some fool a good load of information or not</p>
            <div class="hero-unit">
                <img src="http://placehold.it/100x100">
                <!--Why Not Put a Picture To Celebrate Your 404-->
                <p></p>
            </div>
            <a href="#" class="btn btn-danger btn-large"><i
                class="icon-share icon-white"></i> Take Me There...</a>
        </div>
        <div class="thumbnail span3 center">
            <h3>Try This...</h3>
            <p>write about your error page content here and give
                some fool a good load of information or not</p>
            <div class="hero-unit">
                <img src="http://placehold.it/100x100">
                <!--Why Not Put a Picture To Celebrate Your 404-->
                <p></p>
            </div>
            <a href="#" class="btn btn-danger btn-large"><i
                class="icon-share icon-white"></i> Take Me There...</a>
        </div>
        <br />
        <p></p>

    </div>
    <!-- By ConnerT HTML & CSS Enthusiast -->

    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="http://code.jquery.com/jquery.js"></script>
    <script src="${url}js/bootstrap.min.js"></script>
</body>
</html>