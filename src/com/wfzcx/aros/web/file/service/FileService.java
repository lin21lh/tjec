package com.wfzcx.aros.web.file.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.base.filemanage.component.FileManageComponent;
import com.jbf.base.filemanage.dao.SysFileManageDao;
import com.jbf.base.filemanage.po.SysFileManage;
import com.jbf.base.filemanage.service.impl.UpLoadFile.UpLoadFileDTO;
import com.jbf.common.TableNameConst;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.wfzcx.aros.util.GCC;

/**
 * @ClassName: FileService
 * @Description: 外网文件上传业务操作类
 * @author ybb
 * @date 2016年9月13日 下午2:37:19
 * @version V1.0
 */
@Scope("prototype")
@Service
public class FileService {
	
	 @Autowired
	 private ParamCfgComponent pcfg;
	 @Autowired
	 private MapDataDaoI mapDataDao;
	 @Autowired
	 private SysFileManageDao fileManageDao;
	 @Autowired
	 private FileManageComponent fileManageComponent;
	 
	 //上传文件路径
	 private String savePath = GCC.SYS_FILE_SAVEPATH;
	
	/**
	 * @Title: fileUpload
	 * @Description: 外网附件上传（不验证登录）-文件上传
	 * @author ybb
	 * @date 2016年9月13日 下午2:39:54
	 * @param upLoadFiles
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> fileUpload(List<UpLoadFileDTO> upLoadFiles) {
		
		//设置文件路径为系统参数配置的路径
		setSavePath(); 

        OutputStream out = null;
        SysFileManage fileManage = null;
        Map<String, Object> fileInfo = new HashMap<String, Object>();
        
        String savemode = pcfg.findGeneralParamValue("SYSTEM", "SAVEMODE");
        if(savemode == null){
        	savemode = "1"; //默认为存数据库
        }
        
        for (UpLoadFileDTO upLoadFile : upLoadFiles) {
			
			fileManage = new SysFileManage();
			fileManage.setElementcode(upLoadFile.getmap("elementcode"));
			fileManage.setTitle(upLoadFile.getmap("title"));
			fileManage.setFilename(upLoadFile.getFileName());
			fileManage.setOriginalfilename(getFileSuffix(upLoadFile.getFileName()));
			//fileManage.setSavemode(Integer.valueOf(upLoadFile.getmap("savemode")));
			fileManage.setSavemode(Integer.valueOf(savemode));
			fileManage.setFilesize(upLoadFile.getFilesize());
			
			String keyid = "0"; //0代表先传附件，然后更新业务字段 
			if (StringUtil.isNotBlank(upLoadFile.getmap("keyid"))) {
				keyid = upLoadFile.getmap("keyid");
			}
				
			fileManage.setKeyid(keyid);
			fileManage.setRemark(upLoadFile.getmap("remark"));
			
			if (StringUtil.isNotBlank(upLoadFile.getmap("stepid"))) {
				fileManage.setStepid(upLoadFile.getmap("stepid"));
			}
			
			fileManage.setUsercode(GCC.SYS_ADMINISTER);
			fileManage.setCreatetime(DateUtil.getCurrentDateTime());
			
			try {
				
				fileInfo.put("filename", upLoadFile.getFileName());
				
				Map fileMap = mapDataDao.add(BeanUtils.describe(fileManage), TableNameConst.SYS_FILEMANAGE);
				if (fileManage.getSavemode() == 0) { //保存磁盘
					
					File file = new File(savePath);
			        if (!file.exists()) {
			        	file.mkdirs();
			        }
			        
					file = new File(savePath + fileManage.getOriginalfilename());
					
					fileManage.setFilepath(file.getPath());
					
					fileMap.put("filepath", file.getPath());
					mapDataDao.update(fileMap, TableNameConst.SYS_FILEMANAGE);
					
					out = new FileOutputStream(file);
					byte buffer[] = new byte[4*1024];
					while((upLoadFile.getInput().read(buffer)) != -1){
						out.write(buffer);
					}
					out.flush();
					out.close();
					
				}else if (fileManage.getSavemode() == 1) {
					mapDataDao.setBlob(upLoadFile.getInput(), "content", "itemid", Integer.valueOf(fileMap.get("itemid").toString()), TableNameConst.SYS_FILEMANAGE);
				}
				
				fileInfo.put("itemid", fileMap.get("itemid"));
				
			} catch(Exception e){
				e.printStackTrace();
			}
		}
        
		return fileInfo;
	}
	
	/**
	 * @Title: setSavePath
	 * @Description: 获取上传文件路径（上传路径配置在系统参数表中）
	 * @author ybb
	 * @date 2016年9月13日 下午2:41:12
	 */
	public void setSavePath(){
		
		String path = StringUtil.stringConvert(pcfg.findGeneralParamValue("SYSTEM", "SYS_FILE_SAVEPATH"));
		if (!StringUtil.isBlank(path)) {
			this.savePath = path;
		}
	}
	
	/**
	 * @Title: getSysFileManage
	 * @Description: 根据文件ID获取文件信息
	 * @author ybb
	 * @date 2016年9月13日 下午3:18:32
	 * @param itemid
	 * @return
	 */
	public SysFileManage getSysFileManage(Long itemid) {
		
		return fileManageDao.get(itemid);
	}
	
	/**
	 * @Title: getFile
	 * @Description: 数据库文件下载
	 * @author ybb
	 * @date 2016年9月13日 下午3:20:39
	 * @param itemid
	 * @return
	 */
	public InputStream getFile(Integer itemid) {
		
		return mapDataDao.getBlob("content", "itemid", itemid, TableNameConst.SYS_FILEMANAGE);
	}
	
	/**
	 * @Title: deleteSysFileManage
	 * @Description: 文件删除
	 * @author ybb
	 * @date 2016年9月13日 下午3:28:02
	 * @param itemidstr
	 */
	public void deleteSysFileManage(String itemidstr) {
		
		String [] itemids = itemidstr.split(",");
		for (String itemid : itemids) {
			
			SysFileManage fm = fileManageDao.get(Long.valueOf(itemid));
			
			if (fm.getSavemode() == 0) {
				
				File file = new File(fm.getFilepath());
				if (file.exists() && file.isFile()) {
					file.delete();
				}
			}
			
			fileManageDao.delete(fm);
		}
	}
	
	/**
	 * @Title: queryFiles
	 * @Description: 查询文件列表
	 * @author ybb
	 * @date 2016年9月13日 下午4:50:09
	 * @param elementcode
	 * @param keyid
	 * @param stepid
	 * @param showFileLength
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryFiles(String elementcode, String keyid, String stepid,String showFileLength) {
		
		List<SysFileManage> files = fileManageComponent.findFilesForWf(elementcode, keyid, stepid);
		
		List<Map<String, Object>> newFiles = new ArrayList<Map<String,Object>>(files.size());
		Map<String, Object> fileInfoMap = null;
		
		for (SysFileManage file : files) {
			
			try {
				
				//是否显示缩略的字段
				if (!StringUtils.isBlank(showFileLength)) {
					
					int fileLength = Integer.parseInt(showFileLength);
					
					String fileName = file.getOriginalfilename();
					if(fileName.length() > fileLength){
						file.setOmitFileName(getsubstring(fileName, fileLength));
					}else{
						file.setOmitFileName(file.getOriginalfilename());
					}
				}
				
				fileInfoMap = BeanUtils.describe(file);
				
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}
			
			fileInfoMap.remove("content");
			
			newFiles.add(fileInfoMap);
		}
		
		return newFiles;
	}
	
	/**
	 * @Title: getsubstring
	 * @Description: 设定字符串长度
	 * @author ybb
	 * @date 2016年9月13日 下午4:48:35
	 * @param content
	 * @param sublength
	 * @return
	 * @throws Exception
	 */
	public String getsubstring(String content, int sublength) throws Exception{
		
		int length = content.getBytes().length;
		String substring = "";
		if (sublength >= length) {
			return content;
		} else {
			if (sublength < 0) {
				throw new Exception("字符串长度设定错误");
			} else {
				int i = 0;
				int j = 0;
				for (; i < length; i++) {
					if (content.substring(0, i + 1).getBytes().length >= sublength) {
						j = content.substring(0, i + 1).getBytes().length;
						break;
					}
				}
				substring = j > sublength ? content.substring(0, i) : content.substring(0, i + 1);
			}
		}
		
		return substring+"...";
	}
	
	/**
	 * @Title: getFileSuffix
	 * @Description: 获取文件后缀名
	 * @author ybb
	 * @date 2016年9月14日 下午2:42:41
	 * @param filename
	 * @return
	 */
	public static String getFileSuffix(String filename) {
		
		String currtime = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS");
		
		if (!StringUtils.isBlank(filename) && filename.contains(".")) {
			filename = currtime + "." + filename.substring(filename.lastIndexOf(".") + 1);
		}
		
		return filename;
	}
}
