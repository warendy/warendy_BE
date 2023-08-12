package com.be.friendy.warendy.domain.chat.dto;

import com.be.friendy.warendy.domain.chat.entity.ChatRoom;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    public static List<ChatRoomDto> fromEntityList(List<ChatRoom> chatRooms){
        List<ChatRoomDto> chatRoomDtoList = new ArrayList<>();
        for(ChatRoom chatRoom : chatRooms){
            chatRoomDtoList.add(fromEntity(chatRoom));
        }
        return chatRoomDtoList;
    }
}
