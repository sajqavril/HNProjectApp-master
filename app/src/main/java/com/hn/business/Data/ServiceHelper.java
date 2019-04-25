package com.hn.business.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.hn.business.Tools.CrashHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import as.hn.com.hnprojectapp.FileEntity;

/*远程服务调用类
 * */
public class ServiceHelper {

    private String nameSpace = "http://tempuri.org/";
    private String endPoint = "https://ihomeapptest3.nw-sc.com:8034/DataService.asmx";
    private Context context;

    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String toHexString(byte[] b) { //String to byte
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public String mmd5(String s) {
        try { // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            return toHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    //region 计划操作方法
    public boolean AddPlanEntity(ProjectPlanEntity item, String strUserCode) {

        boolean isAdd = false;
        String methodName = "AddProjectPlan";
        String soapAction = nameSpace + "/" + methodName + "/";
        // 命名空间     String nameSpace = "http://tempuri.org/";
        // 调用的方法名称     String methodName = "HelloWorld";
        // EndPoint     String endPoint = "http://192.168.16.39:1215/WebService.asmx";
        // SOAP Action     String soapAction = "http://tempuri.org//HelloWorld/";
        // 指定WebService的命名空间和调用的方法名

        SoapObject rpc = new SoapObject(nameSpace, methodName);

        //string strUserCode,string strTitle,string strContent,string strBeginDate, string strEndDate
        // 设置需调用WebService接口需要传入的参数
        rpc.addProperty("strUserCode", strUserCode);
        rpc.addProperty("strTitle", item.getPlanTitle());
        rpc.addProperty("strContent", item.getPlanContent());
        rpc.addProperty("strBeginDate", item.GetBeginDateString());
        rpc.addProperty("strEndDate", item.GetEndDateString());

        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = rpc;

        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;
        (new MarshalBase64()).register(envelope);

        // 等价于envelope.bodyOut = rpc;
        envelope.setOutputSoapObject(rpc);
        HttpTransportSE transport = new HttpTransportSE(endPoint);
        transport.debug = true;
        try {

            // 调用WebService
            transport.call(soapAction, envelope);
            if (envelope.getResponse() != null) {
                //                System.out.println(envelope.getResponse());
                isAdd = Boolean.valueOf(envelope.getResponse().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isAdd;
    }

    public List<ProjectPlanEntity> GetUserProjectPlanList(String strUserCode) {
        List<ProjectPlanEntity> items = new ArrayList<>();

        String methodName = "GetUserProjectPlans";
        String soapAction = nameSpace + "/" + methodName + "/";

        SoapObject rpc = new SoapObject(nameSpace, methodName);

        // 设置需调用WebService接口需要传入的参数
        rpc.addProperty("strUserCode", strUserCode);

        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = rpc;

        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;
        (new MarshalBase64()).register(envelope);

        // 等价于envelope.bodyOut = rpc;
        envelope.setOutputSoapObject(rpc);
        HttpTransportSE transport = new HttpTransportSE(endPoint);
        transport.debug = true;
        try {

            // 调用WebService
            transport.call(soapAction, envelope);
            if (envelope.getResponse() != null) {
                //                System.out.println(envelope.getResponse());
                String strJsonList = String.valueOf(envelope.getResponse());

                //region 将Json数据转换成实体对象
                LoadJsonList(items, strJsonList);
                //endregion
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    private ProjectPlanEntity JsonToPlanEntity(String strJson) {
        try {
            JSONObject jsonObject = new JSONObject(strJson);
            int iId = jsonObject.getInt("Id");
            String strTitle = jsonObject.getString("PlanTitle");
            String strContent = jsonObject.getString("PlanContent");
            String strBDate = jsonObject.getString("PlanBeginDate");
            String strEDate = jsonObject.getString("PlanEndDate");
            boolean isEnd = jsonObject.getBoolean("IsEnd");
            int iEndPercentage = jsonObject.getInt("EndPercentage");
            int iTimeUsePercentage = jsonObject.getInt("TimeUsePercentage");
            String strUserCode = jsonObject.getString("UserCode");
            String strAddTime = jsonObject.getString("AddTime");

            ProjectPlanEntity item = new ProjectPlanEntity(iId, strTitle, strContent, strBDate, strEDate, isEnd, iEndPercentage, iTimeUsePercentage, strUserCode, strAddTime);

            return item;

        } catch (JSONException ex) {
            //            ex.printStackTrace();
            return new ProjectPlanEntity();
        }
    }

    public List<ProjectPlanEntity> GetCurrentProjectPlanList(String strUserCode, String strUserPwd) {

        String methodName = "GetCurrentProjectPlans";

        return getProjectPlanEntities(strUserCode, strUserPwd, methodName);
    }

    @NonNull
    private List<ProjectPlanEntity> getProjectPlanEntities(String strUserCode, String strUserPwd, String methodName) {
        List<ProjectPlanEntity> items = new ArrayList<>();
        String soapAction = nameSpace + "/" + methodName + "/";

        SoapObject rpc = new SoapObject(nameSpace, methodName);

        // 设置需调用WebService接口需要传入的参数
        rpc.addProperty("strUserCode", strUserCode);
        rpc.addProperty("strPwdCode", strUserPwd);

        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = rpc;

        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;
        (new MarshalBase64()).register(envelope);

        // 等价于envelope.bodyOut = rpc;
        envelope.setOutputSoapObject(rpc);
        HttpTransportSE transport = new HttpTransportSE(endPoint);
        transport.debug = true;
        try {

            // 调用WebService
            transport.call(soapAction, envelope);
            if (envelope.getResponse() != null) {
                //                System.out.println(envelope.getResponse());
                String strJsonList = String.valueOf(envelope.getResponse());

                //region 将Json数据转换成实体对象
                LoadJsonList(items, strJsonList);
                //endregion
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    public List<ProjectPlanEntity> GetOvertimeProjectPlanList(String strUserCode, String strUserPwd) {

        String methodName = "GetOvertimeProjectPlans";
        return getProjectPlanEntities(strUserCode, strUserPwd, methodName);
    }

    public List<ProjectPlanEntity> GetEndProjectPlanList(String strUserCode, String strUserPwd) {
        String methodName = "GetEndProjectPlans";
        return getProjectPlanEntities(strUserCode, strUserPwd, methodName);
    }

    public ProjectPlanEntity GetProjectPlanEntity(Integer iPlanId) {
        ProjectPlanEntity item = new ProjectPlanEntity();

        String methodName = "GetProjectPlanEntity";
        String soapAction = nameSpace + "/" + methodName + "/";

        SoapObject rpc = new SoapObject(nameSpace, methodName);

        // 设置需调用WebService接口需要传入的参数
        rpc.addProperty("iPlanId", iPlanId);

        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = rpc;

        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;
        (new MarshalBase64()).register(envelope);

        // 等价于envelope.bodyOut = rpc;
        envelope.setOutputSoapObject(rpc);
        HttpTransportSE transport = new HttpTransportSE(endPoint);
        transport.debug = true;
        try {

            // 调用WebService
            transport.call(soapAction, envelope);
            if (envelope.getResponse() != null) {
                //                System.out.println(envelope.getResponse());
                String strJsonList = String.valueOf(envelope.getResponse());

                //region 将Json数据转换成实体对象
                item = JsonToPlanEntity(strJsonList);
                //endregion
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return item;
    }

    private void LoadJsonList(List<ProjectPlanEntity> items, String strJsonList) {
        try {
            JSONArray jsonArray = new JSONArray(strJsonList);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int iId = jsonObject.getInt("Id");
                String strTitle = jsonObject.getString("PlanTitle");
                String strContent = jsonObject.getString("PlanContent");
                String strBDate = jsonObject.getString("PlanBeginDate");
                String strEDate = jsonObject.getString("PlanEndDate");
                boolean isEnd = jsonObject.getBoolean("IsEnd");
                int iEndPercentage = jsonObject.getInt("EndPercentage");
                int iTimeUsePercentage = jsonObject.getInt("TimeUsePercentage");
                String strUserCode = jsonObject.getString("UserCode");
                String strAddTime = jsonObject.getString("AddTime");
                int pIndex = (i + 1) * 10;
                ProjectPlanEntity item = new ProjectPlanEntity(iId, strTitle, strContent, strBDate, strEDate, isEnd, iEndPercentage, iTimeUsePercentage, strUserCode, strAddTime);

                items.add(item);
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    //endregion

    //region 计划进度操作方法
    public boolean AddPlanPercentageEntity(PlanPercentageEntity item, UserInfoEntity myUser) {
        boolean isAdd = false;
        String methodName = "AddPlanPercentage";
        String soapAction = nameSpace + "/" + methodName + "/";
        // 命名空间     String nameSpace = "http://tempuri.org/";
        // 调用的方法名称     String methodName = "HelloWorld";
        // EndPoint     String endPoint = "http://192.168.16.39:1215/WebService.asmx";
        // SOAP Action     String soapAction = "http://tempuri.org//HelloWorld/";
        // 指定WebService的命名空间和调用的方法名

        SoapObject rpc = new SoapObject(nameSpace, methodName);

        //string strUserCode,string strTitle,string strContent,string strBeginDate, string strEndDate
        // 设置需调用WebService接口需要传入的参数
        rpc.addProperty("iPlanId", item.getPlanId());
        rpc.addProperty("strUserCode", myUser.getUserCode());
        rpc.addProperty("iEndPercentage", item.getEndPercentage());
        rpc.addProperty("isEnd", item.isEnd());
        rpc.addProperty("strRemark", item.getRemark());
        rpc.addProperty("strPwdCode", myUser.getPwdCode());

        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = rpc;

        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;
        (new MarshalBase64()).register(envelope);

        // 等价于envelope.bodyOut = rpc;
        envelope.setOutputSoapObject(rpc);
        HttpTransportSE transport = new HttpTransportSE(endPoint);
        transport.debug = true;
        try {

            // 调用WebService
            transport.call(soapAction, envelope);
            if (envelope.getResponse() != null) {
                isAdd = Boolean.valueOf(envelope.getResponse().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isAdd;
    }
    //endregion

    //region 用户登陆操作方法

    //region 用户登陆信息的本地保存和读取
    private boolean LoginToDB(String strLoginUser, String strUserJson) {
        //判断用户是否登陆成功，并将登陆系统保存到数据库
        try {
            boolean isLogin = false;

            JSONObject jsonObject = new JSONObject(strUserJson);

            int iId = jsonObject.getInt("Id");
            String strUserCode = jsonObject.getString("UserCode");
            String strNickName = jsonObject.getString("NickName");
            String strUserName = jsonObject.getString("UserName");
            String strWXCode = jsonObject.getString("WXCode");
            String strPwdCode = jsonObject.getString("PwdCode");
            String strMobilePhone = jsonObject.getString("MobilePhone");
            String strToken = jsonObject.getString("UserToken");
            if (strUserCode != null) {
                if (!(strToken == null || strToken.length() <= 0)) {
                    UserInfoEntity item = new UserInfoEntity(iId, strUserCode, strUserName, strNickName, strPwdCode);
                    item.setWXCode(strWXCode);
                    item.setMobilePhone(strMobilePhone);
                    item.setUserToken(strToken);
                    isLogin = true;
                    //region 将登录账号数据写入本地数据库随时调用
                    UserInLocal(item);
                    //endregion
                }
            }

            return isLogin;

        } catch (JSONException ex) {
            return false;
        }
    }

    private void UserInLocal(UserInfoEntity info) {
        SharedPreferences.Editor edit = context.getSharedPreferences("mypsd", context.MODE_PRIVATE).edit();
        edit.putString("USER_CODE", info.getUserCode());
        edit.putString("USER_NAME", info.getUserName());
        edit.putString("USER_NICK", info.getNickName());
        edit.putString("USER_PWD", info.getPwdCode());
        edit.putString("USER_TOKEN", info.getUserToken());
        edit.commit();
    }

    //清除用户登陆的保存的本地信息
    public void UserRemoveLocal() {
        SharedPreferences.Editor edit = context.getSharedPreferences("mypsd", context.MODE_PRIVATE).edit();
        edit.remove("USER_CODE");
        edit.remove("USER_NAME");
        edit.remove("USER_NICK");
        edit.remove("USER_PWD");
        edit.remove("USER_TOKEN");
        edit.commit();
    }

    /**
     * 获取本地存储的用户登陆信息
     */
    public UserInfoEntity LocalToUser(Context mContext) {
        SharedPreferences shared = mContext.getSharedPreferences("mypsd", mContext.MODE_PRIVATE);
        int iId = 0;
        String strUserCode = shared.getString("USER_CODE", "");
        String strNickName = shared.getString("USER_NAME", "");
        String strUserName = shared.getString("USER_NICK", "");
        String strPwdCode = shared.getString("USER_PWD", "");
        String strToken = shared.getString("USER_TOKEN", "");
        UserInfoEntity item = new UserInfoEntity(iId, strUserCode, strNickName, strUserName, strPwdCode);
        item.setUserToken(strToken);
        return item;
    }

    //endregion

    public boolean UserLoginSystem(String strUserCode, String strUserPwd, Context mContext) {
        this.context = mContext;
        boolean isLogin = false;
        String methodName = "UserLogin";
        String soapAction = nameSpace + "/" + methodName + "/";
        // 命名空间     String nameSpace = "http://tempuri.org/";
        // 调用的方法名称     String methodName = "HelloWorld";
        // EndPoint     String endPoint = "http://192.168.16.39:1215/WebService.asmx";
        // SOAP Action     String soapAction = "http://tempuri.org//HelloWorld/";
        // 指定WebService的命名空间和调用的方法名

        SoapObject rpc = new SoapObject(nameSpace, methodName);

        String strMD5PWd = mmd5(strUserPwd);
        // 设置需调用WebService接口需要传入的参数
        rpc.addProperty("strUserCode", strUserCode);
        rpc.addProperty("strPwdCode", strMD5PWd);

        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = rpc;

        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;
        (new MarshalBase64()).register(envelope);

        // 等价于envelope.bodyOut = rpc;
        envelope.setOutputSoapObject(rpc);
        HttpTransportSE transport = new HttpTransportSE(endPoint);
        transport.debug = true;
        try {

            // 调用WebService
            transport.call(soapAction, envelope);
            if (envelope.getResponse() != null) {
                //
                String strReturn = envelope.getResponse().toString();
                isLogin = LoginToDB(strUserCode, strReturn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isLogin;
    }

    public boolean UserLoginApp(String strUserCode, String strUserPwd, Context mContext) {
        this.context = mContext;
        boolean isLogin = false;
        try {

            String PseudoID = CrashHandler.getUniquePsuedoID();
            String strMD5PWd = mmd5(strUserPwd);

            String strMethodName = "UserLoginV2";
            JSONObject json = new JSONObject();
            json.put("UserCode", strUserCode);
            json.put("PwdCode", strMD5PWd);
            json.put("PseudoID", PseudoID);
            String strRequestJson = json.toString();
            String strJson = GetServiceJsonRequest(strMethodName, strRequestJson);

            if (strJson != "") {
                isLogin = LoginToDB(strUserCode, strJson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return isLogin;
    }

    /**
     * 修改用户密码操作
     * d* @param strUserCode
     *
     * @param strOldPwd
     * @param strNewPwd
     * @return
     */
    //用户修改密码
    public boolean UserEditPwd(String strUserCode, String strOldPwd, String strNewPwd) {
        boolean isLogin = false;
        String methodName = "UserEditPwd";
        String soapAction = nameSpace + "/" + methodName + "/";
        // 命名空间     String nameSpace = "http://tempuri.org/";
        // 调用的方法名称     String methodName = "HelloWorld";
        // EndPoint     String endPoint = "http://192.168.16.39:1215/WebService.asmx";
        // SOAP Action     String soapAction = "http://tempuri.org//HelloWorld/";
        // 指定WebService的命名空间和调用的方法名

        SoapObject rpc = new SoapObject(nameSpace, methodName);

        String strMD5PWd = mmd5(strOldPwd);
        String strNewMD5Pwd = mmd5(strNewPwd);
        // 设置需调用WebService接口需要传入的参数
        rpc.addProperty("strUserCode", strUserCode);
        rpc.addProperty("strPwdCode", strMD5PWd);
        rpc.addProperty("strNewPwdCode", strNewMD5Pwd);

        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = rpc;

        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;
        (new MarshalBase64()).register(envelope);

        // 等价于envelope.bodyOut = rpc;
        envelope.setOutputSoapObject(rpc);
        HttpTransportSE transport = new HttpTransportSE(endPoint);
        transport.debug = true;
        try {

            // 调用WebService
            transport.call(soapAction, envelope);
            if (envelope.getResponse() != null) {
                //
                String strReturn = envelope.getResponse().toString();
                isLogin = LoginToDB(strUserCode, strReturn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isLogin;
    }

    /**
     * 用户注册操作
     *
     * @param strUserCode
     * @param “用户姓名”
     * @param “用户昵称”
     * @param “用户密码”
     * @return
     */
    public boolean UserRegiter(String strUserCode, String strUserName, String strNickName, String strPwd) {
        boolean IsRegiter = false;

        String strMD5PWd = mmd5(strPwd);
        try {
            JSONObject json = new JSONObject();
            json.put("UserCode", strUserCode);
            json.put("UserName", strUserName);
            json.put("NickName", strNickName);
            json.put("PwdCode", strMD5PWd);

            //将json数据转换后传入服务器
            String strJson = json.toString();

            //region 数据处理
            String methodName = "UserRegister";
            String soapAction = nameSpace + "/" + methodName + "/";
            SoapObject rpc = new SoapObject(nameSpace, methodName);
            // 设置需调用WebService接口需要传入的参数
            rpc.addProperty("strRequestJson", strJson);
            // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.bodyOut = rpc;

            // 设置是否调用的是dotNet开发的WebService
            envelope.dotNet = true;
            (new MarshalBase64()).register(envelope);

            // 等价于envelope.bodyOut = rpc;
            envelope.setOutputSoapObject(rpc);
            HttpTransportSE transport = new HttpTransportSE(endPoint);
            transport.debug = true;
            try {

                // 调用WebService
                transport.call(soapAction, envelope);
                if (envelope.getResponse() != null) {
                    String strReturn = envelope.getResponse().toString();
                    UserAPIResult apiResult = new UserAPIResult(strReturn);
                    //添加失败
                    if (apiResult.getResultStatus().equals("fail")) {
                        IsRegiter = false;
                    } else {
                        IsRegiter = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //endregion

        } catch (JSONException e) {
            e.printStackTrace();
            IsRegiter = false;
        }
        return IsRegiter;
    }

    //    private
    //endregion

    //region 工作组操作方法


    /**
     * 获取用户的工作组成员集合
     *
     * @param strUserToken
     * @return
     */
    public List<UserInfoEntity> GetUserTeams(String strUserToken) {
        List<UserInfoEntity> items = new ArrayList<>();
        try {
            String strMethodName = "GetUserTeams";
            JSONObject json = new JSONObject();
            json.put("UserToken", strUserToken);
            String strRequestJson = json.toString();

            String strJson = GetServiceJsonRequest(strMethodName, strRequestJson);

            //region 分析数据
            if (strJson != "") {
                JSONArray jsonArray = new JSONArray(strJson);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int iId = jsonObject.getInt("Id");
                    String strUserCode = jsonObject.getString("UserCode");
                    String strNickName = jsonObject.getString("NickName");
                    String strUserName = jsonObject.getString("UserName");
                    String strWXCode = jsonObject.getString("WXCode");
                    String strPwdCode = jsonObject.getString("PwdCode");
                    String strMobilePhone = jsonObject.getString("MobilePhone");
                    if (!(strUserCode == null || strUserCode.length() <= 0)) {
                        UserInfoEntity item = new UserInfoEntity(iId, strUserCode, strUserName, strNickName, strPwdCode);
                        item.setWXCode(strWXCode);
                        item.setMobilePhone(strMobilePhone);

                        items.add(item);
                    }
                }
            }
            //endregion

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }
    //endregion

    //region 内部操作方法

    /**
     * 通过调用服务获取数据的Json返回
     *
     * @param “调用的方法名称”
     * @param “传递的Json格式的参数”
     * @return
     */
    @NonNull
    private String GetServiceJsonRequest(String methodName, String strRequestJson) {
        String strJson = "";

        String soapAction = nameSpace + "/" + methodName + "/";
        SoapObject rpc = new SoapObject(nameSpace, methodName);

        // 设置需调用WebService接口需要传入的参数
        rpc.addProperty("strRequestJson", strRequestJson);

        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = rpc;

        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;
        (new MarshalBase64()).register(envelope);

        // 等价于envelope.bodyOut = rpc;
        envelope.setOutputSoapObject(rpc);
        HttpTransportSE transport = new HttpTransportSE(endPoint);
        transport.debug = true;
        try {

            // 调用WebService
            transport.call(soapAction, envelope);
            if (envelope.getResponse() != null) {
                strJson = String.valueOf(envelope.getResponse());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strJson;
    }
    //endregion

    //region 异常日志处理
    public void SendErrorToService(Context mContext, String versioninfo, String mobileInfo, String errorinfo) {
        //获取当前用户Token
        UserInfoEntity currentUser = LocalToUser(mContext);
        String strToken = currentUser.getUserToken();

        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String strDate = dataFormat.format(new Date());
        try {
            JSONObject json = new JSONObject();
            json.put("UserToken", strToken);
            json.put("VersionInfo", versioninfo);
            json.put("MobileInfo", mobileInfo);
            json.put("ErrorInfo", errorinfo);
            json.put("Created", strDate);

            //将json数据转换后传入服务器
            String strJson = json.toString();

            //region 数据处理
            String methodName = "AddErrorLog";
            String soapAction = nameSpace + "/" + methodName + "/";
            SoapObject rpc = new SoapObject(nameSpace, methodName);
            // 设置需调用WebService接口需要传入的参数
            rpc.addProperty("strRequestJson", strJson);
            // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.bodyOut = rpc;

            // 设置是否调用的是dotNet开发的WebService
            envelope.dotNet = true;
            (new MarshalBase64()).register(envelope);

            // 等价于envelope.bodyOut = rpc;
            envelope.setOutputSoapObject(rpc);
            HttpTransportSE transport = new HttpTransportSE(endPoint);
            transport.debug = true;
            try {

                // 调用WebService
                transport.call(soapAction, envelope);
                if (envelope.getResponse() != null) {
                    String strReturn = envelope.getResponse().toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //endregion

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //endregion

    //获取当前系统时间filter String
    public String getCurrentTime(){
        Format format = new SimpleDateFormat("HH:mm");
        return format.format(System.currentTimeMillis());
    }

    protected String getCurrentDate(){
        Format format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(System.currentTimeMillis());
    }

    //获取数据库的文件资源
    public List<FileEntity> getFileEntities(String strUserCode, String strUserPwd, String methodName, @Nullable String keyWord){

        //从数据库中拿文件的操作
        List<FileEntity> mFileList = new ArrayList<>();
        mFileList.add(new FileEntity());
        return mFileList;
    }

    public List<FileEntity> getUploadFileEntities(String strUserCode, String strUserPwd){
        String methodName = "getUploadFileEntities" ;
        return this.getFileEntities(strUserCode,strUserPwd,methodName,null);
    }

    public List<FileEntity> getLoadingFileEntities(String strUserCode, String strUserPwd){
        String methodName = "getLoadingFileEntities" ;
        return this.getFileEntities(strUserCode,strUserPwd,methodName,null);
    }

    public List<FileEntity> getReceiveFileEntities(String strUserCode, String strUserPwd){
        String methodName = "getReceiveFileEntities" ;
        return this.getFileEntities(strUserCode,strUserPwd,methodName,null);
    }

    public List<FileEntity> getSearchFileEntities(String strUserCode, String strUserPwd, String searchWord){
        String methodName = "getReceiveFileEntities";
        return this.getFileEntities(strUserCode,strUserPwd,methodName,searchWord);
    }


}
