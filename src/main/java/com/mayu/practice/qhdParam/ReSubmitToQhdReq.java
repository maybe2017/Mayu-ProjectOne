package com.mayu.practice.qhdParam;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: 马瑜
 * @Date: 2023/3/3 16:43
 * @Description:
 */
@Data
public class ReSubmitToQhdReq extends ApiParams {

    private String taskId;
    @Override
    public boolean validate() {
        return !StringUtils.isBlank(taskId);
    }

}
