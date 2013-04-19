<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>

<div class="container">

  <c:url var="add" value="/add_user" />
  <form class="form-horizontal" action="${add}">
    <fieldset>
      <legend> <spring:message code="label.register"/>  </legend>

      <div class="control-group">
        <!-- Username -->
        <label class="control-label" for="username"> <spring:message code="label.username"/>  </label>
        <div class="controls">
        <div class="input-prepend">
          <span class="add-on"><i class="icon-user"></i></span> <input
            type="text" id="username" name="username" placeholder=""
            class="input-xlarge">
          <p class="help-block">Username can contain any letters or
            numbers, without spaces</p>
        </div>
        </div>
      </div>

      <div class="control-group">
        <!-- E-mail -->
        <label class="control-label" for="email">E-mail</label>
        <div class="controls">
        <div class="input-prepend">
          <span class="add-on"><i class="icon-envelope"></i></span> <input
            type="text" id="email" name="email" placeholder=""
            class="input-xlarge">
          <p class="help-block">Please provide your E-mail</p>
        </div>
        </div>
      </div>

      <div class="control-group">
        <!-- Password-->
        <label class="control-label" for="password"> <spring:message code="label.password"/>  </label>
        <div class="controls">
        <div class="input-prepend">
        <span class="add-on"><i class="icon-key"></i></span>
          <input type="password" id="password" name="password"
            placeholder="" class="input-xlarge">
          <p class="help-block">Password should be at least 4
            characters</p>
        </div>
        </div>
      </div>

      <div class="control-group">
        <!-- Password -->
        <label class="control-label" for="password_confirm"> <spring:message code="label.password_confirm"/>  </label>
        <div class="controls">
        <div class="input-prepend">
        <span class="add-on"><i class="icon-key"></i></span>
          <input type="password" id="password_confirm"
            name="password_confirm" placeholder="" class="input-xlarge">
          <p class="help-block">Please confirm password</p>
        </div>
        </div>
      </div>

      <div class="control-group">
        <!-- Button -->
        <div class="controls">
          <button class="btn btn-success"> <spring:message code="label.register"/>  </button>
        </div>
      </div>
    </fieldset>
  </form>

</div>
