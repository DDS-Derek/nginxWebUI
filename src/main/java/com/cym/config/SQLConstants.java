package com.cym.config;

import java.util.HashMap;
import java.util.Map;

import javax.script.SimpleBindings;

import cn.hutool.script.ScriptUtil;

public class SQLConstants {
    public static String SUFFIX = "`";

    public static String ORDER_TYPE_INT = "SIGNED";

    public static String LIMIT_SCRIPT = "(function() { return \" LIMIT \" + offset + \",\" + limit;})();";

    public static String cutPage(Integer curr,Integer limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("curr", curr);
        params.put("offset", (curr - 1) * limit );
        params.put("limit", limit);
        return (String)ScriptUtil.eval(LIMIT_SCRIPT, new SimpleBindings(params));
    }
}