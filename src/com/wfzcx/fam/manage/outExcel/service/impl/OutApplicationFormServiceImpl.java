package com.wfzcx.fam.manage.outExcel.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.sys.dept.dao.SysDeptDao;
import com.jbf.sys.dept.po.SysDept;
import com.wfzcx.fam.manage.dao.FaAccountsDao;
import com.wfzcx.fam.manage.dao.FaApplicationsDao;
import com.wfzcx.fam.manage.dao.FavApplAccountDao;
import com.wfzcx.fam.manage.outExcel.service.OutApplicationFormService;
import com.wfzcx.fam.manage.po.FavApplAccount;

/**
 * 申请表导出service层
 * 
 * @ClassName: OutApplicationFormServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author LiuJunBo
 * @date 2015-5-27 上午08:54:41
 */
@Scope("prototype")
@Service
public class OutApplicationFormServiceImpl implements OutApplicationFormService {

	@Autowired
	FaApplicationsDao faApplicationsDao;

	@Autowired
	FaAccountsDao faAccountsDao;

	@Autowired
	FavApplAccountDao favApplAccountDao;
	
	@Autowired
	SysDeptDao sysDeptDao;
	
	@Override
	public HSSFWorkbook outRegisterAppicationForm(Map<String,Object> map) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		
		String path = map.get("path")==null?"":(String)map.get("path");
		String applicationIds = map.get("applicationIds")==null?"0":(String)map.get("applicationIds");
		String type = map.get("type")==null?"0":(String)map.get("type");//类型
		
		//FavApplAccount favApplAccount = favApplAccountDao.get(Integer.parseInt(applicationId));
		String hql = " from FavApplAccount where applicationId in("+applicationIds+")";
		List<FavApplAccount> list = (List<FavApplAccount>)favApplAccountDao.find(hql);
		
		//读取excel模板
		InputStream in = new FileInputStream(new File(path));
		
		//获得工作簿
		HSSFWorkbook work = new HSSFWorkbook(in); 
		
		HSSFSheet sheet = work.getSheetAt(0); 
		for(int i=0;i<list.size();i++){
			
			FavApplAccount favApplAccount = list.get(i);
			
			sheet = work.cloneSheet(0);
			if("3".equals(type)){
				work.setSheetName(i+1, (i+1)+"-"+favApplAccount.getOldAccountName());
			}else{
				work.setSheetName(i+1, (i+1)+"-"+favApplAccount.getAccountName());
			}
			
			if("1".equals(type) || "2".equals(type)){
				
				if("1".equals(type)){
					sheet.getRow(0).getCell(0).setCellValue("潍坊市预算单位银行账户开立申请表");
				}else{
					sheet.getRow(0).getCell(0).setCellValue("潍坊市预算单位银行账户变更申请表");
				}
				//单位全称
				sheet.getRow(2).getCell(1).setCellValue(favApplAccount.getBdgagencyname());
				
				//单位性质
				sheet.getRow(2).getCell(5).setCellValue(favApplAccount.getDeptNatureName());
				
				//主管部门
				String hqlStr = " from SysDept f where f.itemid = (select t.superitemid from SysDept t where t.code ='"+favApplAccount.getBdgagencycode()+"')";
				List<SysDept> sysDepts = (List<SysDept>)sysDeptDao.find(hqlStr);
				if(sysDepts.size()>0){
					sheet.getRow(2).getCell(9).setCellValue(sysDepts.get(0).getName());
				}else {
					sheet.getRow(2).getCell(9).setCellValue(favApplAccount.getBdgagencyname());
				}
				
				//单位地址
				sheet.getRow(3).getCell(1).setCellValue(favApplAccount.getDeptAddress());
				
				//理由
				sheet.getRow(3).getCell(5).setCellValue(favApplAccount.getApplyReason());
				
				//账户名称
				sheet.getRow(4).getCell(2).setCellValue(favApplAccount.getAccountName());
				
				//账户性质
				sheet.getRow(4).getCell(9).setCellValue(favApplAccount.getType02Name());
				
				//账号
				sheet.getRow(5).getCell(2).setCellValue(favApplAccount.getAccountNumber());
				
				//是否零余额账户
				sheet.getRow(5).getCell(9).setCellValue(favApplAccount.getIszeroName());
				
				//开户银行
				sheet.getRow(6).getCell(2).setCellValue(favApplAccount.getBankName());
				
				//备注
				sheet.getRow(6).getCell(9).setCellValue(favApplAccount.getRemark());
				
			}else{//注销
				
				sheet.getRow(0).getCell(0).setCellValue("潍坊市预算单位银行账户注销申请表");
				//单位全称
				sheet.getRow(2).getCell(1).setCellValue(favApplAccount.getBdgagencyname());
				
				//单位性质
				sheet.getRow(2).getCell(5).setCellValue(favApplAccount.getDeptNatureName());
				
				//主管部门
				String hqlStr = " from SysDept f where f.itemid = (select t.superitemid from SysDept t where t.code ='"+favApplAccount.getBdgagencycode()+"')";
				List<SysDept> sysDepts = (List<SysDept>)sysDeptDao.find(hqlStr);
				if(sysDepts.size()>0){
					sheet.getRow(2).getCell(9).setCellValue(sysDepts.get(0).getName());
				}else {
					sheet.getRow(2).getCell(9).setCellValue(favApplAccount.getBdgagencyname());
				}
				
				//单位地址
				sheet.getRow(3).getCell(1).setCellValue(favApplAccount.getDeptAddress());
				
				//理由
				sheet.getRow(3).getCell(5).setCellValue(favApplAccount.getApplyReason());
				
				//账户名称
				sheet.getRow(4).getCell(2).setCellValue(favApplAccount.getOldAccountName());
				
				//账户性质
				sheet.getRow(4).getCell(9).setCellValue(favApplAccount.getOldType02Name());
				
				//账号
				sheet.getRow(5).getCell(2).setCellValue(favApplAccount.getOldAccountNumber());
				
				//是否零余额账户
				sheet.getRow(5).getCell(9).setCellValue(favApplAccount.getOldIszeroName());
				
				//开户银行
				sheet.getRow(6).getCell(2).setCellValue(favApplAccount.getBankName());
				
				//备注
				sheet.getRow(6).getCell(9).setCellValue(favApplAccount.getRemark());
			}
		}
		
		//删除模板表
		work.removeSheetAt(0);
		return work;
	}

	@Override
	public HSSFWorkbook outRecordAppicationForm(Map<String, Object> map)
			throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub

		String path = map.get("path") == null ? "" : (String) map.get("path");
		String applicationIds = map.get("applicationIds") == null ? "0"
				: (String) map.get("applicationIds");
		String type = map.get("type") == null ? "0" : (String) map.get("type");// 类型

		// FavApplAccount favApplAccount =
		// favApplAccountDao.get(Integer.parseInt(applicationId));

		// 读取excel模板
		InputStream in = new FileInputStream(new File(path));

		// 获得工作簿
		HSSFWorkbook work = new HSSFWorkbook(in);


		HSSFSheet sheet = work.getSheetAt(0);
		// 修改标题
		sheet.getRow(0).getCell(0).setCellValue("潍坊市预算单位银行账户备案申请表");

		DetachedCriteria dc = DetachedCriteria.forClass(FavApplAccount.class);

		if (!applicationIds.equals("")) {
			// 将字符串数组转换为整形数组
			String[] applicationIdss = applicationIds.split(",");
			Integer[] intArr = new Integer[applicationIdss.length];
			for (int i = 0; i < applicationIdss.length; i++) {
				intArr[i] = Integer.parseInt(applicationIdss[i]);
			}

			dc.add(Property.forName("applicationId").in(intArr));
			dc.addOrder(Order.asc("bdgagencycode"));

			List<FavApplAccount> favs = (List<FavApplAccount>) favApplAccountDao.findByCriteria(dc);

			for (int i = 0; i < favs.size(); i++) {

				FavApplAccount favApplAccount = favs.get(i);

				// 户号
				sheet.getRow(i + 3).getCell(0).setCellValue(i+1);

				// 备案类型
				sheet.getRow(i + 3).getCell(1).setCellValue(favApplAccount.getTypeName());
				
			
				
				//主管部门
				String hqlStr = " from SysDept f where f.itemid = (select t.superitemid from SysDept t where t.code ='"+favApplAccount.getBdgagencycode()+"')";
				List<SysDept> sysDepts = (List<SysDept>)sysDeptDao.find(hqlStr);
				if(sysDepts.size()>0){
					//主管部门编码
					sheet.getRow(i + 3).getCell(2).setCellValue(sysDepts.get(0).getCode());
					sheet.getRow(i + 3).getCell(3).setCellValue(sysDepts.get(0).getName());
				}else {
					//主管部门编码
					sheet.getRow(i + 3).getCell(2).setCellValue(sysDepts.get(0).getCode());
					sheet.getRow(i + 3).getCell(3).setCellValue(sysDepts.get(0).getName());
				}
				
				//预算编码
				sheet.getRow(i + 3).getCell(4).setCellValue(favApplAccount.getBdgagencycode());
				// 单位名称
				sheet.getRow(i + 3).getCell(5).setCellValue(favApplAccount.getBdgagencyname());
				
				
				if(favApplAccount.getType()!=3){
					
					// 银行账户名称
					sheet.getRow(i + 3).getCell(6).setCellValue(favApplAccount.getAccountName());

					// 开户行全称
					sheet.getRow(i + 3).getCell(7).setCellValue(favApplAccount.getBankName());

					// 银行账号
					sheet.getRow(i + 3).getCell(8).setCellValue(favApplAccount.getAccountNumber());

					// 账户性质
					sheet.getRow(i + 3).getCell(9).setCellValue(favApplAccount.getType02Name());

					
					
					// 日期
					sheet.getRow(i + 3).getCell(10).setCellValue(favApplAccount.getOpenTime());

					// 核算内容
					sheet.getRow(i + 3).getCell(11).setCellValue(favApplAccount.getAccountContent());
				}
				
				// 银行账户名称（原）
				sheet.getRow(i + 3).getCell(12).setCellValue(favApplAccount.getOldAccountName());

				// 开户行全称（原）
				sheet.getRow(i + 3).getCell(13).setCellValue(favApplAccount.getOldBankName());

				// 银行账号
				sheet.getRow(i + 3).getCell(14).setCellValue(favApplAccount.getOldAccountNumber());

				// 账户性质
				sheet.getRow(i + 3).getCell(15).setCellValue(favApplAccount.getOldType02Name());
				
				// 经办人
				sheet.getRow(i + 3).getCell(16).setCellValue(favApplAccount.getResponsiblePerson());

				// 联系电话
				sheet.getRow(i + 3).getCell(17).setCellValue(favApplAccount.getApplPhonenumber());

				// 单位地址
				sheet.getRow(i + 3).getCell(18).setCellValue(favApplAccount.getDeptAddress());
				
				// 是否零余额账户
				sheet.getRow(i + 3).getCell(19).setCellValue(favApplAccount.getIszeroName());

			}
		}

		return work;
	}

}
