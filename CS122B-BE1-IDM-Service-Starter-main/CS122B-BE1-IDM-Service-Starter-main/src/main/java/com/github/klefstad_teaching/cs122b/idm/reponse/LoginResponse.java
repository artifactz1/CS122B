package com.github.klefstad_teaching.cs122b.idm.reponse;

import java.util.List;

import com.github.klefstad_teaching.cs122b.core.result.Result;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.User;

public class LoginResponse {

    private Result result;
    private List<User> users;

    public Result getResult() {
        return result;
    }

    public LoginResponse setResult(Result result) {
        this.result = result;
        return this;
    }

    public List<User> getUsers() {
        return users;
    }

    public LoginResponse setUsers(List<User> users) {
        this.users = users;
        return this;
    }

}
