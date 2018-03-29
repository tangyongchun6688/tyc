package com.ultrapower.ci.control.deployManage.entity;

import java.io.Serializable;

public class Deploy implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String id;

    private String deployName;

    private String createUser;

    private String createTime;

    private String mouldId;

    private String nodeId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getDeployName() {
        return deployName;
    }

    public void setDeployName(String deployName) {
        this.deployName = deployName == null ? null : deployName.trim();
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

    public String getMouldId() {
        return mouldId;
    }

    public void setMouldId(String mouldId) {
        this.mouldId = mouldId == null ? null : mouldId.trim();
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId == null ? null : nodeId.trim();
    }
}