package com.cym.controller.adminPage;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;

import com.cym.utils.BaseController;

@Mapping("/adminPage/abort")
@Controller
public class AbortController extends BaseController {

	@Mapping("")
	public ModelAndView index( ModelAndView modelAndView) {

		modelAndView.view("/adminPage/abort/index");
		return modelAndView;
	}

}
