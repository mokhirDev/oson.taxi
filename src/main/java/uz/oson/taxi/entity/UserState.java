package uz.oson.taxi.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.oson.taxi.entity.enums.PageCodeEnum;
import uz.oson.taxi.entity.enums.PageMessageEnum;
import uz.oson.taxi.entity.enums.UserTypeEnum;
import uz.oson.taxi.entity.enums.LocaleEnum;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_state")
public class UserState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "chat_id", nullable = false, unique = true)
    private Long chatId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "locale")
    @Enumerated(EnumType.STRING)
    private LocaleEnum locale;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserTypeEnum role;
    @Column(name = "current_page_code")
    @Enumerated(EnumType.STRING)
    private PageCodeEnum currentPageCode;
    @Column(name = "last_message_id")
    private Integer lastMessageId;
}