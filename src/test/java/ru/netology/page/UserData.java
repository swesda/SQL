package ru.netology.page;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserData {
    private final String name;
    private final String password;
}
