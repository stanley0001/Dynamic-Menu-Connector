package com.example.communication.menu.web.controllers;

import com.example.communication.menu.persistence.entities.Menus;
import com.example.communication.menu.persistence.entities.Options;
import com.example.communication.menu.services.MenuService;
import com.example.communication.shared.persistance.entities.Sessions;
import com.example.communication.shared.persistance.models.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("menu")
@Slf4j
public class MenuController {


    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("getDefaultMenu")
    public ResponseEntity<ResponseModel> getDefaultMenu() {

        ResponseModel response = menuService.getDefaultMenu();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("getAllMenu")
    public ResponseEntity<ResponseModel> getAllMenu(@RequestParam int page, @RequestParam int limit) {
        ResponseModel response = menuService.getAllMenus(page, limit);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("createMenu")
    public ResponseEntity<ResponseModel> createMenu(@RequestBody Menus menu) {
        ResponseModel response = menuService.createMenu(menu);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("updateMenu")
    public ResponseEntity<ResponseModel> updateMenu(@RequestBody Menus menu) {
        ResponseModel response = menuService.updateMenu(menu);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("createOption")
    public ResponseEntity<ResponseModel> createOption(@RequestBody Options option) {
        ResponseModel response = menuService.createOption(option);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("getMenuOption")
    public ResponseEntity<ResponseModel> getMenuOption(@RequestBody Sessions session, @RequestParam String option) {
        log.info("Getting Menu Option {} for : {}", option, session.getPhone());
        ResponseModel response = menuService.getOption(session, option);
        log.info("controller response {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("getAllOptions")
    public ResponseEntity<ResponseModel> getAllOptions(@RequestParam int page, @RequestParam int limit) {
        ResponseModel response = menuService.getAllOptions(page, limit);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
