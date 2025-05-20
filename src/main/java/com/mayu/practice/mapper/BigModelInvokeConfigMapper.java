package com.mayu.practice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mayu.qw.po.bigModel.BigModelInvokeConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BigModelInvokeConfigMapper extends BaseMapper<BigModelInvokeConfig> {

    BigModelInvokeConfig selectConfigById(Integer id);

    BigModelInvokeConfig selectConfigByBusinessSceneCode(Integer businessSceneCode);
}
