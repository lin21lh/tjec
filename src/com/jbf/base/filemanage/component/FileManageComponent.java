package com.jbf.base.filemanage.component;

import java.util.List;

import com.jbf.base.filemanage.po.SysFileManage;

public interface FileManageComponent {
	
	/**
	 * 更新附件的业务数据关联ID
	 * @Title: updateKeyid 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param keyid 业务数据ID
	 * @param @param itemids 附件ID集合
	 * @param @return 设定文件 
	 * @return boolean 返回类型 
	 * @throws
	 */
	public boolean updateKeyid(String keyid, List<Long> itemids);
	
	/**
	 * 查找附件
	 * @Title: findFiles 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param elementcode 附件关联业务编码
	 * @param @param keyid 业务数据ID
	 * @param @return 设定文件 
	 * @return List<SysFileManage> 返回类型 
	 * @throws
	 */
	public List<SysFileManage> findFiles(String elementcode, String keyid);
	
	/**
	 * 查找指定人上传的附件
	 * @Title: findFiles 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param elementcode 附件关联业务编码
	 * @param @param keyid 业务数据ID
	 * @param @param usercode 用户编码
	 * @param @return 设定文件 
	 * @return List<SysFileManage> 返回类型 
	 * @throws
	 */
	public List<SysFileManage> findFiles(String elementcode, String keyid, String usercode);
	
	/**
	 * 工作流某个节点查找附件
	 * @Title: findFilesForWf 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param elementcode 附件关联业务编码
	 * @param @param keyid 业务数据ID
	 * @param @param stepid 工作流节点ID
	 * @param @return 设定文件 
	 * @return List<SysFileManage> 返回类型 
	 * @throws
	 */
	public List<SysFileManage> findFilesForWf(String elementcode, String keyid, String stepid);

	/**
	 * 工作流某个节查找指定人上传的附件
	 * @Title: findFilesForWf 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param elementcode 附件关联业务编码
	 * @param @param keyid 业务数据ID
	 * @param @param stepid 工作流节点ID
	 * @param @param usercode 用户编码
	 * @param @return 设定文件 
	 * @return List<SysFileManage> 返回类型 
	 * @throws
	 */
	public List<SysFileManage> findFilesForWf(String elementcode, String keyid, String stepid, String usercode);
	
	/**
	 * 删除
	 * @Title: deleteFile 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param itemid 附件ID
	 * @param @return 设定文件 
	 * @return boolean 返回类型 
	 * @throws
	 */
	public boolean deleteFile(Long itemid);
	
	/**
	 * 删除
	 * @Title: deleteFiles 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param itemids 附件ID集合
	 * @param @return 设定文件 
	 * @return boolean 返回类型 
	 * @throws
	 */
	public boolean deleteFiles(List<Long> itemids);
	/**
	 * 根据项目id删除所有项目附件
	 * @Title: deleteFilesByKeyId 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param xmid
	 * @return 设定文件
	 */
	public boolean deleteFilesByKeyId(String xmid);
	
	/**
	 * 下载文件
	 * @Title: get 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param itemid
	 * @param @return 设定文件 
	 * @return SysFileManage 返回类型 
	 * @throws
	 */
	public SysFileManage get(Long itemid);
	
	/**
	 * 根据业务主键删除附件
	 * @Title: deleteFiles 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param keyId 业务主键
	 * @param @param elementCode 分类（不填，默认删除业务主键下的所有类）
	 * @param @return 设定文件 
	 * @return boolean 返回类型 
	 * @throws
	 */
	public boolean deleteFiles(String keyId,String elementCode);

	List<SysFileManage> findFilesByItemid(String itemid);
}
