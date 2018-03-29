package com.ultrapower.ci.control.autoTestManage.entity;

import java.io.Serializable;

public class AutoTest implements Serializable{
	private static final long serialVersionUID = 1L;

	private String id;

    private String testName;

    private String createUser;

    private String createTime;

    private String requestUrl;

    private String resposeUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName == null ? null : testName.trim();
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

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl == null ? null : requestUrl.trim();
    }

    public String getResposeUrl() {
        return resposeUrl;
    }

    public void setResposeUrl(String resposeUrl) {
        this.resposeUrl = resposeUrl == null ? null : resposeUrl.trim();
    }
}