package uz.oson.taxi.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.oson.taxi.entity.enums.*;

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

    @Column(name = "bot_page_stage")
    @Enumerated(EnumType.STRING)
    private BotPageStageEnum botPageStage;

    @Column(name = "last_message_id")
    private Integer lastMessageId;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "second_name", length = 100)
    private String secondName;


    @Column(name = "is_verified", length = 100)
    @Enumerated(EnumType.STRING)
    private Verification isVerified;
}