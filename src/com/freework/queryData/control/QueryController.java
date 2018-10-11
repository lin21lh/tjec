package com.freework.queryData.control;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.freework.freedbm.util.GeneralTotalResult;
import com.freework.freedbm.util.PageTotalResult;
import com.freework.freedbm.util.TotalResult;
import com.freework.queryData.compileSQL.CompileSQL;
import com.freework.queryData.compileSQL.SqlAndParam;
import com.freework.queryData.servcie.DefaultQueryService;
import com.freework.queryData.servcie.QueryConfig;
import com.freework.queryData.servcie.QueryData;
import com.freework.queryData.servcie.QueryService;
import com.jbf.common.dao.MapDataDaoI;

@Scope("prototype")
@Controller
public class QueryController {
	Charset charset=Charset.forName("UTF-8");
	private SessionValue sessionValue=new SessionValueImpl();
	@Autowired(required=false)
	public void setSessionValue(SessionValue sessionValue) {
		this.sessionValue = sessionValue;
	}
	
	
	/**
	 *查询数据
	 * @param flow
	 * @return
	 */
	@ResponseBody()
	@RequestMapping("queryData/query.do")
	public List<Map> query(String type,HttpServletResponse response,HttpServletRequest request){
		//response.setHeader("Access-Control-Allow-Origin", "*");
		Set<String> paramNames=QueryData.getParams(type);
		Map<String,Object> params=null;
		if(paramNames!=null){
			 params=new HashMap<String,Object>(paramNames.size());
			for (String paramName : paramNames) {
				params.put(paramName,getParamValue(request, paramName));
			}
		}
		return QueryData.queryById(type, params);
	}
	
	@ResponseBody()
	@RequestMapping("queryData/queryPage.do")
	public PageTotalResult<Map> queryPage(String type,HttpServletResponse response,HttpServletRequest request,Integer page,Integer rp){
		//response.setHeader("Access-Control-Allow-Origin", "*");
		QueryConfig cfg=QueryData.getQueryConfig(type);
		QueryService exec=cfg.getExecute();
		if(exec instanceof DefaultQueryService){
			Map<String,Object> params=null;
			Set<String> paramNames=cfg.getParams();
			if(paramNames!=null){
				 params=new HashMap<String,Object>(paramNames.size());
				for (String paramName : paramNames) {
					params.put(paramName,getParamValue(request, paramName));
				}
			}
			rp=rp==null?20:rp;
			TotalResult<Map> total=((DefaultQueryService)exec).queryPage(cfg, params, Map.class, page==null||page==1||page==0?0:(page-1)*rp, rp);
			return  new PageTotalResult<Map>(total,page);	
		}else{
	      return	new PageTotalResult<Map>(new TotalResult<Map>(new LinkedList<Map>(),0),page);	
		}
	}
	
	@Autowired
	MapDataDaoI mapDataDao;
	
	/**
	 * app公共查询方法
	 * 2016-9-19
	 * @author wangyl
	 * @param type
	 * @param response
	 * @param request
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryData/generalQuery.do")
	public GeneralTotalResult<Map> generalQuery(String type,HttpServletResponse response,HttpServletRequest request,Integer pageNum,Integer pageSize){
		QueryConfig cfg=QueryData.getQueryConfig(type);
		QueryService exec=cfg.getExecute();
		try {
			if(exec instanceof DefaultQueryService){
				TotalResult<Map> total = new TotalResult<Map>();
					Map<String,Object> params=null;
					Set<String> paramNames=cfg.getParams();
					if(paramNames!=null){
						 params=new HashMap<String,Object>(paramNames.size());
						for (String paramName : paramNames) {
							params.put(paramName,getParamValue(request, paramName));
						}
					}
					pageNum = pageNum==null?1:pageNum;
					pageSize = pageSize==null?100:pageSize;//默认分页查询
//					rp=rp==null?10000:rp;//不分页时，rp没有结果，则查询前10000条
					CompileSQL compileSQL=cfg.getSql();
					SqlAndParam sqlParam = compileSQL.getSQL(params==null?new TreeMap():params);
					LinkedList<Map> list = null;
					list = (LinkedList<Map>) mapDataDao.query4app(sqlParam,pageNum,pageSize);
					return new GeneralTotalResult<Map>(new TotalResult<Map>(list,list.size()), true, "");
					
//					total = ((DefaultQueryService)exec).queryPageForGeneral(cfg, params, Map.class, page==null||page==1||page==0?0:(page-1)*rp, rp);
//					return  new GeneralTotalResult<Map>(total,true,"");	
			}else{
		      return  new GeneralTotalResult<Map>(new TotalResult<Map>(new LinkedList<Map>(),0),true,"");	
			}
		} catch (Exception e) {
			return  new GeneralTotalResult<Map>(new TotalResult<Map>(new LinkedList<Map>(),0),false,"获取数据失败！"+e.getMessage());	
		}
	}
	 public Object getParamValue(HttpServletRequest request,String paramsName){
		Object value = request.getParameter(paramsName);
		HttpSession session = request.getSession(false);
		if (value == null) {
			String name = null;
			String valuename = null;
			int index = paramsName.indexOf('.');
			if (index != -1) {
				name = paramsName.substring(0, index);
				valuename = paramsName.substring(index + 1);
			} else {
				name = paramsName;
			}
			
			Object tmpvalue = request.getAttribute(name);
			
			if(tmpvalue==null){
				tmpvalue=sessionValue.getValue(session, name);//SessionInfo.get(new Name(name));
			}
			if(tmpvalue==null)
				return null;
			
			try {
				value= valuename == null ? tmpvalue : PropertyUtils.getProperty(tmpvalue, valuename);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			
			
			
		}
		return value;
		
		
		
	}
	
	
	

}
