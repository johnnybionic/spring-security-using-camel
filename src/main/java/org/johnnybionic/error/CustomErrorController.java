package org.johnnybionic.error;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * A custom error handler. Directs 403 errors to a particular page; all others
 * go to a standard page.
 *
 * @author johnny
 *
 */
@Controller
public class CustomErrorController implements ErrorController {

    public static final String ERROR_VIEW_NAME = "error";
    public static final String UNAUTHORISED_VIEW_NAME = "goAway";
    static final String UNAUTHORISED = "/403";
    static final String ERROR_PATH = "/error";

    private final ErrorAttributes errorAttributes;

    @Autowired
    public CustomErrorController(final ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    /**
     * Used when there is not specific handling for an error code.
     *
     * @param model the model
     * @param request the request
     * @return the template to use for the error
     */
    @RequestMapping(ERROR_PATH)
    public String customError(final Model model, final HttpServletRequest request) {

        Map<String, Object> error = getErrorAttributes(request, true);

        model.addAttribute(ERROR_VIEW_NAME, error.get(ERROR_VIEW_NAME));
        model.addAttribute("message", error.get("message"));

        return ERROR_VIEW_NAME;
    }

    /**
     * Specific error handler for 403 Unauthorised.
     *
     * @param model the model
     * @param request the request
     * @return the name of the view to use
     */
    @RequestMapping(UNAUTHORISED)
    public String unauthorised(final Model model, final HttpServletRequest request) {
        model.addAttribute(ERROR_VIEW_NAME, getErrorAttributes(request, true));
        return UNAUTHORISED_VIEW_NAME;
    }

    /**
     * Retrieve the error attributes.
     *
     * @param request the current request
     * @param includeStackTrace include or not the stack trace
     * @return a Map of the attributes
     */
    private Map<String, Object> getErrorAttributes(final HttpServletRequest request, final boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return this.errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }

}
