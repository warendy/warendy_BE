package com.be.friendy.warendy.domain.chat.dto;

import com.be.friendy.warendy.domain.chat.entity.ChatRoom;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {
    private String roomId;
    private String creator;
    private String name;
    private Integer memberNum;

    public static ChatRoomDto fromEntity(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
                .roomId(chatRoom.getRoomId())
                .creator(chatRoom.getCreator().getEmail())
                .name(chatRoom.getName())
                .memberNum(chatRoom.getMemberNum())
                .build();
    }
}
