<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag"%>
<%@ page session="false"%>

<div class="container">

  <!-- Main hero unit for a primary marketing message or call to action -->
  <div class="hero-unit">
    <div align="center">
      <table class="table-hover " style="min-width: 35%;">
        <tr>
          <td><spring:message code="label.list_incomes" />:</td>
          <td>
            <div class="text-right">${incomes}</div>
          </td>
        </tr>
        <tr>
          <td><spring:message code="label.list_expenses" />:</td>
          <td>
            <div class="text-right">${expenses}</div>
          </td>
        </tr>
        <tr>
          <td><spring:message code="label.balance" />:</td>
          <td>
            <div class="text-right">${balance}</div>
          </td>
        </tr>

      </table>
    </div>
  </div>

</div>
<!-- /container -->


