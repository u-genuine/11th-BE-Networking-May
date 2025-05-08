package com.cotato.weather.domain.user.entity;

import com.cotato.weather.domain.place.entity.SavedPlace;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class UserTemp {
    @jakarta.persistence.Id
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
            throw new IllegalArgumentException("잘못된 사용자입니다.");
        }

        savedPlace.updatePin();
    }

}
