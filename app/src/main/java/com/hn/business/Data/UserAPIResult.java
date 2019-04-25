package com.hn.business.Data;

import org.json.JSONException;
import org.json.JSONObject;

public class UserAPIResult {

    private String dateStr;
    /// <summary>
    /// 用户身份验证Token
    /// </summary>
    private String userToken;

    /// <summary>
    /// 操作状态
    /// "success" - 操作成功
    /// "fail"    - 操作失败
    /// "cancel"  - 操作取消
    /// </summary>
    private String resultStatus;

    /// <summary>
    /// 操作内容说明
    /// </summary>
    private String resulMessage;


    public UserAPIResult() {

    }

    /**
     * 将Json字符转换成对象数据
     *
     * @param strJson
     */
    public UserAPIResult(String strJson) {
        try {
            JSONObject jsonObject = new JSONObject(strJson);
            String strDateStr = jsonObject.getString("dateStr");
            String strUserToken = jsonObject.getString("userToken");
            String strResultStatus = jsonObject.getString("resultStatus");
            String strResulMessage = jsonObject.getString("resulMessage");

            this.dateStr = strDateStr;
            this.userToken = strUserToken;
            this.resultStatus = strResultStatus;
            this.resulMessage = strResulMessage;

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 当前时间
     */
    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getResulMessage() {
        return resulMessage;
    }

    public void setResulMessage(String resulMessage) {
        this.resulMessage = resulMessage;
    }
}
