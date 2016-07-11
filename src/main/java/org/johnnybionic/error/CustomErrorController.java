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

	private static final String UNAUTHORISED = "/403";
	private static final String ERROR_PATH = "/error";

	private final ErrorAttributes errorAttributes;
	
	@Autowired
	public CustomErrorController (ErrorAttributes errorAttributes) {
		this.errorAttributes = errorAttributes;
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}

	@RequestMapping(ERROR_PATH)
	public String customError(Model model,HttpServletRequest request) {

		Map<String,Object> error = getErrorAttributes(request, true);
		
		model.addAttribute("error", error.get("error"));
		model.addAttribute("message", error.get("message"));

		return "error";
	}
	
	@RequestMapping(UNAUTHORISED)
	public String pageNotFound(Model model,HttpServletRequest request){
		model.addAttribute("error", getErrorAttributes(request,true));
		return "goAway";
	}

	private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
		RequestAttributes requestAttributes = new ServletRequestAttributes(request);
		return this.errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
	}

}
