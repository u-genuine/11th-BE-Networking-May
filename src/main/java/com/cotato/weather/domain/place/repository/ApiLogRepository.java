package com.cotato.weather.domain.place.repository;

import com.cotato.weather.domain.place.dto.vo.ApiLog;
import com.cotato.weather.domain.place.dto.vo.ApiStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Repository
public class ApiLogRepository {
    private final JdbcTemplate jdbcTemplate;

    public void insertMainApiLog(String status, String code, String message) {
        String sql = "INSERT INTO api_log (status, code,  message, time) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, status, code, message, LocalDateTime.now());
    }

    public ApiLog findMainApiLogNowStatus() {
        String sql = "SELECT * FROM api_log ORDER BY time DESC LIMIT 1";
        return jdbcTemplate.queryForObject(sql, apiLogRowMapper);
    }

    public void insertMainApiLog(ApiStatus apiStatus) {
        String sql = "INSERT INTO api_log (status, code, message, time) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, apiStatus.getStatus(), apiStatus.getCode(), apiStatus.getMessage(), LocalDateTime.now());
    }

    private final RowMapper<ApiLog> apiLogRowMapper = (rs, rowNum) -> {
        ApiLog apiLog = new ApiLog();
        apiLog.setStatus(rs.getString("status"));
        apiLog.setCode(rs.getString("code"));
        apiLog.setMessage(rs.getString("message"));
        apiLog.setTime(rs.getObject("time", LocalDateTime.class));
        return apiLog;
    };
}
