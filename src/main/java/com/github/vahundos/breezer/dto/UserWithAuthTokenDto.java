package com.github.vahundos.breezer.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.view.UserViews;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserWithAuthTokenDto {

    @JsonView(UserViews.WithoutSensitiveData.class)
    private String authToken;

    @JsonView(UserViews.WithoutSensitiveData.class)
    private User user;
}
