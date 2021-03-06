package com.github.vahundos.breezer;

import com.github.vahundos.breezer.dto.UserRegistrationDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static com.github.vahundos.breezer.TestData.getUser1;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NotWellFormedUserRegistrationDtoData {

    public static Stream<Arguments> provideNotWellFormedUserFields() {
        var nullFirstName = new UserRegistrationDto(null, WellFormedUserData.SECOND_NAME, WellFormedUserData.NICKNAME,
                                                    WellFormedUserData.EMAIL, WellFormedUserData.PASSWORD);
        var nullSecondName = new UserRegistrationDto(WellFormedUserData.FIRST_NAME, null, WellFormedUserData.NICKNAME,
                                                     WellFormedUserData.EMAIL, WellFormedUserData.PASSWORD);
        var nullNickname = new UserRegistrationDto(WellFormedUserData.FIRST_NAME, WellFormedUserData.SECOND_NAME,
                                                   null, WellFormedUserData.EMAIL, WellFormedUserData.PASSWORD);
        var nullEmail = new UserRegistrationDto(WellFormedUserData.FIRST_NAME, WellFormedUserData.SECOND_NAME,
                                                WellFormedUserData.NICKNAME, null, WellFormedUserData.PASSWORD);
        var nullPassword = new UserRegistrationDto(WellFormedUserData.FIRST_NAME, WellFormedUserData.SECOND_NAME,
                                                   WellFormedUserData.NICKNAME, WellFormedUserData.EMAIL, null);
        var emptyFirstName = new UserRegistrationDto("", WellFormedUserData.SECOND_NAME, WellFormedUserData.NICKNAME,
                                                     WellFormedUserData.EMAIL, WellFormedUserData.PASSWORD);
        var emptySecondName = new UserRegistrationDto(WellFormedUserData.FIRST_NAME, "", WellFormedUserData.NICKNAME,
                                                      WellFormedUserData.EMAIL, WellFormedUserData.PASSWORD);
        var emptyNickname = new UserRegistrationDto(WellFormedUserData.FIRST_NAME, WellFormedUserData.SECOND_NAME, "",
                                                    WellFormedUserData.EMAIL, WellFormedUserData.PASSWORD);
        var emptyEmail = new UserRegistrationDto(WellFormedUserData.FIRST_NAME, WellFormedUserData.SECOND_NAME,
                                                 WellFormedUserData.NICKNAME, "", WellFormedUserData.PASSWORD);
        var emptyPassword = new UserRegistrationDto(WellFormedUserData.FIRST_NAME, WellFormedUserData.SECOND_NAME,
                                                    WellFormedUserData.NICKNAME, WellFormedUserData.EMAIL, "");
        var notWellFormedEmail = new UserRegistrationDto(WellFormedUserData.FIRST_NAME, WellFormedUserData.SECOND_NAME,
                                                         WellFormedUserData.NICKNAME, "not-well-formed-email",
                                                         WellFormedUserData.PASSWORD);
        var emailAlreadyExists = new UserRegistrationDto(WellFormedUserData.FIRST_NAME, WellFormedUserData.SECOND_NAME,
                                                         WellFormedUserData.NICKNAME, getUser1().getEmail(),
                                                         WellFormedUserData.PASSWORD);
        var nicknameAlreadyExists = new UserRegistrationDto(WellFormedUserData.FIRST_NAME, WellFormedUserData.SECOND_NAME,
                                                            getUser1().getNickname(), WellFormedUserData.EMAIL,
                                                            WellFormedUserData.PASSWORD);
        var notWellFormedNickname = new UserRegistrationDto(WellFormedUserData.FIRST_NAME, WellFormedUserData.SECOND_NAME,
                                                            "ivan_not_well_formed", WellFormedUserData.EMAIL,
                                                            WellFormedUserData.PASSWORD);
        return Stream.of(
                Arguments.of(nullFirstName),
                Arguments.of(nullSecondName),
                Arguments.of(nullNickname),
                Arguments.of(nullEmail),
                Arguments.of(nullPassword),

                Arguments.of(emptyFirstName),
                Arguments.of(emptySecondName),
                Arguments.of(emptyNickname),
                Arguments.of(emptyEmail),
                Arguments.of(emptyPassword),

                Arguments.of(notWellFormedEmail),
                Arguments.of(emailAlreadyExists),

                Arguments.of(nicknameAlreadyExists),
                Arguments.of(notWellFormedNickname)
        );
    }
}
