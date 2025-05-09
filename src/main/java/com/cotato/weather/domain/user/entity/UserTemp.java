package com.cotato.weather.domain.user.entity;

import com.cotato.weather.domain.place.entity.SavedPlace;
import com.cotato.weather.global.exception.ErrorCode;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class UserTemp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    public SavedPlace createSavePlace(String placeName, Double x, Double y) {
        return SavedPlace.builder()
                .userTemp(this)
                .placeName(placeName)
                .x(x)
                .y(y)
                .isPinned("N")
                .build();
    }

    public void updatePin(SavedPlace savedPlace) {
        if(!savedPlace.getUserTemp().equals(this)) {
            throw new IllegalArgumentException(ErrorCode.USER_NOT_AUTHORIZED_FOR_ACTION.getMessage());
        }

        savedPlace.updatePin();
    }

}
