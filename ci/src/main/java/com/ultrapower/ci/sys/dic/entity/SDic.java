package com.ultrapower.ci.sys.dic.entity;
/**
 * 构建环境与构建类型的bean
 * @author pc
 *
 */
public class SDic {
	//主键 
    private String dicId;
    //类型编码
    private String dicCode;
    //类型名称
    private String dicName;
    //类型图标
    private String dicIcon;
    //序号
    private String dicSort;
    //父id
    private String parentDicId;

    public String getDicId() {
        return dicId;
    }

    public void setDicId(String dicId) {
        this.dicId = dicId == null ? null : dicId.trim();
    }

    public String getDicCode() {
        return dicCode;
    }

    public void setDicCode(String dicCode) {
        this.dicCode = dicCode == null ? null : dicCode.trim();
    }

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName == null ? null : dicName.trim();
    }

    public String getDicIcon() {
        return dicIcon;
    }

    public void setDicIcon(String dicIcon) {
        this.dicIcon = dicIcon == null ? null : dicIcon.trim();
    }

    public String getDicSort() {
        return dicSort;
    }

    public void setDicSort(String dicSort) {
        this.dicSort = dicSort == null ? null : dicSort.trim();
    }

    public String getParentDicId() {
        return parentDicId;
    }

    public void setParentDicId(String parentDicId) {
        this.parentDicId = parentDicId == null ? null : parentDicId.trim();
    }

	@Override
	public String toString() {
		return "SDic [dicId=" + dicId + ", dicCode=" + dicCode + ", dicName=" + dicName + ", dicIcon=" + dicIcon
				+ ", dicSort=" + dicSort + ", parentDicId=" + parentDicId + "]";
	}
    
}