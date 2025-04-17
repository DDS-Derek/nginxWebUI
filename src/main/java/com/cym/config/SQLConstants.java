package com.cym.config;

import cn.hutool.script.ScriptRuntimeException;
import cn.hutool.script.ScriptUtil;

import javax.script.SimpleBindings;
import java.util.HashMap;
import java.util.Map;

public class SQLConstants {
    // Table names
    public static String TABLE_PREFIX = "`";
    public static String TABLE_SUFFIX = "`";
//    public static String TABLE_PREFIX = "\"";
//    public static String TABLE_SUFFIX = "\"";
    
    // Column names
    public static String COLUMN_PREFIX = "`";
    public static String COLUMN_SUFFIX = "`";
//    public static String COLUMN_PREFIX = "\"";
//    public static String COLUMN_SUFFIX = "\"";
    
    // Common columns
    public static String ID_COLUMN = "`id`";
//    public static String ID_COLUMN = "\"id\"";

    public static String ORDER_TYPE_INT = "SIGNED";
//    public static String ORDER_TYPE_INT = "BIGINT";

    public static String LIMIT_SCRIPT = "(function() { return \" LIMIT \" + offset + \",\" + limit;})();";

    public static String cutPage(Integer curr,Integer limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("curr", curr);
        params.put("offset", (curr - 1) * limit );
        params.put("limit", limit);
        return (String)ScriptUtil.eval(LIMIT_SCRIPT, new SimpleBindings(params));
    }
}