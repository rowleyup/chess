package model;

import com.google.gson.annotations.Expose;

public record UserData(@Expose String username, @Expose String password, @Expose String email) {}
