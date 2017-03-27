package com.tripfriend.model.api;

public interface ApiResultListener {
    void requestSuccessfull(String data);
    void requestFailed(String message);
}
