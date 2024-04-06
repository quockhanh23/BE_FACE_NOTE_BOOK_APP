package com.example.final_case_social_web.dto;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
public class NotificationDTO {
    private Long id;
    private String title;
    private String status;
    private UserNotificationDTO idSendTo;
    private UserNotificationDTO idAction;
    private Long typeId;
    private String type;
    private Date createAt;

    @Override
    public String toString() {
        return "NotificationDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", idSendTo=" + idSendTo +
                ", idAction=" + idAction +
                ", typeId=" + typeId +
                ", type='" + type + '\'' +
                ", createAt=" + createAt +
                '}';
    }
}
