<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>

<div class="container">

  <form class="form-horizontal">
    <fieldset>

      <!-- Form Name -->
      <legend>Add Income</legend>

      <!-- Appended Input-->
      <div class="control-group">
        <label class="control-label">Amount:</label>
        <div class="controls">
          <div class="input-append">
            <input id="amounttext" name="amounttext" class="span2"
              placeholder="0.00" type="text" required="required">
            <span class="add-on">HUF</span>
          </div>

        </div>
      </div>

      <!-- Select Basic -->
      <div class="control-group">
        <label class="control-label">Category: </label>
        <div class="controls">
          <select id="selectbasic" name="selectbasic"
            class="input-xlarge">
            <option>Option one</option>
            <option>Option two</option>
          </select>
        </div>
      </div>

      <!-- Button -->
      <div class="control-group">
        <label class="control-label">Date:</label>
        <div class="controls">
          <!--           <button id="datebutton" name="datebutton" class="btn btn-info">2013.04.18</button> -->
          <div class="input-append date" id="datepicker" data-date="12-02-2013"
            data-date-format="dd-mm-yyyy">
            <input class="span2" size="16" type="text"
              value="12-02-2013"> <span class="add-on"><i
              class="icon-calendar"></i></span>
          </div>
        </div>
      </div>


      <!-- Textarea -->
      <div class="control-group">
        <label class="control-label">Notes:</label>
        <div class="controls">
          <textarea id="notestextarea" name="notestextarea"></textarea>
        </div>
      </div>

      <!-- Multiple Radios (inline) -->
      <div class="control-group">
        <label class="control-label">Recurring type</label>
        <div class="controls">
          <label class="radio inline"> <input type="radio"
            name="radios" value="None" checked="checked"> None
          </label> <label class="radio inline"> <input type="radio"
            name="radios" value="Daily"> Daily
          </label> <label class="radio inline"> <input type="radio"
            name="radios" value="Weekly"> Weekly
          </label> <label class="radio inline"> <input type="radio"
            name="radios" value="Biweekly"> Biweekly
          </label> <label class="radio inline"> <input type="radio"
            name="radios" value="Monthly"> Monthly
          </label> <label class="radio inline"> <input type="radio"
            name="radios" value="Annually"> Annually
          </label>
        </div>
      </div>

      <!-- Button -->
      <div class="control-group">
        <label class="control-label"></label>
        <div class="controls">
          <button id="submitbutton" name="submitbutton"
            class="btn btn-success">Submit</button>
        </div>
      </div>

    </fieldset>
  </form>


</div>