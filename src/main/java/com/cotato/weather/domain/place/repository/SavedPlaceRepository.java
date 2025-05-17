package com.cotato.weather.domain.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.weather.domain.place.entity.SavedPlace;

import java.util.List;

@Repository
public interface SavedPlaceRepository extends JpaRepository<SavedPlace, Long> {

    List<SavedPlace> findByUserId(Long id);
}
