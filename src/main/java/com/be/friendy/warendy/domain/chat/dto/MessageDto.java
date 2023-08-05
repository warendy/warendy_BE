package com.be.friendy.warendy.domain.chat.dto;

import com.be.friendy.warendy.domain.chat.entity.Message;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class MessageDto {
    private String sender;
    private String content;
    private String timestamp;
    private String roomId;

    public static MessageDto fromEntity(Message message){
        return MessageDto.builder()
                .sender(message.getSender())
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .roomId(message.getRoomId())
                .build();
    }

    public static List<MessageDto> fromEntityList(List<Message> messages){
        List<MessageDto> messageDtos = new ArrayList<>();
        for(Message message:messages){
            messageDtos.add(fromEntity(message));
        }
        return messageDtos;
    }
}
