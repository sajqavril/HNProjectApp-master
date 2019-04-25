package com.hn.business.Data;


public class UserInfoEntity {
    //region 内部属性
    private int Id;
    private String UserCode;
    private String UserName;
    private String NickName;
    private String PwdCode;
    private String WXCode;
    private String MobilePhone;
    private String UserToken;

    public UserInfoEntity() {

    }

    public UserInfoEntity(int Id, String strUserCode, String strUserName, String strNickName, String strPwdCode) {
        this.Id = Id;
        this.UserCode = strUserCode;
        this.UserName = strUserName;
        this.NickName = strNickName;
        this.PwdCode = strPwdCode;
    }

    public boolean UserIsLogin() {
        //判断用户是否登陆
        boolean isLogin = true;
        if (this.UserCode == null || this.UserCode.isEmpty()) {
            isLogin = false;
        }
        return isLogin;
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String userCode) {
        UserCode = userCode;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
    //endregion

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getPwdCode() {
        return PwdCode;
    }

    public void setPwdCode(String pwdCode) {
        PwdCode = pwdCode;
    }

    public String getWXCode() {
        return WXCode;
    }

    public void setWXCode(String WXCode) {
        this.WXCode = WXCode;
    }

    public String getMobilePhone() {
        return MobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        MobilePhone = mobilePhone;
    }

    public String getUserToken() {
        return UserToken;
    }

    public void setUserToken(String userToken) {
        UserToken = userToken;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("current user: name = ");
        stringBuilder.append(this.UserName);
        stringBuilder.append(", pwd = ");
        stringBuilder.append(this.PwdCode);
        return stringBuilder.toString();
    }
}
