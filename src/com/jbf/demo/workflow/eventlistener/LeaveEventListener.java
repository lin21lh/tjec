package com.jbf.demo.workflow.eventlistener;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jbf.demo.workflow.dao.LeaveNoteDao;
import com.jbf.demo.workflow.po.LeaveNote;
import com.jbf.workflow.vo.EventSourceVO;

@Component("com.jbf.demo.workflow.eventlistener.LeaveEventListener")
public class LeaveEventListener {

	@Autowired
	LeaveNoteDao leaveNoteDao;

	public void toEnd(String execId, Map args, Map vars,
			EventSourceVO eventSource) throws Exception {
		Object uuid = vars.get("objid");
		if (uuid == null) {
			throw new Exception("无法取得objid环境变量");
		}

		LeaveNote note = leaveNoteDao.get((String) uuid);
		if (note == null) {
			throw new Exception("无法取得uuid为" + uuid + "的实例！");
		}
		note.setWfnode("流程结束");
		leaveNoteDao.update(note);
	}

	public void operationA(String execId, Map args, Map variables,
			EventSourceVO eventSource) throws Exception {
		// 打印execId
		System.out.println("============= operationA 开始=============");
		System.out.println("execid is " + execId);
		System.out.println("打印参数列表：");
		//
		for (Object key : args.keySet()) {
			System.out.println(key + " : " + args.get(key));
		}
		System.out.println("打印环境变量：");
		for (Object key : variables.keySet()) {
			System.out.println(key + " : " + variables.get(key));
		}
		System.out.println("============= operationA 结束=============");
	}
}
