package com.be.friendy.warendy.exception.member;

public class DuplicatedUserException extends RuntimeException{
    public DuplicatedUserException() {
        super("아이디가 이미 존재합니다.");
    }
}
