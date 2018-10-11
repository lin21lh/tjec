//var orgcodePattern = /^\d{8}(X|[0-9])$/;
var orgcodePattern = /^([a-z]|[A-Z]|[0-9]){9}$/; //只能是字母或数字
var namePattern = /^[\u2E80-\uFE4Fa-zA-Z0-9（）]+$/;
var namePattern1 = /^[\u2E80-\uFE4F]+$/;
var phonePattern = /^(13[0-9]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/;
var addressPattern = /^[0-9a-zA-Z\u2E80-\uFE4F#]+$/;
var postPattern = /^[1-9]{1}(\d+){5}$/;
var pwdPattern =/^[a-zA-Z0-9|0-9|a-zA-Z]{6,12}$/;
var codePattern = /^[a-zA-Z0-9|0-9|a-zA-Z]+$/;
var lPattern1 = /^\d{3,4}?$/;
var lPattern2 = /^\d{7,8}?$/;
var idcardPattern = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)/;
var mailPattern = /^(([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-]))+/;
$(document).ready(function() {
	//下拉列表框默认选中
	$("#categoryName").val($("#category")[0].selectedOptions[0].text);
	$("#preferencesName").val($("#preferences")[0].selectedOptions[0].text);
	$("#ISCOMBOS").val("否");
    //社会资本名称填写检测
    $("#ORGNAME").focusin(function () {
        $("#TIPORGNAME").html("");
    }).focusout(function () {
            var str = $("#ORGNAME").val();
            if (str == "")
                $("#TIPORGNAME").html("请填写社会资本名称");
            else if (!namePattern.test(str)) {
                $("#TIPORGNAME").html("请正确填写社会资本名称");
            } else if(str.length>50){
            	 $("#TIPORGNAME").html("社会资本名称最多为50字");
            }else if(str != ""||str != null){
            	$.post("VerifyUniqueness.do", {
            		type:1,
					orgname :str
				}, function(result) {
					if(result.success==false){
						$("#TIPORGNAME").html(result.title);
					}else{
						$("#TIPORGNAME").html("");
						$("#NAME").val($("#ORGNAME").val().replace(/[&\|\\\*^%$#@\-]/g,""));
					}
				}, "json");
            	
            }else
                $("#TIPORGNAME").html("");
				$("#NAME").val($("#ORGNAME").val().replace(/[&\|\\\*^%$#@\-]/g,""));
        });
//组织机构代码
    $("#ORGCODE").focusin(function () {
        $("#TIPORGCODE").html("");
    }).focusout(function () {
            var str = $("#ORGCODE").val().toUpperCase().replace("-","");
            if (str == ""){
                $("#TIPORGCODE").html("请填写代理机构组织机构代码");
            }
            else if(!orgcodePattern.test(str)){
                $("#TIPORGCODE").html("请输入正确的9位字母或数字的组织机构代码");
            }else if(str != ""||str != null){
            	$.post("VerifyUniqueness.do", {
            		type:1,
					orgcode :str
				}, function(result) {
					if(result.success==false){
						$("#TIPORGCODE").html(result.title);
					}else{
						$("#TIPORGCODE").html("");
						$("#ORGCODES").val($("#ORGCODE").val().replace(/[&\|\\\*^%$#@\-]/g,""));
					}
				}, "json");
			   
            }else
                $("#TIPORGCODE").html("");
				$("#ORGCODES").val($("#ORGCODE").val().replace(/[&\|\\\*^%$#@\-]/g,""));
        });

    //是否是联合体
    $("#ISCOMBO").focusin(function () {
        $("#TIPISCOMBO").html("");
    }).focusout(function () {
            if($("#ISCOMBO").val()==""){
                $("#TIPISCOMBO").html("请填写是否是联合体");
            }else{
            	
            	$("#TIPISCOMBO").html("");
            	if($("#ISCOMBO").val()=="0"){
            		$("#ISCOMBOS").val("否");
                }else{
                	$("#ISCOMBOS").val("是");
                }
            	
            }
        });
    
    //所属行业
    $("#category").focusin(function () {
        $("#tcategory").html("");
    }).focusout(function () {
            if($("#category").val()==""){
                $("#tcategory").html("请填写所属行业");
            }else{
            	$("#tcategory").html("");
            	$("#categoryName").val($("#category")[0].selectedOptions[0].text);
            }
        });
  //投资偏好
    $("#preferences").focusin(function () {
        $("#tpreferences").html("");
    }).focusout(function () {
            if($("#preferences").val()==""){
                $("#tpreferences").html("请填写所属行业");
            }else{
            	$("#tpreferences").html("");
            	$("#preferencesName").val($("#preferences")[0].selectedOptions[0].text);
            }
        });
    
    //联系人
    $("#linkperson").focusin(function () {
    	$("#tiplinkperson").html("");
    }).focusout(function () {
    	if($("#linkperson").val()==""){
    		$("#tiplinkperson").html("请填写联系人");
    	}else if($("#linkperson").val().length>25){
       	 $("#tiplinkperson").html("联系人最多为25字");
        }else{
    		$("#tiplinkperson").html("");
    		$("#LINK_MAN").val($("#linkperson").val().replace(/[&\|\\\*^%$#@\-]/g,""));
    	}
    });
		
	//备注
    $("#REMARK").focusin(function () {
        $("#TIPREMARK").html("");
    }).focusout(function () {
            if($("#REMARK").val()==""){
                $("#TIPREMARK").html("");
            }else
                $("#TIPREMARK").html("");
				$("#REMARKS").val($("#REMARK").val().replace(/[&\|\\\*^%$#@\-]/g,""));
        });
		
    //手机格式检测
    $("#ORGTEL").focusin(function () {
        $("#CELLPHONESNOTE").html("");
    }).focusout(function () {
            if ($("#ORGTEL").val() == ""){
                $("#TIPORGTEL").html("请填写联系人手机");
            }
            else if (!phonePattern.test($("#ORGTEL").val())) {
                $("#TIPORGTEL").html("请正确填写联系人手机");
				
            }
            else{
				$("#TIPORGTEL").html("");
				$("#ORGTELS").val($("#ORGTEL").val().replace(/[&\|\\\*^%$#@\-]/g,""));
			}
                
        });


    //用户名填写检测
    $("#codes").focusin(function () {
        $("#codesnote").html("输入6~10位字母、数字或其组合");
    }).focusout(function () {
            if ($("#codes").val() == ""){
                $("#codesnote").html("请填写用户名");
            }else if($("#codes").val().length < 6){
                $("#codesnote").html("用户名长度不足6位");
            }else if($("#codes").val().length >10){
                $("#codesnote").html("用户名长度大于10位");
            }else if (!codePattern.test($("#codes").val())) {
                $("#codesnote").html("请输入6~10位字母、数字或其组合");
            }else if($("#codes").val() != ""||$("#codes").val() != null){
            	$.post("VerifyUniqueness.do", {
            		type:1,
					usercode :$("#nameflag").val()+$("#codes").val()
				}, function(result) {
					if(result.success==false){
						$("#codesnote").html(result.title);
					}else{
						  $("#codesnote").html("");
						   $("#NAMECODE").val($("#nameflag").val()+$("#codes").val().replace(/[&\|\\\*^%$#@\-]/g,""));
					}
				}, "json");
			 
            } else{
                $("#codesnote").html("");
                $("#NAMECODE").val($("#nameflag").val()+$("#codes").val().replace(/[&\|\\\*^%$#@\-]/g,""));

            }
        });
    //登录密码检验
    $("#pwds").focusin(function () {
        $("#pwdsnote").html("6~12位字母、数字或其组合");
    }).focusout(function () {
            if ($("#pwds").val() == ""){
                $("#pwdsnote").html("请填写密码");
            }else if($("#pwds").val().length < 6){
                $("#pwdsnote").html("密码长度不足6位");
            }else if($("#pwds").val().length >12){
                $("#pwdsnote").html("密码长度大于12位");
            }else if (!pwdPattern.test($("#pwds").val())) {
                $("#pwdsnote").html("请输入6~12位字母、数字或其组合");
            }else
                $("#pwdsnote").html("");
        });
    //确认密码检验
    $("#pwds2").focusin(function () {
        $("#pwds2note").html("");
    }).focusout(function () {
            if ($("#pwds2").val() == "")
                $("#pwds2note").html("请确认密码");
            else if ($("#pwds").val()!=$("#pwds2").val()) {
                $("#pwds2note").html("两次密码填写不正确");
            }
            else
                $("#pwds2note").html("");
            $("#PWD").val($("#pwds").val());
        });



    $("#wizard").scrollable({
        onSeek: function (event, i) { //切换tab样式
            $("#status li").removeClass("active").eq(i).addClass("active");
        },
        onBeforeSeek: function (event, i) { //验证表单
            if (gonext) {
                gonext = false;

                if(i==1){}

                if(i==2){
                    var cantgo=true;
                    var ids = "";
                    if($('#agent').attr('name')=="agent"){
                        $("span.NOTE").each(function () {
                            var v = $(this).text();
                            if(v!=""){
                                ids = $(this).attr('id');
                                //cantgo=true;
                                //alert("信息填写不完整或格式错误");
                                $('#'+ids).html("未填写或形式不正确");
                                cantgo=false;
                                //goornot(cantgo,ids);
                            }
                        });
                    }
                    return cantgo;
                }

                if (i==3) {
                    var cantgo1=true;
                    var map = {};
                    var kett = "";
                    if($('#agent1').attr('name')=="agent1"){
                        $("span.NOTES").each(function () {
                            var v = $(this).text();
                            if(v!=""){
                                kett = $(this).attr("id");
                                $('#'+kett).html("未填写或形式不正确");
                                cantgo1=false;
                            }
                        });
                    }
                    return cantgo1;
                }

                if (i==4) {
                    if($('#agent2').attr('name')=="agent2"){
                        if(flag==1)//flag用于防止重复提交
                        {
                            alert("数据正在提交，不需要重复点击！如果无法响应，请刷新重新注册");
                            isSave = false;
                        }else{
                        	$.messager.confirm("提交确认", "确认要提交信息吗？", function(r) {
                        		if (r) {
                        			$("#msgForm").form("submit",{
                            			url : "subRegistInfo.do",
                            			onSubmit : function() {
                            				
                            			},
                            			success : function(result) {
                            				result = $.parseJSON(result);
                            				if (result.success) {
                            					$.messager.alert('成功','注册成功！管理员审核通过后，可登陆系统！','info',function(){
                            						location.href='login.do';
                            					});  
                            					//alert("注册成功！管理员审核通过后，可登陆系统！");
                            				} else {
                            					$.messager.alert('失败',result.title,'error',function(){
                            						changeVerifyCode();
                            					}); 
                            					
                            				}
                            			}
                            		});
                        		}
                        	});
                        	
                            /*if (confirm("您确认要提交信息吗？")) {
                                $("#msgForm").form("submit",{
                        			url : "subRegistInfo.do",
                        			onSubmit : function() {
                        				
                        			},
                        			success : function(result) {
                        				result = $.parseJSON(result);
                        				if (result.success) {
                        					alert("注册成功！管理员审核通过后，可登陆系统！");
                        					location.href='login.do';
                        				
                        				} else {
                        					alert(result.title);
                        					changeVerifyCode();
                        				}
                        			}
                        		});
                                
                            }*/
                        }
                    }
                    return isSave;
                }
            }
        }});
    $("#sub").click(function () {
        var data = $("form").serialize();
        alert(data);
    });
});
function doqueryName(map) {
    dwrmng.queryWithoutUi(1, map, rtndata3);
}
var rtndata3 = function (data) {
    try {
        if (data != null) {
            if (data.rsltCode > 0) {
                $("#TIPNAMES").html("该代理机构已经注册");
            }
            else {
                $("#TIPNAMES").html("");
                $("#NAME").val($("#NAMES").val().replace(/[&\|\\\*^%$#@\-]/g,""));
                $("#ALL_NAME").val($("#NAMES").val().replace(/[&\|\\\*^%$#@\-]/g,""));
                $("#TIPALL_NAMES").html("");
            }
        } else {
            $("#TIPNAMES").html("数据库连接出错，请刷新页面重试或联系通管理员");
        }
    } catch (ex) {
    }
    finally {

    }

}

function doqueryOrgcode(map) {
    dwrmng.queryWithoutUi(1, map, rtnorgcode);
}
var rtnorgcode = function (data) {
    try {
        if (data != null) {
            if (data.rsltCode > 0) {
                $("#TIPORGCODES").html("该组织机构代码已经被注册");
            }
            else {
                $("#TIPORGCODES").html("");
                $("#ORGCODE").val($("#ORGCODES").val().toUpperCase().replace("-",""));
                $("#NEWCODE").val($("#ORGCODES").val().toUpperCase().replace("-",""));
            }
        } else {
            $("#TIPORGCODES").html("数据库连接出错，请刷新页面重试或联系通管理员");
        }
    } catch (ex) {
    }
    finally {

    }

}

function doqueryCode(map) {
    dwrmng.queryWithoutUi(1, map, rtndata4);
}
var rtndata4 = function (data) {
    try {
        if (data != null) {
            if (data.rsltCode> 0) {
                $("#codesnote").html("该用户名已经注册");
            }
            else {
                $("#codesnote").html("");
                $("#NAMECODE").val("A_"+$("#codes").val().replace(/[&\|\\\*^%$#@\-]/g,""));
            }
        } else {
            $("#codesnote").html("数据库连接出错，请刷新页面重试或联系系统管理员");
        }
    } catch (ex) {
    }
    finally {

    }

}

function dosure(map) {
    dwrmng.queryWithoutUi(4, map, rtnsure);
}
var rtnsure = function (data) {
    try {
        if (data != null) {
            if (data.rsltCode > 0) {
                alert("您的代理机构名称或者组织机构代码已经被注册过，如有疑问请尽快联系系统管理员！");
                flag = 0;
            }
            else {
                var mapsure= {};
                mapsure["tbnames"]  = "AGENT_USER_CODE";
                mapsure["keys"]  = "CODE";
                mapsure["values"]  =$("#NAMECODE").val();
                mapsure["otherwhere"]="";
                dosureagain(mapsure);
            }
        } else {
            $("#TIPORGCODES").html("数据库连接出错，请刷新页面重试或联系通管理员");
        }
    } catch (ex) {
    }
    finally {

    }

}
function dosureagain(map) {
    dwrmng.queryWithoutUi(4, map, rtnsure2);
}
var rtnsure2 = function (data) {
    try {
        if (data != null) {
            if (data.rsltCode > 0) {
                alert("您的用户名已经被注册过，如有疑问请尽快联系系统管理员！");
                flag = 0;
            }
            else {
                var map = {};
                var j = 0;
                var values="";
                var keys="";
                var types="";
                values=ip;
                keys = "AGENT_USER_CODE$^$IP_ADDR$^$1";
                types = "text";
                $("input.GODB").each(function () {
                    values=values+"$~$"+$(this).val();
                    keys =keys+"$~$"+ $(this).attr("name");
                    types =types+"$~$"+ $(this).attr("type");
                    j++;
                });
                map["choose"]="agt";
                map["values"] = values;
                map["keys"] = keys;
                map["types"] = types;
                doinsertAgent(map);
            }
        } else {
            $("#TIPORGCODES").html("数据库连接出错，请刷新页面重试或联系通管理员");
        }
    } catch (ex) {
    }
    finally {

    }

}

function doinsertAgent(map) {
    dwrmng.modifyWithoutUi(1, map, rtndata);
}
var rtndata = function (data) {
    try {
        if (data != null && data.rsltCode > 0) {
            //$("#INFONOTE").html("信息已录入");
            //跳转到成功页面
            window.location.href = "success_for_agent.jsp?name="+$("#NAMECODE").val();//跳转网址0
        } else {
            alert("注册未成功，建议重新注册");
            window.location.reload();
        }
    } catch (ex) {
        alert("系统异常，建议重新注册");
        window.location.reload();
    }
    finally {
        //afterdwr(); flag = 1;
    }
}

//更换验证码
function changeVerifyCode() {
	//2、如果用<img>实现，则修改<img src=url>的url
	//这里有一个小技巧，如果给url赋相同的值，浏览器不会重新发出请求，因此用js生成一个即时毫秒数做url中的参数
	t = new Date().getTime();
	$("#verifyCodeImg").attr("src","exclude/verfiyCode.do?t=" + t);
}