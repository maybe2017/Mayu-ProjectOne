package com.mayu.practice.po.resourceInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lazycece
 * @date 2020/5/8
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NodeMark {
    private String name;
    private boolean pass;
    private List<LabelKeyword> keywords;
}
