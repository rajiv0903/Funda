package com.fanniemae.mbsportal.api.exceptions;

import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fanniemae.mbsportal.api.editor.AcceptedTradesBooleanEditor;
import com.fanniemae.mbsportal.api.editor.RegionColumnListEditor;
import com.fanniemae.mbsportal.api.editor.SortByEditor;
import com.fanniemae.mbsportal.constants.AcceptedTradesBoolean;
import com.fanniemae.mbsportal.constants.RegionColumnList;
import com.fanniemae.mbsportal.gf.pojo.SortBy;

/**
 * @class This class is built to globally handle the custom exceptions being
 *        thrown from the controller methods. We can handle the custom
 *        exceptions in one place to return the same error message from the API
 *        
 */
@ControllerAdvice(annotations = RestController.class)
@RequestMapping(produces =  MediaType.APPLICATION_JSON_VALUE)
public class GlobalExceptionHandler {

    /**
     * @param dataBinder
     */
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(RegionColumnList.class, new RegionColumnListEditor());
        dataBinder.registerCustomEditor(SortBy.class, new SortByEditor());
        dataBinder.registerCustomEditor(AcceptedTradesBoolean.class, new AcceptedTradesBooleanEditor());
    }
}
