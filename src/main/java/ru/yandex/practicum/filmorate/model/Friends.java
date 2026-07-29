package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Friends {

    private Long friendsId;
    private Long userId;
    private boolean isFriend;
}
