<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="title" required="true" type="java.lang.String"%>

      <!-- Select Basic -->
      <div class="control-group">
        <label class="control-label">${title}</label>
        <div class="controls">
          <select id="selectbasic" name="selectbasic"
            class="input-xlarge">
            <option>Option one</option>
            <option>Option two</option>
          </select>
        </div>
      </div>