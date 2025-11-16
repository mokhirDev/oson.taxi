package uz.oson.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.entity.UserState;
import uz.oson.taxi.entity.enums.*;
import uz.oson.taxi.repository.UserStateRepository;
import uz.oson.taxi.util.UpdateUtil;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStateRepository userStateRepository;
    private final UserCacheService userCacheService;

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

    void setDefaultCredentials(UserState entity) {
        entity.setLocale(LocaleEnum.UNKNOWN);
        entity.setRole(UserTypeEnum.GUEST);
        entity.setLastMessageId(0);
        entity.setBotPageStage(BotPageStageEnum.START);
        entity.setUserName(null);
        entity.setFirstName(null);
        entity.setSecondName(null);
        entity.setPhoneNumber(null);
        entity.setIsVerified(Verification.UNVERIFIED);
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

    public void setFirstName(Update update) {
        Long chatId = UpdateUtil.getChatId(update);
        InputType inputType = InputType.getInputType(update);
        if (inputType == null) return;
        if (inputType.equals(InputType.TEXT)) {
            UserState cache = userCacheService.get(chatId);
            if (cache == null) {
                return;
            }
            cache.setFirstName(update.getMessage().getText());
            userCacheService.put(cache);
        }
    }

    public BotPageStageEnum getCurrentPage(Long chatId) {
        UserState user = getUser(chatId);
        BotPageStageEnum botPageStage = user.getBotPageStage();
        if (botPageStage == null) {
            botPageStage = BotPageStageEnum.START;
            setCurrentPage(botPageStage, chatId);
        }
        return botPageStage;
    }

    public void setCurrentPage(BotPageStageEnum pageMessageEnum, Long chatId) {
        UserState user = getUser(chatId);
        user.setBotPageStage(pageMessageEnum);
        userCacheService.put(user);
    }

    public void setSecondName(Update update) {
        Long chatId = UpdateUtil.getChatId(update);
        InputType inputType = InputType.getInputType(update);
        if (inputType == null) return;
        if (inputType.equals(InputType.TEXT)) {
            UserState cache = userCacheService.get(chatId);
            if (cache == null) {
                return;
            }
            cache.setSecondName(update.getMessage().getText());
            userStateRepository.save(cache);
        }
    }

    public LocaleEnum getUserLocale(Update update) {
        if (PageCommandEnum.isValid(List.of(PageCommandEnum.LANG_CODE), update)) {
            String language = update.getCallbackQuery().getData();
            return LocaleEnum.getLocaleEnum(language);
        }
        LocaleEnum locale = userCacheService.get(UpdateUtil.getChatId(update)).getLocale();
        if (locale != null) {
            return locale;
        }
        return LocaleEnum.UNKNOWN;
    }

    public void pending(UserState user) {
        user.setIsVerified(Verification.PENDING);
        userCacheService.put(user);
        userStateRepository.save(user);
    }
}
