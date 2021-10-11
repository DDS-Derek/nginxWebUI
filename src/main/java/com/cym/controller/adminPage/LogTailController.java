package com.cym.controller.adminPage;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import com.cym.model.Log;
import com.cym.utils.ApplicationContextRegister;
import com.cym.utils.SystemTool;
import com.cym.utils.TailLogThread;

import cn.craccd.sqlHelper.utils.SqlHelper;

@ServerEndpoint("/adminPage/logTail/{id}")
@Controller
public class LogTailController {

	Map<String, Process> processMap = new HashMap<>();
	Map<String, InputStream> inputStreamMap = new HashMap<>();

//	private Process process;
//	private InputStream inputStream;

	/**
	 * 新的WebSocket请求开启
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("id") String id) {

		ApplicationContext act = ApplicationContextRegister.getApplicationContext();
		SqlHelper sqlHelper = act.getBean(SqlHelper.class);

		try {
			// 执行tail -f命令
			Log log = sqlHelper.findById(id, Log.class);

			Process process = null;
			InputStream inputStream = null;

			if (SystemTool.isWindows()) {
				process = Runtime.getRuntime().exec("powershell Get-Content " + log.getPath() + " -Tail 20");
			} else {
				process = Runtime.getRuntime().exec("tail -f " + log.getPath() + " --line 20");
			}
			inputStream = process.getInputStream();

			processMap.put(id, process);
			inputStreamMap.put(id, inputStream);

			// 一定要启动新的线程，防止InputStream阻塞处理WebSocket的线程
			TailLogThread thread = new TailLogThread(inputStream, session);
			thread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * WebSocket请求关闭
	 */
	@OnClose
	public void onClose(@PathParam("id") String id) {
		try {
			InputStream inputStream = inputStreamMap.get(id);
			Process process = processMap.get(id);

			if (inputStream != null) {
				inputStream.close();
			}
			
			if (process != null) {
				process.destroy();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OnError
	public void onError(Throwable thr) {
		thr.printStackTrace();
	}
}
