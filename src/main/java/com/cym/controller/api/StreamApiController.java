package com.cym.controller.api;

import cn.hutool.core.util.StrUtil;
import com.cym.controller.adminPage.StreamController;
import com.cym.model.Stream;
import com.cym.service.StreamService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;

import java.util.List;

/**
 * Stream接口
 *
 */
@Mapping("/api/stream")
@Controller
public class StreamApiController extends BaseController {

    @Inject
    StreamController streamController;
    @Inject
    StreamService streamService;

    /**
     * 获取stream配置列表
     *
     */
    @Mapping("getAll")
    public JsonResult<List<Stream>> getAll() {
        return renderSuccess(streamService.findAll());
    }

    /**
     * 添加或编辑stream
     *
     * @param stream 流转发
     *
     */
    @Mapping("addOver")
    public JsonResult addOver(Stream stream) {
        return streamController.addOver(stream);
    }

    /**
     * 删除指定的stream配置
     *
     * @param id  stream标识符
     *
     */
    @Mapping("del")
    public JsonResult del(String id) {
        return streamController.del(id);
    }

    /**
     * 删除全部stream配置
     *
     */
    @Mapping("delAll")
    public JsonResult delAll() {
        List<Stream> streams = streamService.findAll();
        String id = "";
        for(Stream stream : streams) {
            if(!StrUtil.isEmpty(id)) {
                id += ",";
            }
            id += stream.getId();
        }
        return streamController.del(id);
    }
}
