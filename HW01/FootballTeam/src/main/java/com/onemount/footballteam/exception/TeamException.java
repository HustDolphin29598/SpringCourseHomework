package com.onemount.footballteam.exception;

public class TeamException extends RuntimeException{

    private static final long serialVersionUID = 743962959923490925L;
    private String detail;

    public TeamException(String message, String detail) {
        super(message);
        this.detail = detail;
    }

    public TeamException(String message) {
        super(message);
    }

    public String getDetail() {
        return detail;
    }
}
