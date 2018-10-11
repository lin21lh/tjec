package com.wfzcx.aros.jzgl.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.alibaba.fastjson.JSONObject;
import com.jbf.base.filemanage.dao.SysFileManageDao;
import com.jbf.base.filemanage.po.SysFileManage;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.util.StringUtil;
import com.wfzcx.aros.print.dao.NoticeDao;
import com.wfzcx.aros.print.po.Noticebaseinfo;
import com.wfzcx.aros.util.GCC;
import com.wfzcx.aros.xzfy.dao.CasebaseinfoDao;
import com.wfzcx.aros.xzfy.po.Casebaseinfo;

import freemarker.template.Configuration;
import freemarker.template.Template;
@SuppressWarnings("unchecked")
@Scope("prototype")
@Service("com.wfzcx.aros.jzgl.service.CaseFileManageService")
public class CaseFileManageService {
	
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	NoticeDao noticeDao;
	@Autowired
	private SysFileManageDao sysFileManageDao;
	@Autowired
	CasebaseinfoDao casebaseinfoDao;
	
	public List<Map<String, Object>> queryCaseFileByCaseId(Map<String, Object> param){
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		String caseid = (String) param.get("caseid");
		// 通知书
		/*StringBuffer sql1 = new StringBuffer();
		sql1.append("select '1' AS filetype, b.noticeid, b.buildtime,");
		sql1.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='DOCTYPE' and a.status=0 and a.code = b.doctype) doctype ");
		sql1.append(",(select u.csaecode from B_CASEBASEINFO u where u.caseid = b.caseid) as csaecode,");
		sql1.append("(select a1.proname from pub_pronodebaseinfo a1, pub_noticeconfig a2 where a1.protype = a2.protype and a2.doctype = b.doctype GROUP BY proname) protype");
		sql1.append(" from B_NOTICEBASEINFO b");
		sql1.append(" where b.caseid='");
		sql1.append(caseid);
		sql1.append("'");*/
		
		StringBuffer sql1 = new StringBuffer();
		sql1.append("select '1' AS filetype, b.id noticeid, b.buildtime,");
		sql1.append("  b.noticename doctype ");
		sql1.append(",(select u.csaecode from B_CASEBASEINFO u where u.caseid = b.caseid) as csaecode,");
		sql1.append("b.protype");
		sql1.append(" from B_CASENOTICERELAINFO b");
		sql1.append(" where b.caseid='");
		sql1.append(caseid);
		sql1.append("'");
		sql1.append(" and b.tempid is not null");
		
		System.out.println(sql1.toString());
		result = mapDataDao.queryListBySQL(sql1.toString());
		
		//用户上传的附件
		StringBuffer sql2 = new StringBuffer();
		sql2.append("select '0' as filetype, t.savemode, t.filename as doctype, t.itemid as noticeid, (select u.csaecode from B_CASEBASEINFO u where u.caseid = t.keyid) as csaecode, t.createtime as buildtime ,");
		sql2.append("(select proname from pub_pronodebaseinfo where protype = t.remark GROUP BY proname) protype ");
		sql2.append("from SYS_FILEMANAGE t where keyid='");
		sql2.append(caseid);
		sql2.append("' and ELEMENTCODE='XZFY_JZGL_JZ'");
		result.addAll(mapDataDao.queryListBySQL(sql2.toString()));
		
		return result;
	}
	
	/**
	 * 根据通知书ID查询通知书信息
	 * @param caseid
	 * @return
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Noticebaseinfo queryNoticeInfo(String noticeid)
	{
		//return noticeDao.get(noticeid);
		Noticebaseinfo noticebaseinfo = new Noticebaseinfo();
		
		String sql = "select type,contents from b_Casenoticerelainfo where id ='" + noticeid + "'";
		
		List<JSONObject> jsonList = mapDataDao.queryListBySQL(sql.toString());
		if(jsonList != null && !jsonList.isEmpty()){
			for (JSONObject obj : jsonList) {
				
				noticebaseinfo.setDoctype(obj.getString("type"));
				noticebaseinfo.setContents(obj.getString("contents"));
				
			}
		}
		
		return noticebaseinfo;
	}

	/**
	 * 获取字典信息
	 * @param doctype
	 * @return
	 */
	public String getNoticeTypeName(String doctype) {
		String name = "";
		String sql = "select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='DOCTYPE' and a.status=0 and a.code ='" + doctype + "'";
		List<Map<String, Object>> list = mapDataDao.queryListBySQL(sql);
		if(!CollectionUtils.isEmpty(list)){
			name = (String) list.get(0).get("name");
		}
		return name;
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	public PaginationSupport queryCaseBaseinfoList(Map<String, Object> param) {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()) : PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		
		// 案件Id
		String caseid = StringUtil.stringConvert(param.get("caseid")); 
		// 案件编码
		String csaecode = StringUtil.stringConvert(param.get("csaecode")); 
		// 申请复议类型
		String casetype = StringUtil.stringConvert(param.get("casetype"));
		//申请人名称
		String appname = StringUtil.stringConvert(param.get("appname"));
		//被申请人名称
		String defname = StringUtil.stringConvert(param.get("defname"));

		sql.append("select c.caseid,c.csaecode,c.appname,c.defname,c.appdate, c.state, (SELECT NAME FROM SYS_YW_DICENUMITEM WHERE elementcode='B_CASEBASEINFO_CASETYPE' and code=c.casetype) as casetype from b_casebaseinfo c ");
		//状态为2 已处理
		sql.append(" where 1=1");// "c.opttype = '").append(GCC.PROBASEINFO_OPTTYPE_PROCESSED).append("'");
		
		//undefined
		if(StringUtils.isNotBlank(caseid) && !"undefined".equals(caseid)) {
			sql.append(" and c.caseid = '").append(caseid).append("'");
		}
		if(!"".equals(appname) && !"null".equals(appname)) {
			sql.append(" and c.appname like '%").append(appname).append("%'");
		}
		if(!"".equals(csaecode) && !"null".equals(csaecode)) {
			sql.append(" and c.csaecode like '%").append(csaecode).append("%'");
		}
		if(!"".equals(casetype) && !"null".equals(casetype)) {
			sql.append(" and c.casetype = '").append(casetype).append("'");
		}
		if(!"".equals(defname) && !"null".equals(defname)) {
			sql.append(" and c.defname like '%").append(defname).append("%'");
		}
		
    	return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}

	/**
	 * 保存上传的案件卷宗信息
	 * @param param
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveCaseFileKid(Map<String, Object> param)throws Exception {
		String caseid = (String) param.get("caseid");
		String timeLong = (String) param.get("timeLong");
		String protype = (String) param.get("protype");
		// 附件信息
        SysFileManage sysFileManage = new SysFileManage();
        sysFileManage.setKeyid(caseid + "_" + timeLong);
        sysFileManage.setElementcode("XZFY_JZGL_JZ");
        List<SysFileManage> sysFileManageList = sysFileManageDao.findByExample(sysFileManage);
        if(!CollectionUtils.isEmpty(sysFileManageList)){
        	for (SysFileManage sysFileManageTemp : sysFileManageList) {
        		sysFileManageTemp.setKeyid(caseid);
        		sysFileManageTemp.setRemark(protype);
        		sysFileManageDao.update(sysFileManageTemp);
        	}
        }
		
	}

	/**
	 * 流程下拉框选择流程
	 * @return
	 */
	public List<Map<String, Object>> queryWorkflowCheckBox() {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		String sql = " SELECT PRONAME,PROTYPE FROM PUB_PRONODEBASEINFO GROUP BY PRONAME,PROTYPE ORDER BY PROTYPE";
		result =  mapDataDao.queryListBySQL(sql);
		Map<String, Object> other = new HashMap<String, Object>();
		other.put("proname", "其他");
		other.put("protype","99");
		result.add(other);
		return result;
	}

	/**
	 * 案件归档置状态
	 * @param param
	 */
	public void palceOnFile(String caseid) throws Exception {
		Map<String, Object> value = new HashMap<String, Object>();
		value.put("caseid",caseid);
		value.put("state", GCC.RCASEBASEINFO_PSTATE_FINISH);
		mapDataDao.update(value, "B_CASEBASEINFO");
	}
	/**
	 * 判断案件是否结案
	 * @param caseid
	 * @return
	 */
	public boolean checkCaseIsClose(String caseid) {
		boolean result = false;
		String sql = "select state from b_casebaseinfo where caseid='" + caseid + "'";
		List<Map<String, String>> list = mapDataDao.queryListBySQL(sql);
		if(!CollectionUtils.isEmpty(list)){
			if(GCC.RCASEBASEINFO_PSTATE_NOARCHIVE.equals(list.get(0).get("state"))){
				result = true;
			}
		}
		return result;
	}

	/** 
	 * 通过附件Id获取附件信息
	 * @param noticeId
	 * @return
	 */
	public List<Map<String, Object>> getUploadFile(String noticeId) {
		String sql = "select t.* from sys_filemanage t where itemid='" + noticeId + "'";
		
		return mapDataDao.queryListBySQL(sql);
		
	}
	
	/**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 下载案件全部的卷宗文件
     * @param request
     * @param response
     * @param param
     * @throws Exception
     */
	public void downloadAllFile(HttpServletRequest request, HttpServletResponse response, Map<String, Object> param) throws Exception{
		String caseid = (String) param.get("caseid");
		
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		String casecode = casebaseinfo.getCsaecode();
		if(StringUtils.isEmpty(casecode)){
			casecode = casebaseinfo.getAppname() + "申请的案件";
		}
		List<Map<String, Object>> list = queryCaseFileByCaseId(param);
		String tempFileName = String.valueOf(Calendar.getInstance().getTimeInMillis());
		String filePath = request.getSession().getServletContext().getRealPath("\\") + "uploadfile\\temp\\" + tempFileName;
		if(!CollectionUtils.isEmpty(list)){
			// 创建临时文件夹放置所有的案件卷宗文件
			File tempFile = new File(filePath);
			ZipOutputStream zipOut = null;
			ServletOutputStream outputStream = null;
			try {
				if (!tempFile.exists()) {
					tempFile.mkdirs();
				}
				for (Map<String, Object> dataMap : list) {
					// 判断文件类型 1.系统生成的通知书文件 2.上传的附件文件
					String type = (String) dataMap.get("filetype");
					if("1".equals(type)){
						String noticeId = (String) dataMap.get("noticeid");
						Noticebaseinfo noticebaseinfo = queryNoticeInfo(noticeId);
						Map<String, String> docMap = new HashMap<String, String>();
						// 通知书类型名称
						String noticeTypeName = getNoticeTypeName(noticebaseinfo.getDoctype());
						String expFileName = filePath + "//" + noticeTypeName + ".doc";
						// 生成通知书doc版
						String content = noticebaseinfo.getContents();
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				        DocumentBuilder builder = factory.newDocumentBuilder();
				        Document doc = builder.parse(new InputSource(new StringReader(content)));   
				        Element root = doc.getDocumentElement();
				        NodeList nodes = root.getChildNodes();
						for (int i = 0; i<nodes.getLength(); i++ ) {
							Node item = nodes.item(i);
							docMap.put(item.getNodeName(), item.getTextContent());
						}
						
						Configuration configuration = new Configuration();
						configuration.setDefaultEncoding("utf-8");
						configuration.setClassForTemplateLoading(this.getClass(), "/com/wfzcx/aros/jzgl/template");
						String temFileName = noticeTypeName + "_模板";
						Template t = configuration.getTemplate(temFileName + ".ftl");
						FileOutputStream fileOut = null;
						OutputStreamWriter out = null;
						try{
							File noticefile = new File(expFileName);
							if(noticefile.exists()){
								String expFileName_real = filePath + "//" + noticeTypeName + String.valueOf(Calendar.getInstance().getTimeInMillis()) +".doc";
								noticefile = new File(expFileName_real);
							}
							noticefile.createNewFile();
							fileOut = new FileOutputStream(noticefile);
							out = new OutputStreamWriter(fileOut);
							t.process(docMap, out);
							out.flush();
						}
						catch (Exception e) {
							e.printStackTrace();
						}finally {
							try{
								if (null != out) {
									out.close();
								}
								if (null != fileOut) {
									fileOut.close();
								}
							}catch (Exception e) {
								e.printStackTrace();
								}
						}
					}else if("0".equals(type)){// 当文件类型为上传附件时
						String noticeId = String.valueOf(((BigDecimal)dataMap.get("noticeid")).longValue());

						List<Map<String, Object>> filelist = getUploadFile(noticeId);
						if(!CollectionUtils.isEmpty(filelist)){
							
							Map<String, Object> map = filelist.get(0);
							String oldPath = (String) map.get("filepath");
							String fileName = (String) map.get("filename");
							String newPath = filePath + "//" + fileName;
							InputStream input = null;
							FileOutputStream out = null;
							try { 
						           int bytesum = 0; 
						           int byteread = 0; 
						           File oldfile = new File(oldPath); 
						           File newFile = new File(newPath);
						           if(newFile.exists()){
						        	   newPath =  filePath + "//" + String.valueOf(Calendar.getInstance().getTimeInMillis()) + fileName;
						           }
						           if (oldfile.exists()) { //文件存在时 
						               input = new FileInputStream(oldPath); //读入原文件 
						               out = new FileOutputStream(newPath); 
						               byte[] buffer = new byte[1024]; 
						               while ( (byteread = input.read(buffer)) != -1) { 
						                   bytesum = bytesum + byteread; //字节数 文件大小 
						                   out.write(buffer, 0, byteread); 
						               } 
						               out.flush();
						           } 
						       } 
						       catch (Exception e) {
						           e.printStackTrace();
						       } finally {
						    	   try{
						    		   if (null != input) {
						    			   input.close();
						    		   }
										if (null != out) {
											out.close();
										}
									}catch (Exception e) {
										e.printStackTrace();
									}
						       }
						}
					}
				}
				// 文件压缩下载
				InputStream zininput = null;
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/octet-stream");
				String downFileName = URLEncoder.encode(casecode + "_案件卷宗文件汇总.zip", "UTF-8");
				response.setHeader("Content-Disposition", "attachment;filename=\"" + downFileName+"\"");
				outputStream = response.getOutputStream();
		        zipOut = new ZipOutputStream(outputStream);
		        if(tempFile.exists() && tempFile.isDirectory()){
		            File[] files = tempFile.listFiles();
		            for(int i = 0; i < files.length; ++i){
		            	try {
			                zininput = new FileInputStream(files[i]);
			                zipOut.putNextEntry(new ZipEntry(files[i].getName()));
			                int temp = 0;
			                while((temp = zininput.read()) != -1){
			                    zipOut.write(temp);
			                }
		            	} catch (Exception e) {
							e.printStackTrace();
						}finally {
							 try{	
								 	if (null != zininput) {
								 		zininput.close();
								 	}
								}catch (Exception e1) {
									e1.printStackTrace();
								}
						}
		            }
		            zipOut.flush();
		            outputStream.flush();
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				
				try {
					if (null != zipOut) {
						zipOut.close();
					}
					if(null != outputStream){
						outputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(tempFile.exists()){
					deleteDir(tempFile);
				}
			}
		}
		
	}
	/**
	 * 下载案件卷宗文件
	 * @param response
	 * @param param
	 */
	public void downCaseFile(HttpServletResponse response, Map<String, Object> param) {
		Writer out = null;
		try{
			String noticeId = (String) param.get("noticeid");
			Noticebaseinfo noticebaseinfo = queryNoticeInfo(noticeId);
			Map<String, String> docMap = new HashMap<String, String>();
			// 通知书类型名称
			String noticeTypeName = getNoticeTypeName(noticebaseinfo.getDoctype());
			String expFileName = URLEncoder.encode(noticeTypeName + ".doc", "UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + expFileName+"\"");
			// 生成通知书doc版
			String content = noticebaseinfo.getContents();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document doc = builder.parse(new InputSource(new StringReader(content)));   
	        Element root = doc.getDocumentElement();
	        NodeList nodes = root.getChildNodes();
			for (int i = 0; i<nodes.getLength(); i++ ) {
				Node item = nodes.item(i);
				docMap.put(item.getNodeName(), item.getTextContent());
			}
			
			Configuration configuration = new Configuration();
			configuration.setDefaultEncoding("utf-8");
			configuration.setClassForTemplateLoading(this.getClass(), "/com/wfzcx/aros/jzgl/template");
			String temFileName = noticeTypeName + "_模板";
			Template t = configuration.getTemplate(temFileName + ".ftl");
			out = new OutputStreamWriter(response.getOutputStream());
			t.process(docMap, out);
			out.flush();
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(null != out)
			{
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	
	}

	/**
	 * 获取案件状态
	 * @param caseid
	 * @return
	 */
	public String getCaseStateByCaseid(String caseid) {
		String  result = "";
		String sql = "select state from B_CASEBASEINFO where caseid='" + caseid + "'";
		List<Map<String, Object>> list = mapDataDao.queryListBySQL(sql);
		if(!CollectionUtils.isEmpty(list)){
			result = (String) list.get(0).get("state");
		}
		return result;
	}
}
