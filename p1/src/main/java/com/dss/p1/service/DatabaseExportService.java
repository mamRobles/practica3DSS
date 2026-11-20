package com.dss.p1.service;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class DatabaseExportService {
    private final JdbcTemplate jdbctemplate;
    public byte [] exportarSql(){
        List<String> lineas = jdbctemplate.queryForList("SCRIPT", String.class);
        return String.join("\n", lineas).getBytes(StandardCharsets.UTF_8);
    }
}
