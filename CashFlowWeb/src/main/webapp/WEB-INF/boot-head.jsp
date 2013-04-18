<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<c:url var="url" value="/resources/" />
<c:url var="home" value="/" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html lang="en">
  <head>
    <meta charset="utf-8">
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
          <a class="brand" href="${home}">CashFlow</a>
          <div class="nav-collapse collapse">
            <ul class="nav">
              <li class="active"><a href="#">Home</a></li>
              <li><a href="#about">About</a></li>
              <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">List <b class="caret"></b></a>
                <ul class="dropdown-menu">
                  <li><a href="list">Incomes</a></li>
                  <li><a href="#">Expenses</a></li>
                  <li class="divider"></li>
                  <li class="nav-header">Recurring</li>
                  <li><a href="#">Incomes</a></li>
                  <li><a href="#">Bills</a></li>
                </ul>
              </li>
              <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Actions <b class="caret"></b></a>
                <ul class="dropdown-menu">
                  <li><a href="add_statement">Add Income</a></li>
                  <li><a href="add_statement">Add Expense</a></li>
                  <li><a href="add_bill">Add Bill</a></li>
                  <li class="divider"></li>
                  <li class="nav-header">Others</li>
                  <li><a href="#">Add Category</a></li>
                </ul>
              </li>
            </ul>
            <form class="navbar-form pull-right">
              <input class="span2"  type="text" placeholder="Email" >
              <input class="span2" type="password" placeholder="Password">
              <button type="submit" class="btn">Sign in</button>
            </form>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>

