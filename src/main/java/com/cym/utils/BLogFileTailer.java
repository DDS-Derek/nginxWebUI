package com.cym.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.noear.solon.annotation.Component;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

@Component
public class BLogFileTailer {
	// 定时过期map
	public Map<String, Long> filePointerMap =  
			ExpiringMap.builder()
            .expiration(60, TimeUnit.SECONDS)
            .expirationPolicy(ExpirationPolicy.ACCESSED)
            .build();

	public String run(String guid, String path) {
		System.out.println(filePointerMap);
		
		Long pointer = null;
		if (filePointerMap.get(guid) != null) {
			pointer = filePointerMap.get(guid);
		} else {
			pointer = 0l;
			filePointerMap.put(guid, pointer);
		}
		File logfile = new File(path);

		String str = "";
		try {
			RandomAccessFile file = new RandomAccessFile(logfile, "r");
			long fileLength = logfile.length(); // 新文件的长度

			// 新文件的长度小于上一次读取文件的长度时，从头开始读
			if (fileLength < pointer) {
				pointer = 0l;
				filePointerMap.put(guid, pointer);
			}

			// 新文件长度大于上一次读取文件的长度时
			// 通过 FilePointer 从上一次读取文件的结尾开始读
			if (fileLength > pointer) {
				// RandomAccessFile.seek():表示从文件的第几个位置开始搜索
				file.seek(pointer);
				String line = file.readLine();
				while (line != null) {
					// 过滤掉：当读取到换行符 '/n' 的时候,会读取到一行的内容为空字符串
					if ("".equals(line)) {
						line = file.readLine();
						continue;
					}
					str += "<div>" + line.replaceAll(" ", "&nbsp;").replaceAll("	", "&emsp;") + "</div>";
					line = file.readLine(); // 读取下一行数据
				}
				// RandomAccessFile.getFilePointer():记录文件指针
				pointer = file.getFilePointer();
				// 存放到map变量中
				filePointerMap.put(guid, pointer);
			}
			file.close();
		} catch (IOException e) {
			// 异常信息
			e.printStackTrace();
		}

		return str;
	}
}
