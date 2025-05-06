package com.cotato.weather.domain.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.weather.domain.place.entity.SavedPlace;

@Repository
public interface SavedPlaceRepository extends JpaRepository<SavedPlace, Long> {
}
