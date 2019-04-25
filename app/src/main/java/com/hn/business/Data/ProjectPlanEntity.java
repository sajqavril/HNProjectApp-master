package com.hn.business.Data;


import com.hn.gc.Date.ConvertDate;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

///工作计划实体类
public class ProjectPlanEntity implements Serializable {
    //region 内部属性
    private int PlanId;
    private String PlanTitle;
    private String PlanContent;
    private Date BeginDate;
    private Date EndDate;
    private boolean IsEnd;
    private int EndPercentage;
    private int TimeUsePercentage;
    private String UserCode;
    private Date AddTime;
    private Date EndTime;

    public ProjectPlanEntity() {
    }

    public ProjectPlanEntity(int mId, String mTitle, String mContent, String mBdate
            , String mEdate, boolean isEnd, int iEndPercentage, int iTimeUsePercentage
            , String strUserCode, String mAddTime
    ) {
        this.PlanId = mId;
        this.PlanTitle = mTitle;
        this.PlanContent = mContent;

        this.BeginDate = ConvertDate.StrToSmallDate(mBdate);
        this.EndDate = ConvertDate.StrToSmallDate(mEdate);
        this.IsEnd = isEnd;
        this.EndPercentage = iEndPercentage;
        this.TimeUsePercentage = iTimeUsePercentage;
        this.UserCode = strUserCode;
        this.AddTime = ConvertDate.StrToSmallDate(mAddTime);
    }

    public int getPlanId() {
        return PlanId;
    }

    public void setPlanId(int planId) {
        PlanId = planId;
    }

    public String getPlanTitle() {
        return PlanTitle;
    }

    public void setPlanTitle(String planTitle) {
        PlanTitle = planTitle;
    }

    public String getPlanContent() {
        return PlanContent;
    }

    public void setPlanContent(String planContent) {
        PlanContent = planContent;
    }

    public Date getBeginDate() {
        return BeginDate;
    }

    public void setBeginDate(Date beginDate) {
        BeginDate = beginDate;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date endDate) {
        EndDate = endDate;
    }


    //endregion

    /**
     * 显示日期组合
     *
     * @return 开始结束日期的组合展示
     */
    public String GetPublishDate() {
        Format f = new SimpleDateFormat("yyyy年MM月dd日");
        StringBuilder strText = new StringBuilder();

        strText.append("计划开始：" + f.format(BeginDate) + "  计划完成：" + f.format(EndDate));

        return strText.toString();
    }

    public String GetBeginDateString() {
        Format f = new SimpleDateFormat("yyyy-MM-dd");
        return f.format(BeginDate).toString();
    }

    public String GetEndDateString() {
        Format f = new SimpleDateFormat("yyyy-MM-dd");
        return f.format(EndDate).toString();
    }

    public boolean isEnd() {
        return IsEnd;
    }

    public void setEnd(boolean end) {
        IsEnd = end;
    }

    public int getEndPercentage() {
        return EndPercentage;
    }

    public void setEndPercentage(int endPercentage) {
        EndPercentage = endPercentage;
    }

    public int getTimeUsePercentage() {
        return TimeUsePercentage;
    }

    public void setTimeUsePercentage(int timeUsePercentage) {
        TimeUsePercentage = timeUsePercentage;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String userCode) {
        UserCode = userCode;
    }

    public Date getAddTime() {
        return AddTime;
    }

    public void setAddTime(Date addTime) {
        AddTime = addTime;
    }

    public Date getEndTime() {
        return EndTime;
    }

    public void setEndTime(Date endTime) {
        EndTime = endTime;
    }
}
