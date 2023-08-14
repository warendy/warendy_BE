package com.be.friendy.warendy.exception.member;

public class DuplicatedUserNicknameException extends RuntimeException {
    public DuplicatedUserNicknameException() {
        super("이미 사용중인 닉네임 입니다");
    }
}
