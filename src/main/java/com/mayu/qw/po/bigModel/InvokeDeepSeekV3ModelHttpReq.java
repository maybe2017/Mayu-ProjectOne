package com.mayu.qw.po.bigModel;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class InvokeDeepSeekV3ModelHttpReq {
    private Integer top_k = 4;
    /**
     * 指定要调用的对话生成模型ID
     */
    private String model = "xdeepseekv3";
    /**
     * 表示对话上下文的消息列表，支持多轮对话交互。
     * 其中，role 用于标识消息发送方（例如 user 表示用户、assistant 表示模型回复）
     * content 则为实际文本内容。
     */
    private List<InvokeBigModelMsgItem> messages;
    /**
     * 指定是否采用流式响应模式。
     * 若设置为 true，系统将逐步返回生成的回复内容；否则，将一次性返回完整响应
     */
    private Boolean stream = Boolean.FALSE;
    /**
     * 核采样阈值。用于决定结果随机性，取值越高随机性越强即相同的问题得到的不同答案的可能性越高
     */
    private float temperature = 0.7f;
    /**
     * 限制生成回复的最大 token 数量
     */
    private int max_tokens = 4096;
//    /**
//     * 	通过传递 lora_id 加载特定的LoRA模型
//     */
//    private HashMap<String,String> extra_headers = this.putExtra_headers();
//    /**
//     * 关闭联网检索，不展示检索信源信息
//     */
//    private HashMap<String,Boolean> extra_body = this.putExtra_body();
//    /**
//     * 是否在响应中包含API调用统计信息等附加数据
//     */
//    private HashMap<String,Boolean> stream_options = this.putStream_options();
//
//
//    public HashMap<String, String> putExtra_headers() {
//        HashMap<String, String> map = new HashMap<>();
//        map.put("lora_id","0");
//        return map;
//    }
//
//    public HashMap<String, Boolean> putExtra_body() {
//        HashMap<String, Boolean> map = new HashMap<>();
//        map.put("search_disable",Boolean.TRUE);
//        map.put("show_ref_label",Boolean.FALSE);
//        return map;
//    }
//
//    public HashMap<String, Boolean> putStream_options() {
//        HashMap<String, Boolean> map = new HashMap<>();
//        map.put("include_usage",Boolean.TRUE);
//        return map;
//    }
}

