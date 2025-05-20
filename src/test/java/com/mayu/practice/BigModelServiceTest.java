package com.mayu.practice;

import com.mayu.NeptuneApplication;
import com.mayu.qw.po.bigModel.BigModelInvokeSceneEnum;
import com.mayu.qw.service.CheckMircoTaskService;
import com.mayu.qw.service.bigModel.BigModelService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Author: 马瑜
 * @Date: 2025/3/14 11:13
 * @Description: 客户业务大类 大模型识别
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NeptuneApplication.class)
public class BigModelServiceTest {

    @Resource
    private BigModelService bigModelService;

    @Test
    public void testInvoke() {
        String prompt = "<对话记录>\\n客户：要是 办理一年，只有10元抵扣券是吧客户：那不需要了，谢谢！\\n<判定要求>\\n请你基于<业务分类定义>中的标准，判断<对话记录>中，客户发言咨询内容的业务分类\\n1.若客户发言中仅 回复或者发送纯数字、符号等无意义信息，你需要将“业务分类”判定为“无”；\\n2.若客户发言中未提及任何运营商业务，你需要将“业务分类”判定为“无”；\\n3.若客户发言中识别存在 多个业务分类，你只需要记录一条对话中客户最主要最频繁谈及的业务；\\n4.你需要按照<回复格式>输出判定结果，不要发散回复其他内容。\\n\\n<业务分类定义>\\n1.流量类：涉及套 餐流量升降档以及欢孝包、居家流量包等各类流量包、加油包业务。\\n2.宽带类：涉及宽带新增、续费、提速、路由器网络检修、移机、测速等宽带相关业务，此外还包含宽带电视、 电视VIP、监控以及其他智能家居产品相关业务。\\n3.权益类：涉及随心系列、权益超市会员、惠生活权益包等vip增值服务和特权业务。\\n4.终端：涉及领手机、儿童手表、手环、电 视机、投影仪、电动车等实体终端产品业务。\\n5.合约：涉及话费补贴、话费返还、话费合约、折扣购机、手机活动、终端合约等各类合约性质业务。\\n6.语音类：涉及语音模组升降 档、亲情网、虚拟网等语音通话时长办理的业务。\\n7.5G：涉及5G网络相关的套餐、流量包等业务。\\n8.新业务：涉及咪咕视频爱看版、和彩云、视频彩铃、机伶、5g消息、云游戏、 超级sim卡的业务。\\n\\n<回复格式>\\n业务分类：无/流量类/宽带类/权益类/终端/合约/语音类/5G/新业务";
        String conversationId = "100051165";
        bigModelService.detectInvoke(BigModelInvokeSceneEnum.C_USER_TYPE, conversationId, prompt);
    }
}
