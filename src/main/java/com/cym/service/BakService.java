package com.cym.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cym.model.Bak;
import com.cym.model.BakSub;
import com.cym.model.Remote;
import com.cym.utils.JsonResult;

import cn.craccd.sqlHelper.bean.Page;
import cn.craccd.sqlHelper.bean.Sort;
import cn.craccd.sqlHelper.bean.Sort.Direction;
import cn.craccd.sqlHelper.utils.ConditionAndWrapper;
import cn.craccd.sqlHelper.utils.SqlHelper;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

@Service
public class BakService {
	@Autowired
	SqlHelper sqlHelper;
	@Autowired
	RemoteService remoteService;

	public Page<Bak> getList(Page page) {
		return sqlHelper.findPage(new ConditionAndWrapper(), new Sort(Bak::getTime, Direction.DESC), page, Bak.class);
	}

	public List<BakSub> getSubList(String id) {
		return sqlHelper.findListByQuery(new ConditionAndWrapper().eq(BakSub::getBakId, id), BakSub.class);
	}

	public void del(String id) {
		sqlHelper.deleteById(id, Bak.class);
		sqlHelper.deleteByQuery(new ConditionAndWrapper().eq(BakSub::getBakId, id), BakSub.class);
	}

	public void delAll() {
		sqlHelper.deleteByQuery(new ConditionAndWrapper(), Bak.class);
		sqlHelper.deleteByQuery(new ConditionAndWrapper(), BakSub.class);
	}

	public Bak getPre(String id) {
		Bak bak = sqlHelper.findById(id, Bak.class);
		Bak pre = sqlHelper.findOneByQuery(new ConditionAndWrapper().lt(Bak::getTime, bak.getTime()), new Sort(Bak::getTime, Direction.DESC), Bak.class);

		return pre;
	}

	public boolean hasApplyNumber(String applyNumber) {
		return sqlHelper.findCountByQuery(new ConditionAndWrapper().eq(Bak::getApplyNumber, applyNumber), Bak.class) > 0;
	}

	public boolean hasVersion(String version) {
		return sqlHelper.findCountByQuery(new ConditionAndWrapper().eq(Bak::getVersion, version), Bak.class) > 0;
	}

	public Boolean isApplying(String remoteName) {
		if(StrUtil.isEmpty(remoteName)) {
			return sqlHelper.findCountByQuery(new ConditionAndWrapper().eq(Bak::getStatus, 0), Bak.class) > 0;
		} else {
			Remote remote = remoteService.findByName(remoteName);
			if (remote != null) {
				Map<String, Object> map = new HashMap<>();
				map.put("creditKey", remote.getCreditKey());
				String rs = HttpUtil.post("http://" + remote.getIp() + ":" + remote.getPort() + "/api/nginx/isApplying", map);
				System.out.println(rs);
				JsonResult jsonResult = JSONUtil.toBean(rs, JsonResult.class);
				return (Boolean) jsonResult.getObj();
			}
			return false;
		}
		
	}

}
