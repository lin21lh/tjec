package com.jbf.base.filemanage.component.impl;

import java.io.File;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jbf.base.filemanage.component.FileManageComponent;
import com.jbf.base.filemanage.dao.SysFileManageDao;
import com.jbf.base.filemanage.po.SysFileManage;
import com.jbf.common.util.StringUtil;

@Scope("prototype")
@Component
public class FileManageComponentImpl implements FileManageComponent {

	@Autowired
	SysFileManageDao fileManageDao;
	@Override
	public boolean updateKeyid(String keyid, List<Long> itemids) {
		
		if (StringUtil.isBlank(keyid) || itemids == null || itemids.size() == 0)
			return false;
		/**
		String fileItemids = "";
		for (Long itemid : itemids) {
			if (StringUtil.isNotBlank(fileItemids))
				fileItemids += ",";
			fileItemids += itemid;
		}
		
		String hql = "UPDATE SysFileManage t SET t.keyid='" + keyid + "' WHERE t.itemid in(" + fileItemids +")";
		Integer updateSize = fileManageDao.updateByHql(hql);
		*/
		DetachedCriteria dc = DetachedCriteria.forClass(SysFileManage.class);
		dc.add(Property.forName("itemid").in(itemids));
		List<SysFileManage> files = (List<SysFileManage>)fileManageDao.findByCriteria(dc);
		for (SysFileManage file : files) {
			file.setKeyid(keyid);
			fileManageDao.update(file);
		}
		return true;
	}
	
	public List<SysFileManage> findFiles(String elementcode, String keyid) {
		
		return findFiles(elementcode, keyid, null);
	}

	@Override
	public List<SysFileManage> findFiles(String elementcode, String keyid, String usercode) {
		
		
		return findFilesForWf(elementcode, keyid, null, usercode);
	}
	
	public List<SysFileManage> findFilesForWf(String elementcode, String keyid, String stepid) {
		
		return findFilesForWf(elementcode, keyid, stepid, null);
	}
	public List<SysFileManage> findFilesForWf(String itemid) {
		return findFilesByItemid(itemid);
	}
	
	public SysFileManage get(Long itemid) {
		
		return fileManageDao.get(itemid);
	}

	@Override
	public List<SysFileManage> findFilesForWf(String elementcode, String keyid, String stepid, String usercode) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(SysFileManage.class);
		dc.add(Property.forName("elementcode").eq(elementcode));
		dc = dc.add(Property.forName("keyid").eq(keyid));
		if (StringUtil.isNotBlank(usercode))
			dc = dc.add(Property.forName("usercode").eq(usercode));
		
		if (StringUtil.isNotBlank(stepid))
			dc = dc.add(Property.forName("stepid").eq(stepid));
		
		
		return (List<SysFileManage>) fileManageDao.findByCriteria(dc);
	}

	@Override
	public List<SysFileManage> findFilesByItemid(String itemid) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysFileManage.class);
		if(StringUtil.isNotBlank(itemid)&&!itemid.equals("null")){
			dc.add(Property.forName("itemid").eq(Long.parseLong(itemid)));
		}else {
			dc.add(Property.forName("itemid").eq(Long.parseLong("0")));
		}
		return (List<SysFileManage>) fileManageDao.findByCriteria(dc);
	}

	@Override
	public boolean deleteFile(Long itemid) {
		SysFileManage filePo = fileManageDao.get(itemid);
		if (filePo.getSavemode() == 0) {
			if (filePo.getSavemode() == 0) {
				File file = new File(filePo.getFilepath() + filePo.getFilename());
				if (file.exists() && file.isFile()) {
					file.delete();
				}
			}
		}
		fileManageDao.delete(filePo);
		return true;
	}

	@Override
	public boolean deleteFiles(List<Long> itemids) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysFileManage.class);
		dc.add(Property.forName("itemid").in(itemids));
		List<SysFileManage> files = (List<SysFileManage>)fileManageDao.findByCriteria(dc);
		for (SysFileManage filePo : files) {
			if (filePo.getSavemode() == 0) {
				File file = new File(filePo.getFilepath() + filePo.getFilename());
				if (file.exists() && file.isFile()) {
					file.delete();
				}
			}
		}
		fileManageDao.deleteAll(files);
		return true;
	}

	@Override
	public boolean deleteFiles(String keyId, String elementCode) {
		// TODO Auto-generated method stub
		try {
			String sql = "select  itemid, elementcode, keyid, usercode, createtime, title, originalfilename, filename, savemode, filepath, stepid, filesize, remark from  SYS_FILEMANAGE t where  keyid in('"+keyId.replaceAll(",", "','")+"') and elementCode='"+elementCode+"'";
			List<SysFileManage> files = (List<SysFileManage>)fileManageDao.findVoBySql(sql, SysFileManage.class);
			for (SysFileManage filePo : files) {
				if (filePo.getSavemode() == 0) {
					File file = new File(filePo.getFilepath() + filePo.getFilename());
					if (file.exists() && file.isFile()) {
						file.delete();
					}
				}
			}
			fileManageDao.deleteAll(files);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
		
	}

	@Override
	public boolean deleteFilesByKeyId(String xmid) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysFileManage.class);
		dc.add(Property.forName("keyid").eq(xmid));
		List<SysFileManage> files = (List<SysFileManage>)fileManageDao.findByCriteria(dc);
		for (SysFileManage filePo : files) {
			if (filePo.getSavemode() == 0) {
				File file = new File(filePo.getFilepath() + filePo.getFilename());
				if (file.exists() && file.isFile()) {
					file.delete();
				}
			}
		}
		fileManageDao.deleteAll(files);
		return true;
	}
	
}
