package com.hn.business.Data;

import java.util.ArrayList;
import java.util.List;

public class TeamEntity {
    private List<UserInfoEntity> teamMembers = new ArrayList<>();
    private UserInfoEntity teamLeader = new UserInfoEntity();
    private int teamSize;//包括组长在内
    private String teamName;
    private String teamCode;
    private String teamContent;
    private List<ProjectPlanEntity> teamPlans = new ArrayList<>();

    public TeamEntity(){

    }

    public TeamEntity(String teamName, UserInfoEntity teamLeader){
        this.teamName = teamName;
        this.teamLeader = teamLeader;

    }

    public TeamEntity(String teamName, UserInfoEntity teamLeader, List<UserInfoEntity> teamMembers){
        this(teamName, teamLeader);
        this.teamMembers = teamMembers;
    }

    public TeamEntity(String teamName, UserInfoEntity teamLeader, List<UserInfoEntity> teamMembers,
                      String teamContent){
        this(teamName,teamLeader,teamMembers);
        this.teamContent = teamContent;
    }

    public List<UserInfoEntity> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<UserInfoEntity> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public UserInfoEntity getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(UserInfoEntity teamLeader) {
        this.teamLeader = teamLeader;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getTeamContent() {
        return teamContent;
    }

    public void setTeamContent(String teamContent) {
        this.teamContent = teamContent;
    }

    public List<ProjectPlanEntity> getTeamPlans() {
        return teamPlans;
    }

    public void setTeamPlans(List<ProjectPlanEntity> teamPlans) {
        this.teamPlans = teamPlans;
    }
}
