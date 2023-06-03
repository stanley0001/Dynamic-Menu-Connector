package com.example.communication.menu.services;
import com.example.communication.menu.persistence.entities.Menus;
import com.example.communication.menu.persistence.entities.Options;
import com.example.communication.menu.persistence.entities.Sessions;
import com.example.communication.shared.ResponseModel;
import org.springframework.stereotype.Service;

@Service
public interface MenuService {
    ResponseModel getDefaultMenu();
    ResponseModel createMenu(Menus menu);
    ResponseModel getAllMenus(int page,int limit);
    ResponseModel createOption(Options option);
    ResponseModel getOption(String menu,String option);
    ResponseModel getOption(Long sessionId,String option);
    ResponseModel getAllOptions(int page,int limit);
    ResponseModel createSession(Sessions session);
    ResponseModel getMostRecentSession(String phone);


    }
