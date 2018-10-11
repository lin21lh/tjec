package com.jbf.sys.paramCfg.component;

/**
 * 参数配置接口
 * @ClassName: ParamCfgComponent 
 * @Description: TODO(查询系统配置参数) 
 * @author MaQingShuang
 * @date 2015年5月13日 下午2:28:22
 */
public interface ParamCfgComponent {
	
	/**
	 * 查找通用配置参数
	 * @param scenecode 子系统或模块编码
	 * @param paramcode 参数编码
	 * @return
	 */
	public String findGeneralParamValue(String scenecode, String paramcode);

	/**
	 * 查找配置参数 
	 * @param admivcode 地区码
	 * @param scenecode 子系统或模块编码
	 * @param paramcode 参数编码
	 * @return
	 */
	public String findParamValue(String admivcode, String scenecode, String paramcode);
}
