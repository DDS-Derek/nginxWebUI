package com.cym.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import org.noear.solon.annotation.Component;

@Component
public class BLogFileTailer {

	public Map<String, FilePointer> filePointerMap = new HashMap<>();

	public String run(String guid, String path) {

		Long pointer = null;
		if (filePointerMap.get(guid) != null) {
			FilePointer filePointer = filePointerMap.get(guid);
			filePointer.setLastTime(System.currentTimeMillis());
			pointer = filePointer.getPointer();
		} else {
			pointer = 0l;
			FilePointer filePointer = new FilePointer(pointer, System.currentTimeMillis());
			filePointerMap.put(guid, filePointer);
			pointer = filePointer.getPointer();
		}
		File logfile = new File(path);

		String str = "";
		try {
			RandomAccessFile file = new RandomAccessFile(logfile, "r");
			long fileLength = logfile.length(); // 新文件的长度

			// 新文件的长度小于上一次读取文件的长度时，从头开始读
			if (fileLength < pointer) {
				pointer = 0l;
				FilePointer filePointer = new FilePointer(pointer, System.currentTimeMillis());
				filePointerMap.put(guid, filePointer);
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
				FilePointer filePointer = new FilePointer(pointer, System.currentTimeMillis());
				filePointerMap.put(guid, filePointer);
			}
			file.close();
		} catch (IOException e) {
			// 异常信息
			e.printStackTrace();
		}

		return str;
	}
}
