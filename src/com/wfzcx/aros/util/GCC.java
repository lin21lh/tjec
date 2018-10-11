package com.wfzcx.aros.util;


/**
 * @ClassName: GCC
 * @Description: 行政复议静态变量 GlobalConstCode
 * @author ybb
 * @date 2016年8月11日 下午1:32:11
 * @version V1.0
 */
public class GCC {

	/** ******************系统常量表    start ************************** */
	/**
	 * 系统常量   上传文件路径   E:/uploadFiles/
	 */
	public static String SYS_FILE_SAVEPATH = "E:/uploadFiles/";
	
	/**
	 * 系统常量   管理员用户   admin
	 */
	public static String SYS_ADMINISTER = "admin";
	
	/**
	 * 系统常量  是  0
	 */
	public static String SYS_WHETHER_NO = "0";
	/**
	 * 系统常量  否  1
	 */
	public static String SYS_WHETHER_YES = "1";
	
	/** ******************系统常量表  start ************************** */
	
	/** ******************流程过程表  流程类型  start ************************** */
	/**
	 * 流程过程表      流程类型       行政复议审批    01
	 */
	public static String PROBASEINFO_PROTYPE_XZFYAUDIT = "01";
	/**
	 * 流程过程表      流程类型       行政复议中止    02
	 */
	public static String PROBASEINFO_PROTYPE_XZFYSUSPEND = "02";
	/**
	 * 流程过程表      流程类型       行政复议恢复    03
	 */
	public static String PROBASEINFO_PROTYPE_XZFYRECOVER = "03";
	/**
	 * 流程过程表      流程类型       行政复议终止    04
	 */
	public static String PROBASEINFO_PROTYPE_XZFYEND = "04";
	/**
	 * 流程过程表      流程类型       行政复议和解    05
	 */
	public static String PROBASEINFO_PROTYPE_XZFYCOMPROMISE = "05";
	/**
	 * 流程过程表      流程类型       行政复议延期   06
	 */
	public static String PROBASEINFO_PROTYPE_XZFYDELAY = "06";
	/**
	 * 流程过程表      流程类型       行政复议调解   07
	 */
	public static String PROBASEINFO_PROTYPE_XZFYMEDIATION = "07";
	/**
	 * 流程过程表      流程类型       行政复议程序    08
	 */
	public static String PROBASEINFO_PROTYPE_XZFYPROGRAM = "08";
	/**
	 * 流程过程表      流程类型       行政复议撤回    09
	 */
	public static String PROBASEINFO_PROTYPE_XZFYCANCEL = "09";
	/**
	 * 流程过程表      流程类型       被行政复议  11
	 */
	public static String PROBASEINFO_PROTYPE_RCASEBASEINFO = "11";
	/**
	 * 流程过程表      流程类型       行政应诉   20
	 */
	public static String PROBASEINFO_PROTYPE_ADMLITBASEINFO = "20";
	
	/** ******************流程过程表  流程类型     end  ************************** */
	
	/** ******************流程过程表  处理标志  start ************************** */
	/**
	 * 流程过程表      处理标志       已提交    0
	 */
	public static String PROBASEINFO_OPTTYPE_SUBMITTED = "0";
	/**
	 * 流程过程表      处理标志       已接收    1
	 */
	public static String PROBASEINFO_OPTTYPE_ACCEPTED = "1";
	/**
	 * 流程过程表     处理标志       已处理    2
	 */
	public static String PROBASEINFO_OPTTYPE_PROCESSED = "2";
	/**
	 * 流程过程表      处理标志      已办    3
	 */
	public static String PROBASEINFO_OPTTYPE_END = "3";
	/**
	 * 流程过程表      处理标志      不予办理    4
	 */
	public static String PROBASEINFO_OPTTYPE_REFUSE = "4";
	/**
	 * 流程过程表      处理标志      转发   5
	 */
	public static String PROBASEINFO_OPTTYPE_FORWARDING = "5";
	/**
	 * 流程过程表      处理标志      已退回    9
	 */
	public static String PROBASEINFO_OPTTYPE_RETURNED = "9";
	
	/** ******************流程过程表  处理标志     end  ************************** */
	
	/** ******************流程过程表  处理结果  start ************************** */
	/**
	 * 流程过程表      处理结果       受理    01
	 */
	public static String PROBASEINFO_OPTTYPE_ACCEPT = "01";
	/**
	 * 流程过程表      处理结果      不予受理     02
	 */
	public static String PROBASEINFO_OPTTYPE_NOTACCEPT = "02";
	/**
	 * 流程过程表     处理结果       转送    03
	 */
	public static String PROBASEINFO_OPTTYPE_FORWARD = "03";
	/**
	 * 流程过程表     处理结果      补正    04
	 */
	public static String PROBASEINFO_OPTTYPE_EOFFSET = "04";
	
	/** ******************流程过程表  处理结果     end  ************************** */
	
	/** ******************流程过程表  审理结果  start ************************** */
	/**
	 * 流程过程表      审理结果       维持    01
	 */
	public static String PROBASEINFO_OPTTYPE_MAINTAIN = "01";
	/**
	 * 流程过程表      审理结果      变更     02
	 */
	public static String PROBASEINFO_OPTTYPE_CHANGE = "02";
	/**
	 * 流程过程表     审理结果       撤销    03
	 */
	public static String PROBASEINFO_OPTTYPE_CANCEL = "03";
	/**
	 * 流程过程表     审理结果      确认违法    04
	 */
	public static String PROBASEINFO_OPTTYPE_CONFIRM = "04";
	/**
	 * 流程过程表     审理结果      责令履行    05
	 */
	public static String PROBASEINFO_OPTTYPE_PERFORM = "05";
	
	/** ******************流程过程表  审理结果     end  ************************** */
	
	/** ******************流程节点配置信息表  流程名称  start ************************** */
	/**
	 * 流程节点配置信息表     流程名称       行政复议审批
	 */
	public static String PRONODEBASEINFO_PRONAME_XZFYAUDIT = "行政复议审批";
	/**
	 * 流程节点配置信息表      流程名称       行政复议中止    
	 */
	public static String PRONODEBASEINFO_PRONAME_XZFYSUSPEND = "行政复议中止";
	/**
	 * 流程节点配置信息表      流程名称       行政复议恢复    
	 */
	public static String PRONODEBASEINFO_PRONAME_XZFYRECOVER = "行政复议恢复";
	/**
	 * 流程节点配置信息表      流程名称       行政复议终止    
	 */
	public static String PRONODEBASEINFO_PRONAME_XZFYEND = "行政复议终止";
	/**
	 * 流程节点配置信息表      流程名称       行政复议和解    
	 */
	public static String PRONODEBASEINFO_PRONAME_XZFYCOMPROMISE = "行政复议和解 ";
	
	/** ******************流程节点配置信息表  流程名称     end  ************************** */
	
	/** ******************流程节点配置信息表  节点编号  start ************************** */
	
	/**
	 * 结束节点
	 */
	public static int END_NODEID = 99;
	/**
	 * 流程节点配置信息表(行政复议)    节点编号     10-申请笔录
	 */
	public static int PRONODEBASEINFO_NODEID_APPLYRECORD = 10;
	/**
	 * 流程节点配置信息表(行政复议)    节点编号     20-接收申请材料     
	 */
	public static int PRONODEBASEINFO_NODEID_REQRECEIVE = 20;
	/**
	 * 流程节点配置信息表(行政复议)    节点编号     30-案件登记     
	 */
	public static int PRONODEBASEINFO_NODEID_REQ = 30;
	/**
	 * 流程节点配置信息表(行政复议)    节点编号      40-受理承办人审批   
	 */
	public static int PRONODEBASEINFO_NODEID_ACCEPT = 40;
	/**
	 * 流程节点配置信息表(行政复议)    节点编号      50-受理科室负责人审批 
	 */
	public static int PRONODEBASEINFO_NODEID_ACCSECTION = 50;
	/**
	 * 流程节点配置信息表(行政复议)    节点编号      60-受理机构负责人审批
	 */
	public static int PRONODEBASEINFO_NODEID_ACCORGAN = 60;
	/**
	 * 流程节点配置信息表(行政复议)    节点编号      70-受理机关负责人审批
	 */
	public static int PRONODEBASEINFO_NODEID_ACCOFFICE = 70;
	/**
	 * 流程节点配置信息表 (行政复议)   节点编号      80-受理决定
	 */
	public static int PRONODEBASEINFO_NODEID_ACCEND = 80;
	/**
	 * 流程节点配置信息表 (行政复议)   节点编号      90-转送登记
	 */
	public static int PRONODEBASEINFO_NODEID_TRANSFERREGISTRATION = 90;
	/**
	 * 流程节点配置信息表 (行政复议)   节点编号      100-指定承办人
	 */
	public static int PRONODEBASEINFO_NODEID_APPOINTSLUNDERTAKER = 100;
	/**
	 * 流程节点配置信息表 (行政复议)   节点编号      110-审理承办人审核
	 */
	public static int PRONODEBASEINFO_NODEID_SLUNDERTAKERHEAR = 110;
	/**
	 * 流程节点配置信息表 (行政复议)   节点编号      120-庭审
	 */
	public static int PRONODEBASEINFO_NODEID_TRIAL= 120;
	/**
	 * 流程节点配置信息表 (行政复议)   节点编号      130-委员审议
	 */
	public static int PRONODEBASEINFO_NODEID_REVIEW = 130;
	/**
	 * 流程节点配置信息表 (行政复议)   节点编号      140-集体讨论
	 */
	public static int PRONODEBASEINFO_NODEID_DISCUSS = 140;
	/**
	 * 流程节点配置信息表 (行政复议)   节点编号      150- 科室负责人审核受理
	 */
	public static int PRONODEBASEINFO_NODEID_SECTIONREMARKHEAR = 150;
	/**
	 * 流程节点配置信息表 (行政复议)   节点编号      160-机构负责人审核受理
	 */
	public static int PRONODEBASEINFO_NODEID_ORGANREMARKHEAR = 160;
	/**
	 * 流程节点配置信息表 (行政复议)   节点编号      170-机关负责人审核受理
	 */
	public static int PRONODEBASEINFO_NODEID_OFFICEREMARKHEAR = 170;
	/**
	 * 流程节点配置信息表 (行政复议)   节点编号      180-审理决定
	 */
	public static int PRONODEBASEINFO_NODEID_DECISION = 180;
	/**
	 * 流程节点配置信息表 (行政复议)   节点编号      190-行政复议决定书
	 */
	public static int PRONODEBASEINFO_NODEID_DECISIONDOC= 190;
	/**
	 * 流程节点配置信息表 (行政复议)   节点编号      200-文书送达
	 */
	public static int PRONODEBASEINFO_NODEID_DOCSEND= 200;
	/**
	 * 流程节点配置信息表 (行政复议)   节点编号      210-廉政回访
	 */
	public static int PRONODEBASEINFO_NODEID_GOVERNMENBACK= 210;
	/**
	 * 流程节点配置信息表 (行政复议)   节点编号      220-结案归档
	 */
	public static int PRONODEBASEINFO_NODEID_PLACEONFILE= 220;
	/**
	 * 流程节点配置信息表(行政复议)    节点编号      8-受理文书制作
	 */
	public static int PRONODEBASEINFO_NODEID_ACCDOC = 8;
	/**
	 * 流程节点配置信息表(行政复议)    节点编号      9-受理文书送达
	 */
	public static int PRONODEBASEINFO_NODEID_ACCDOCSEND = 9;
	/**
	 * 流程节点配置信息表(行政复议)    节点编号      10-受理回访单
	 */
	public static int PRONODEBASEINFO_NODEID_ACCBACKDOC = 10;
	/**
	 * 流程节点配置信息表(行政复议)     节点编号     11-接收答复材料
	 */
	public static int PRONODEBASEINFO_NODEID_RESRECEIVE = 11;
	/**
	 * 流程节点配置信息表(行政复议)    节点编号    12-审理承办人审批 
	 */
	public static int PRONODEBASEINFO_NODEID_HEAR = 12;
	/**
	 * 流程节点配置信息表(行政复议)    节点编号      13-审理科室负责人审批
	 */
	public static int PRONODEBASEINFO_NODEID_HEARSECTION = 13;
	/**
	 * 流程节点配置信息表(行政复议)    节点编号      14-审理机构负责人审批 
	 */
	public static int PRONODEBASEINFO_NODEID_HEARORGAN = 14;
	/**
	 * 流程节点配置信息表(行政复议)    节点编号      15-审理机关负责人审批
	 */
	public static int PRONODEBASEINFO_NODEID_HEAROFFICE = 15;
	/**
	 * 流程节点配置信息表(行政复议)     节点编号     16-审理决定 
	 */
	public static int PRONODEBASEINFO_NODEID_HEAREND = 16;
	/**
	 * 流程节点配置信息表(行政复议)    节点编号      17-审理文书制作
	 */
	public static int PRONODEBASEINFO_NODEID_HEARDOC = 17;
	/**
	 * 流程节点配置信息表(行政复议)    节点编号     18-审理文书送达
	 */
	public static int PRONODEBASEINFO_NODEID_HEARDOCSEND = 18;
	/**
	 * 流程节点配置信息表(行政复议)    节点编号      19-审理回访单
	 */
	public static int PRONODEBASEINFO_NODEID_HEARBACKDOC = 19;
	/**
	 * 流程节点配置信息表(行政复议)    节点编号      20-审理备考表
	 */
	public static int PRONODEBASEINFO_NODEID_HEARBACKUP = 20;
	/**
	 * 流程节点配置信息表(行政复议)    节点编号      99-结束
	 */
	public static int PRONODEBASEINFO_NODEID_END = 99;
	
	/**
	 * 流程节点配置信息表(被复议案件)	节点编号     1-案件维护     
	 */
	public static int PRONODEBASEINFO_NODEID_MAINTAIN = 1;
	/**
	 * 流程节点配置信息表(被复议案件)	节点编号      2-分案审批    
	 */
	public static int PRONODEBASEINFO_NODEID_DIVISION = 2;
	/**
	 * 流程节点配置信息表(被复议案件)	节点编号     3-案件处理   
	 */
	public static int PRONODEBASEINFO_NODEID_DISPOSE = 3;
	/**
	 * 流程节点配置信息表(被复议案件)	节点编号     4-案件审批    
	 */
	public static int PRONODEBASEINFO_NODEID_AUDIT = 4;
	/**
	 * 流程节点配置信息表(被复议案件)	节点编号     5-复议结果  
	 */
	public static int PRONODEBASEINFO_NODEID_RESULT = 5;
	/**
	 * 流程节点配置信息表(被复议案件)	节点编号     6-结束  
	 */
	public static int RCASEBASEINFO_NODEID_END = 6;
	
	
	/**
	 * 流程节点配置信息表(应诉案件)	节点编号     1-案件维护     
	 */
	public static int ADMLITBASEINFO_NODEID_MAINTAIN = 1;
	/**
	 * 流程节点配置信息表(应诉案件)	节点编号      2-分案审批    
	 */
	public static int ADMLITBASEINFO_NODEID_DIVISION = 2;
	/**
	 * 流程节点配置信息表(应诉案件)	节点编号     3-案件处理   
	 */
	public static int ADMLITBASEINFO_NODEID_DISPOSE = 3;
	/**
	 * 流程节点配置信息表(应诉案件)	节点编号     4-案件审批    
	 */
	public static int ADMLITBASEINFO_NODEID_AUDIT = 4;
	/**
	 * 流程节点配置信息表(应诉案件)	节点编号     5-案件结果  
	 */
	public static int ADMLITBASEINFO_NODEID_RESULT = 5;
	/**
	 * 流程节点配置信息表(应诉案件)	节点编号     6-结束  
	 */
	public static int ADMLITBASEINFO_NODEID_END = 6;
	/** ******************流程节点配置信息表  节点编号     end  ************************** */
	
	/**
	 * 流程节点配置信息表(应诉案件)	节点编号     10-收案登记
	 */
	public static int RESPBASEINFO_NODEID_MAINTAIN = 10;
	/**
	 * 流程节点配置信息表(应诉案件)	节点编号      20-案件审查    
	 */
	public static int RESPBASEINFO_NODEID_DIVISION = 20;
	/**
	 * 流程节点配置信息表(应诉案件)	节点编号     30-出庭应诉 
	 */
	public static int RESPBASEINFO_NODEID_DISPOSE = 30;
	/**
	 * 流程节点配置信息表(应诉案件)	节点编号     40-立案归档
	 */
	public static int RESPBASEINFO_NODEID_FILING = 40;
	
	
	/** ******************流程节点配置信息表  节点名称  start ************************** */
	/**
	 * 流程节点配置信息表(行政复议) 节点编号     1-申请     
	 */
	public static String PRONODEBASEINFO_NODENAME_REQ = "申请";
	/**
	 * 流程节点配置信息表(行政复议)     节点编号      2-受理    
	 */
	public static String PRONODEBASEINFO_NODENAME_ACCEPT = "受理";
	/**
	 * 流程节点配置信息表 (行政复议)    节点编号      3-受理决定
	 */
	public static String PRONODEBASEINFO_NODENAME_ACCEND = "受理决定";
	/**
	 * 流程节点配置信息表(行政复议)     节点编号     3-审理   
	 */
	public static String PRONODEBASEINFO_NODENAME_HEAR = "审理";
	/**
	 * 流程节点配置信息表(行政复议)     节点编号     4-审理决定    
	 */
	public static String PRONODEBASEINFO_NODENAME_HEAREND = "审理决定";
	
	/** ******************流程节点配置信息表  节点名称     end  ************************** */
	
	/** ******************流程过程表  审核结果  start ************************** */
	/**
	 * 流程过程表      审核结果       同意    1
	 */
	public static String PROBASEINFO_OPTTYPE_PASS = "1";
	/**
	 * 流程过程表      审核结果      不同意    2
	 */
	public static String PROBASEINFO_OPTTYPE_NOPASS = "2";
	
	/** ******************流程过程表  审核结果     end  ************************** */
	
	/** ******************行政复议通知书基本信息表  通知书类型  start ************************** */
	/**
	 * 行政复议通知书基本信息表     通知书类型       行政复议申请受理通知书    1
	 */
	public static String NOTICEBASEINFO_DOCTYPE_1 = "1";
	/**
	 * 行政复议通知书基本信息表     通知书类型       行政复议答复通知书    2
	 */
	public static String NOTICEBASEINFO_DOCTYPE_2 = "2";
	/**
	 * 行政复议通知书基本信息表     通知书类型       被申请人答复书    3
	 */
	public static String NOTICEBASEINFO_DOCTYPE_3 = "3";
	/**
	 * 行政复议通知书基本信息表     通知书类型       不予受理行政复议申请决定书    4
	 */
	public static String NOTICEBASEINFO_DOCTYPE_4 = "4";
	/**
	 * 行政复议通知书基本信息表     通知书类型       中止行政复议通知书    5
	 */
	public static String NOTICEBASEINFO_DOCTYPE_5 = "5";
	/**
	 * 行政复议通知书基本信息表     通知书类型       恢复审理通知书    6
	 */
	public static String NOTICEBASEINFO_DOCTYPE_6 = "6";
	/**
	 * 行政复议通知书基本信息表     通知书类型       行政复议终止决定书    7
	 */
	public static String NOTICEBASEINFO_DOCTYPE_7 = "7";
	/**
	 * 行政复议通知书基本信息表     通知书类型       行政复议和解书    8
	 */
	public static String NOTICEBASEINFO_DOCTYPE_8 = "8";
	
	/** ******************行政复议通知书基本信息表  通知书类型     end  ************************** */
	
	/** ******************行政复议案件基本信息表  数据来源  start ************************** */
	/**
	 * 行政复议案件基本信息表      数据来源       网上申报    1
	 */
	public static String CASEBASEINFO_PROTYPE_NETWORK = "1";
	/**
	 * 行政复议案件基本信息表      数据来源       纸质申报    2
	 */
	public static String CASEBASEINFO_PROTYPE_DIRECTORDEP = "2";
	/**
	 * 行政复议案件基本信息表      数据来源       手机申报    3
	 */
	public static String CASEBASEINFO_PROTYPE_APPREQ = "3";
	
	//结案未归档 
	public static String CASEBASEINFO_STATE_NODEID = "220";
	//结案已归档
	public static String CASEBASEINFO_STATUS_NODEID = "99";
	
	/** ******************行政复议案件基本信息表  数据来源     end  ************************** */
	
	/** ******************被复议案件基本信息表   状态  start ************************** */
	/**
	 * 被复议案件基本信息表   状态       收案未立案    01
	 */
	public static String RCASEBASEINFO_STATE_REQ = "01";
	/**
	 * 被复议案件基本信息表   状态       立案未结案    02
	 */
	public static String RCASEBASEINFO_STATE_NOCLOSURE = "02";
	/**
	 * 被复议案件基本信息表   状态       结案未归档    03
	 */
	public static String RCASEBASEINFO_PSTATE_NOARCHIVE = "03";
	/**
	 * 被复议案件基本信息表   状态       结案已归档    04
	 */
	public static String RCASEBASEINFO_PSTATE_FINISH = "04";
	
	/** ******************被复议案件基本信息表     状态     end  ************************** */
	
	/** ******************各环节时间规则信息    状态     start  ************************** */
	/**
	 * 未发布    0
	 */
	public static String RULE_STATUS_NOPUBLISH = "0";
	
	/**
	 * 已发布
	 */
	public static String RULE_STATUS_PUBLISH = "1";
	
	/** ******************各环节时间规则信息     状态     end  ************************** */
	
	/** ******************消息基本信息表     消息类型     start  ************************** */
	/**
	 * 流程
	 */
	public static String MESSAGETYPE_PROCEDURE = "01";
	/**
	 * 逾期
	 */
	public static String MESSAGETYPE_OVERDUE = "02";
	/**
	 * 意见反馈
	 */
	public static String MESSAGETYPE_FEEDBACK = "03";
	/**
	 * 网上答疑
	 */
	public static String MESSAGETYPE_NETWORKQUESTION = "04";
	
	/** ******************消息基本信息表     消息类型     end  ************************** */
	
	/** ******************应诉案件基本信息表    审理阶段     start  ************************** */
	/**
	 * 应诉案件基本信息表    审理阶段   一审   01 
	 */
	public static String ADMLITBASEINFO_STAGE_FIRSTINSTANCE = "01";
	/**
	 * 应诉案件基本信息表    审理阶段   二审   02
	 */
	public static String ADMLITBASEINFO_STAGE_SECONDINSTANCE = "02";
	/**
	 * 应诉案件基本信息表    审理阶段    三审   03
	 */
	public static String ADMLITBASEINFO_STAGE_FINALINSTANCE = "03";
	/** ******************应诉案件基本信息表    审理阶段     end  ************************** */
	
	/** ******************应诉案件基本信息表    当事人类型     start  ************************** */
	/**
	 * 应诉案件基本信息表    当事人类型   单独被告   01 
	 */
	public static String ADMLITBASEINFO_PARTYTYPE_ALONE = "01";
	/**
	 * 应诉案件基本信息表    当事人类型   共同被告   02
	 */
	public static String ADMLITBASEINFO_PARTYTYPE_TOGETHER = "02";
	/** ******************应诉案件基本信息表    当事人类型     end  ************************** */
	
	/** ******************案件变更申请表    状态     start  ************************** */
	/**
	 * 案件变更申请表    状态   在办   1 
	 */
	public static String CASECHANGEREQ_STATE_TRANSIT = "1";
	/**
	 * 案件变更申请表    状态   已办   2
	 */
	public static String CASECHANGEREQ_STATE_COMPLETE = "2";
	/**
	 * 案件变更申请表    状态   暂停   3
	 */
	public static String CASECHANGEREQ_STATE_SUSPEND = "3";
	/**
	 * 案件变更申请表    状态   不予办理   4
	 */
	public static String CASECHANGEREQ_STATE_NOPASS = "4";
	/**
	 * 案件变更申请表    状态  用户撤回   5
	 */
	public static String CASECHANGEREQ_STATE_CANCEL = "5";
	/** ******************案件变更申请表    状态     end  ************************** */
	
	/** ******************案件变更申请表    流程节点编号     start  ************************** */
	/**
	 * 案件变更申请表     流程节点编号   承办人   10 
	 */
	public static int CASECHANGEREQ_NODEID_ONEAUDIT = 10;
	/**
	 * 案件变更申请表     流程节点编号   科室负责人   20
	 */
	public static int CASECHANGEREQ_NODEID_TWOAUDIT = 20;
	/**
	 * 案件变更申请表     流程节点编号   机构负责人   30
	 */
	public static int CASECHANGEREQ_NODEID_THREEAUDIT = 30;
	/**
	 * 案件变更申请表   流程节点编号   机关负责人   40
	 */
	public static int CASECHANGEREQ_NODEID_FOURAUDIT = 40;
	/**
	 * 案件变更申请表     流程节点编号  处理决定   50
	 */
	public static int CASECHANGEREQ_NODEID_DECISION = 50;
	/** ******************案件变更申请表   流程节点编号    end  ************************** */
	
	/** ******************行政复议案件基本信息表    流程节点编号     start  ************************** */
	/**
	 * 行政复议案件基本信息表   案件程序   简易   1 
	 */
	public static String CASEBASEINFO_CASESORT_SIMPLE = "1";
	/**
	 * 行政复议案件基本信息表    案件程序   普通   2
	 */
	public static String CASEBASEINFO_CASESORT_COMMON = "2";
	/** ******************行政复议案件基本信息表   流程节点编号    end  ************************** */
}
