package model;

import com.google.gson.annotations.Expose;

public record AuthData(@Expose String username, @Expose String authToken) {}
