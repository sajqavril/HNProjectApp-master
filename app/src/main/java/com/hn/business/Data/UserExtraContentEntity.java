package com.hn.business.Data;

import java.util.ArrayList;
import java.util.List;

//用户的外部信息储存类 用map存储
public class UserExtraContentEntity {

    //表示存到本地的聊天内容
    // 之后改进：映射为本地存储区域，用getChatContentLocal(userCode)来实现
    private List<ChatEntity> mChatContentLocal;

    //个人的所有计划信息
    private List<ProjectPlanEntity> mPlans;

    //个人所有小组列表
    private List<TeamEntity> mTeams;

    public UserExtraContentEntity(){
        this.mPlans = new ArrayList<>();
        this.mChatContentLocal = new ArrayList<>();
        this.mTeams = new ArrayList<>();

    }

    public List<ChatEntity> getChatContentLocal() {
        return mChatContentLocal;
    }

    public List<ProjectPlanEntity> getPlans() {
        return mPlans;
    }

    public List<TeamEntity> getTeams() {
        return mTeams;
    }

}

