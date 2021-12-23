package com.cym.controller.adminPage;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cym.model.Stream;
import com.cym.service.StreamService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;
import com.cym.utils.SnowFlakeUtils;

import cn.hutool.core.util.StrUtil;

@Controller
@Mapping("/adminPage/stream")
public class StreamController extends BaseController {
	@Inject
	StreamService streamService;

	@Mapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) {
		List<Stream> streamList = streamService.findAll();

		modelAndView.addObject("streamList", streamList);
		modelAndView.setViewName("/adminPage/stream/index");
		return modelAndView;
	}

	@Mapping("addOver")
	@ResponseBody
	public JsonResult addOver(Stream stream) {
		if (StrUtil.isEmpty(stream.getId())) {
			stream.setSeq( SnowFlakeUtils.getId());
		}
		sqlHelper.insertOrUpdate(stream);

		return renderSuccess();
	}
	

	@Mapping("addTemplate")
	@ResponseBody
	public JsonResult addTemplate(String templateId) {
		streamService.addTemplate(templateId);
		
		return renderSuccess();
	}

	@Mapping("detail")
	@ResponseBody
	public JsonResult detail(String id) {
		return renderSuccess(sqlHelper.findById(id, Stream.class));
	}

	@Mapping("del")
	@ResponseBody
	public JsonResult del(String id) {
		sqlHelper.deleteById(id, Stream.class);

		return renderSuccess();
	}

	@Mapping("setOrder")
	@ResponseBody
	public JsonResult setOrder(String id, Integer count) {
		streamService.setSeq(id, count);

		return renderSuccess();
	}
}
