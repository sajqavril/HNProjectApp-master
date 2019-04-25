package com.hn.business.Data;


import com.hn.gc.Date.ConvertDate;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

//计划进度实体类
public class PlanPercentageEntity {

    //region 内部属性
    private int Id;
    private int PlanId;
    private Date AddTime;
    private String UserCode;
    private int EndPercentage;
    private boolean IsEnd;
    private String Remark;
    //endregion

    //region 构造函数
    public PlanPercentageEntity() {

    }

    public PlanPercentageEntity(int iPlanId, String strUserCode, int iEndPercentage, boolean isEnd, String strRemark) {
        this.PlanId = iPlanId;
        this.UserCode = strUserCode;
        this.EndPercentage = iEndPercentage;
        this.IsEnd = isEnd;
        this.Remark = strRemark;
    }

    public PlanPercentageEntity(int id, int iPlanId, String mAddTime, String strUserCode, int iEndPercentage, boolean isEnd, String strRemark) {
        this.Id = id;
        this.PlanId = iPlanId;
        this.AddTime = ConvertDate.StrToSmallDate(mAddTime);
        this.UserCode = strUserCode;
        this.EndPercentage = iEndPercentage;
        this.IsEnd = isEnd;
        this.Remark = strRemark;
    }

    //region 公开属性方法
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getPlanId() {
        return PlanId;
    }

    public void setPlanId(int planId) {
        PlanId = planId;
    }

    public Date getAddTime() {
        return AddTime;
    }

    public void setAddTime(Date addTime) {
        AddTime = addTime;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String userCode) {
        UserCode = userCode;
    }

    public int getEndPercentage() {
        return EndPercentage;
    }

    public void setEndPercentage(int endPercentage) {
        EndPercentage = endPercentage;
    }

    public boolean isEnd() {
        return IsEnd;
    }

    public void setEnd(boolean end) {
        IsEnd = end;
    }
    //endregion

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String GetAddTimeString() {
        Format f = new SimpleDateFormat("yyyy年MM月dd日");
        StringBuilder strText = new StringBuilder();

        strText.append(f.format(AddTime));

        return strText.toString();
    }

    //endregion
}
