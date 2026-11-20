package com.dss.p1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dss.p1.service.DatabaseExportService;
import com.dss.p1.service.ProductoService;

import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;


@Controller 
@RequestMapping ("/admin")
@RequiredArgsConstructor 
public class AdminController {
    private final DatabaseExportService exportDB;
    @Autowired
    private ProductoService productService;
    @GetMapping
    public String admin(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin";     // templates/admin.html
    }
    @GetMapping("/download-db-sql")
    public ResponseEntity<byte[]> descargarSql() {
        byte[] contenido = exportDB.exportarSql();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename("backup.sql").build().toString())
                .contentType(MediaType.parseMediaType("application/sql"))
                .body(contenido);
    }
    

}
