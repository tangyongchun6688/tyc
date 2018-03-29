package com.ultrapower.ci.control.codeManage.entity;

import java.io.Serializable;

public class CodeRepositories implements Serializable{
	private static final long serialVersionUID = 1L;

	private String id;

    private String repName;

    private String repDescribe;

    private String repUrl;

    private String repVersion;

    private String repAccountNumber;

    private String repPassword;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getRepName() {
        return repName;
    }

    public void setRepName(String repName) {
        this.repName = repName == null ? null : repName.trim();
    }

    public String getRepDescribe() {
        return repDescribe;
    }

    public void setRepDescribe(String repDescribe) {
        this.repDescribe = repDescribe == null ? null : repDescribe.trim();
    }

    public String getRepUrl() {
        return repUrl;
    }

    public void setRepUrl(String repUrl) {
        this.repUrl = repUrl == null ? null : repUrl.trim();
    }

    public String getRepVersion() {
        return repVersion;
    }

    public void setRepVersion(String repVersion) {
        this.repVersion = repVersion == null ? null : repVersion.trim();
    }

    public String getRepAccountNumber() {
        return repAccountNumber;
    }

    public void setRepAccountNumber(String repAccountNumber) {
        this.repAccountNumber = repAccountNumber == null ? null : repAccountNumber.trim();
    }

    public String getRepPassword() {
        return repPassword;
    }

    public void setRepPassword(String repPassword) {
        this.repPassword = repPassword == null ? null : repPassword.trim();
    }
}