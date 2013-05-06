<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page session="false"%>
<c:url var="url" value="/resources/" />
<c:url var="home" value="/" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html lang="en">
  <head>
    <title>CashFlow</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link href="${url}css/bootstrap.min.css" rel="stylesheet">
    <style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
    </style>
    <link href="${url}css/bootstrap-responsive.min.css" rel="stylesheet">
    <link href="${url}css/font-awesome.min.css" rel="stylesheet" >
    <link href="${url}css/datepicker.css" rel="stylesheet" >
    <link href="${url}css/jquery.dataTables.css" rel="stylesheet" >

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="../assets/js/html5shiv.js"></script>
    <![endif]-->

    <!-- Fav and touch icons -->
<!--     <link rel="apple-touch-icon-precomposed" sizes="144x144" href="../assets/ico/apple-touch-icon-144-precomposed.png"> -->
<!--     <link rel="apple-touch-icon-precomposed" sizes="114x114" href="../assets/ico/apple-touch-icon-114-precomposed.png"> -->
<!--       <link rel="apple-touch-icon-precomposed" sizes="72x72" href="../assets/ico/apple-touch-icon-72-precomposed.png"> -->
<!--                     <link rel="apple-touch-icon-precomposed" href="../assets/ico/apple-touch-icon-57-precomposed.png"> -->
                                   <link rel="shortcut icon" href="${url}img/favicon.ico" type="image/x-icon">
  </head>
  
<body>

<div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="brand active" href="${home}">CashFlow</a>
          <div class="nav-collapse collapse">
            <ul class="nav">
              <li class="active"><a href="#"><spring:message code="navbar.home"/></a></li>
              <li><a href="#about"><spring:message code="navbar.about"/></a></li>
              <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown"> <spring:message code="navbar.list"/> <b class="caret"></b></a>
                <ul class="dropdown-menu">
                  <li><a href="list_incomes"> <spring:message code="navbar.incomes"/> </a></li>
                  <li><a href="list_expenses"> <spring:message code="navbar.expenses"/> </a></li>
                  <li class="divider"></li>
                  <li class="nav-header"> <spring:message code="navbar.recurring"/> </li>
                  <li><a href="list_recurring_incomes"> <spring:message code="navbar.incomes"/> </a></li>
                  <li><a href="list_bills"> <spring:message code="navbar.bills"/> </a></li>
                </ul>
              </li>
              <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message code="navbar.actions"/><b class="caret"></b></a>
                <ul class="dropdown-menu">
                  <li><a href="add_income"> <spring:message code="navbar.add_income"/> </a></li>
                  <li><a href="add_expense"> <spring:message code="navbar.add_expense"/> </a></li>
                  <li><a href="add_bill"> <spring:message code="navbar.add_bill"/> </a></li>
                  <li class="divider"></li>
                  <li class="nav-header"> <spring:message code="navbar.others"/> </li>
                  <li><a href="manage_categories"> <spring:message code="navbar.manage_categories"/> </a></li>
                </ul>
              </li>
            </ul>
            <form class="navbar-form pull-right">
              <input class="span2"  type="text" placeholder="Email" >
              <input class="span2" type="password" placeholder="<spring:message code="label.password"/>">
              <button type="submit" class="btn"> <spring:message code="navbar.sign_in"/> </button>
            </form>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>

