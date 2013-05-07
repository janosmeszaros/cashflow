<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:url var="url" value="/resources/" />
<c:url var="home" value="/" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="error.title" /></title>

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
        <spring:message code="error.page_not_found" />
        <small><font face="Tahoma" color="red">  <spring:message code="label.error" /> 404</font></small>
      </h1>
      <br />
      <p>
      <spring:message code="error.message" />
      </p>

      <a href="${home}" class="btn btn-large btn-info"><i
        class="icon-home icon-white"></i>  <spring:message code="error.take_me_home" /> </a>
    </div>
  </div>
  <!-- By ConnerT HTML & CSS Enthusiast -->

  <!-- Le javascript
    ================================================== -->
  <!-- Placed at the end of the document so the pages load faster -->
  <script src="http://code.jquery.com/jquery.js"></script>
  <script src="${url}js/bootstrap.min.js"></script>
</body>
</html>