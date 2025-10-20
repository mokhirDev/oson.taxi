package uz.oson.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.entity.UserState;
import uz.oson.taxi.entity.enums.*;
import uz.oson.taxi.repository.UserStateRepository;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserStateService {
    private final UserStateRepository userStateRepository;
    private final UserStateCacheService userCacheService;

    public UserState setNewCredentials(Long chatId) {
        UserState userState;
        Optional<UserState> userStateOptional = userStateRepository.findByChatId(chatId);
        if (userStateOptional.isPresent()) {
            userState = userStateOptional.get();
            setDefaultCredentials(userState);
        } else {
            userState = UserState.builder().build();
            userState.setChatId(chatId);
            setDefaultCredentials(userState);
        }
        userCacheService.put(userState);
        return userStateRepository.save(userState);
    }

    public Long getChatId(Update update) {
        InputType inputType = InputType.getInputType(update);
        if (inputType == null) return null;
        return switch (inputType) {
            case TEXT, CONTACT -> update.getMessage().getChatId();
            case CALLBACK -> update.getCallbackQuery().getFrom().getId();
            default -> null;
        };
    }

    void setDefaultCredentials(UserState entity) {
        entity.setLocale(LocaleEnum.UNKNOWN);
        entity.setRole(UserTypeEnum.GUEST);
        entity.setLastMessageId(0);
        entity.setCurrentPageCode(PageCodeEnum.START_CODE);
    }

    public void setLang(LocaleEnum localeEnum, Long chatId) {
        UserState entity;
        Optional<UserState> userState = userStateRepository.findByChatId(chatId);
        if (userState.isPresent()) {
            userState.get().setLocale(localeEnum);
            entity = userStateRepository.save(userState.get());
        } else {
            entity = setNewCredentials(chatId);
        }
        userCacheService.put(entity);

    }

    public void setRole(Update update, Long chatId) {
        UserState entity;
        String role = update.getCallbackQuery().getData().toUpperCase();
        if (!UserTypeEnum.existRole(role)) {
            return;
        }
        UserTypeEnum userTypeEnum = UserTypeEnum.valueOf(role);
        Optional<UserState> userState = userStateRepository.findByChatId(chatId);
        if (userState.isPresent()) {
            userState.get().setRole(userTypeEnum);
            entity = userStateRepository.save(userState.get());
        } else {
            entity = setNewCredentials(chatId);
        }
        userCacheService.put(entity);
    }

    public UserState getUser(Long chatId) {
        UserState userState = userCacheService.get(chatId);
        if (userState == null) {
            Optional<UserState> entity = userStateRepository.findByChatId(chatId);
            userState = entity.orElseGet(() -> setNewCredentials(chatId));
            userCacheService.put(userState);
        }
        return userState;
    }

    public PageCodeEnum getCurrentPage(Long chatId) {
        UserState user = getUser(chatId);
        return user.getCurrentPageCode();
    }

    public void setCurrentPage(PageCodeEnum pageMessageEnum, Long chatId) {
        UserState user = getUser(chatId);
        user.setCurrentPageCode(pageMessageEnum);
        userCacheService.put(user);
    }
}
