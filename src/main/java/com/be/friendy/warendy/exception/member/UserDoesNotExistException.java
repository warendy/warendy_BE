package com.be.friendy.warendy.exception.member;

public class UserDoesNotExistException extends RuntimeException{
    public UserDoesNotExistException() {
        super("존재하지 않은 유저입니다.");
    }
}
