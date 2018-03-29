package com.ultrapower.ci.control.buildManage.entity;

import java.io.Serializable;

public class Build implements Serializable{
	private static final long serialVersionUID = 1L;

	private String id;

    private String buildName;

    private String buildEnvironment;

    private String buildType;

    private String codeRepId;

    private String createUser;

    private String createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName == null ? null : buildName.trim();
    }

    public String getBuildEnvironment() {
        return buildEnvironment;
    }

    public void setBuildEnvironment(String buildEnvironment) {
        this.buildEnvironment = buildEnvironment == null ? null : buildEnvironment.trim();
    }

    public String getBuildType() {
        return buildType;
    }

    public void setBuildType(String buildType) {
        this.buildType = buildType == null ? null : buildType.trim();
    }

    public String getCodeRepId() {
        return codeRepId;
    }

    public void setCodeRepId(String codeRepId) {
        this.codeRepId = codeRepId == null ? null : codeRepId.trim();
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }
}