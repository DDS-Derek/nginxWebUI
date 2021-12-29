package com.cym;

import com.power.common.util.DateTimeUtil;
import com.power.doc.constants.DocGlobalConstants;
import com.power.doc.model.ApiConfig;
import com.power.doc.model.SourceCodePath;
import org.junit.Test;
import org.noear.solonhat.smartdoc.SolonHtmlApiDocBuilder;

public class Doc {

	/**
	 * 包括设置请求头，缺失注释的字段批量在文档生成期使用定义好的注释
	 */
	@Test
	public void generate() {
		ApiConfig config = new ApiConfig();

		config.setServerUrl("http://your_ip:8080");

		// 设置用md5加密html文件名,不设置为true，html的文件名将直接为controller的名称
		config.setMd5EncryptedHtmlName(true);
		config.setStrict(false);// true会严格要求注释，推荐设置true
		config.setOutPath(DocGlobalConstants.HTML_DOC_OUT_PATH);// 输出到static/doc下

		// 不指定SourcePaths默认加载代码为项目src/main/java下的,如果项目的某一些实体来自外部代码可以一起加载
		config.setSourceCodePaths(//
				SourceCodePath.path().setDesc("smart-doc").setPath("src/main/java/com/cym/controller/api/"), //
				SourceCodePath.path().setDesc("smart-doc").setPath("src/main/java/com/cym/model/"), //
				SourceCodePath.path().setDesc("smart-doc").setPath("src/main/java/com/cym/sqlhelper/"), //
				SourceCodePath.path().setDesc("smart-doc").setPath("src/main/java/com/cym/utils/") //
		);

		long start = System.currentTimeMillis();
		SolonHtmlApiDocBuilder.buildApiDoc(config);
		long end = System.currentTimeMillis();
		DateTimeUtil.printRunTime(end, start);
	}
}
