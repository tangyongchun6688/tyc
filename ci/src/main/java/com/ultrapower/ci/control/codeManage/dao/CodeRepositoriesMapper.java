package com.ultrapower.ci.control.codeManage.dao;

import java.util.List;
import java.util.Map;

import com.ultrapower.ci.control.codeManage.entity.CodeRepositories;

public interface CodeRepositoriesMapper {
    int deleteByPrimaryKey(String id);

    int insert(CodeRepositories record);

    int insertSelective(CodeRepositories record);

    CodeRepositories selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CodeRepositories record);

    int updateByPrimaryKey(CodeRepositories record);

    List<Map<String, Object>> queryCodeBaseList();
}