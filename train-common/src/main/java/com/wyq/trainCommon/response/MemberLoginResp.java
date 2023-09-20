package com.wyq.trainCommon.response;

public class MemberLoginResp {
    private Long id;


    private String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MemberLoginResp{");
        sb.append("id=").append(id);
        sb.append(", token='").append(token).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
