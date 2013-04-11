<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<c:url var="url" value="/resources/" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Cashflow</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
    <script src="http://code.jquery.com/jquery-latest.js"></script>
<link href="${url}css/bootstrap.min.css" rel="stylesheet">
<link href="${url}css/bootstrap-responsive.min.css" rel="stylesheet">
</head>
<body>
	<h1>Hello world!</h1>
	${controllerMessage}
</body>
<footer>
    <script src="http://code.jquery.com/jquery.js"></script>
    <script src="${url}js/bootstrap.min.js"></script>
</footer>
</html>
