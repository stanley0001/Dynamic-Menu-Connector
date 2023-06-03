package com.example.communication.menu.services.imp;

import com.example.communication.menu.persistence.entities.Menus;
import com.example.communication.menu.persistence.entities.Options;
import com.example.communication.menu.persistence.entities.Sessions;
import com.example.communication.menu.persistence.models.Menu;
import com.example.communication.shared.ResponseModel;
import com.example.communication.menu.persistence.repositories.MenuRepository;
import com.example.communication.menu.persistence.repositories.OptionsRepository;
import com.example.communication.menu.persistence.repositories.SessionRepository;
import com.example.communication.menu.services.MenuService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class MenuServiceImpl implements MenuService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    ModelMapper mapper=new ModelMapper();
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    OptionsRepository optionsRepository;
    @Autowired
    SessionRepository sessionRepository;
    private static final int sessionExpiry=3;
    public ResponseModel getDefaultMenu(){
        ResponseModel response=new ResponseModel<>();
        Optional<Menus> menu=menuRepository.findTopByLevel("DEFAULT");
        if (menu.isPresent()){
            Menu displayMenu=mapper.map(menu.get(),Menu.class);
            response.setBody(displayMenu);
            response.setStatus("200");
            response.setMessage("Default Menu Found");
            return response;
        }
        response.setMessage("No Default Menu found");
        response.setStatus("404");
        return response;
    }
    public ResponseModel getMenuByName(String name){
        ResponseModel response=new ResponseModel<>();
        Optional<Menus> menu=menuRepository.findTopByName(name);
        if (menu.isPresent()){
            Menu displayMenu=mapper.map(menu.get(),Menu.class);
            response.setBody(displayMenu);
            response.setStatus("200");
            response.setMessage("Menu Found");
            return response;
        }
        response.setMessage("No Menu found");
        response.setStatus("404");
        return response;
    }
    public ResponseModel createMenu(Menus menu){
        ResponseModel response=new ResponseModel();
        Optional<Menus> preExistingMenu=menuRepository.findByName(menu.getName());
        if (preExistingMenu.isPresent()){
            response.setStatus("200");
            response.setBody(preExistingMenu.get());
            response.setMessage("Pre existing menu with the same name found");
            return response;
        }
        response.setBody(menuRepository.save(menu));
        response.setStatus("201");
        response.setMessage("Menu Created");
        return response;
    }
    public ResponseModel getOption(String menu,String option){
        ResponseModel response=new ResponseModel();
        if (option.equalsIgnoreCase("00")){
            Menu displayMenu=mapper.map(this.getDefaultMenu().getBody(),Menu.class);
            response.setStatus("200");
            response.setMessage("Option Found");
            response.setBody(displayMenu);
            return response;
        }
        Optional<Options> menuOption=optionsRepository.findByMenuAndOption(menu,option);
        if (menuOption.isPresent()){
            Menu displayMenu=mapper.map(menuOption.get().getDisplayMenu(),Menu.class);
            response.setStatus("200");
            response.setMessage("Option Found");
            response.setBody(displayMenu);
            return response;
        }
        Optional<Options> menuTextOption=optionsRepository.findByMenuAndText(menu,option);
        if (menuTextOption.isPresent()){
            Menu displayMenu=mapper.map(menuTextOption.get().getDisplayMenu(),Menu.class);
            response.setStatus("200");
            response.setMessage("Option Found");
            response.setBody(displayMenu);
            return response;
        }
        response.setStatus("404");
        response.setMessage("Option not found");
        return response;
    }
    public ResponseModel getOption(Long sessionId,String command){
        Sessions session=sessionRepository.findById(sessionId).get();
        ResponseModel response=this.getOption(session.getCurrentMenu(),command);
        if (command.equalsIgnoreCase("0"))
            response=this.getMenuByName(session.getPreviousMenu());
        if (command.equalsIgnoreCase("000"))
            return closeSession(session);
        if (response.getStatus().equalsIgnoreCase("200")){
            //update menus
            session.setPreviousMenu(session.getCurrentMenu());
            session.setUpdatedAt(LocalDateTime.now());
            session.setCurrentMenu(mapper.map(response.getBody(),Menu.class).getName());
            sessionRepository.save(session);
        }
        return response;
    }
    public ResponseModel createSession(Sessions session){
        ResponseModel response=new ResponseModel();
        session.setCurrentMenu("WELCOME");
        response.setStatus("201");
        response.setMessage("Session Created");
        response.setBody(sessionRepository.save(session));
        return response;
    }
    public ResponseModel closeSession(Sessions session){
        ResponseModel response=new ResponseModel();
        session.setCurrentMenu("WELCOME");
        response.setStatus("301");
        response.setMessage("Session Closed");
        session.setUpdatedAt(LocalDateTime.now().minusHours(4));
        sessionRepository.save(session);
        response.setMessage("Bye \n Respond with anything to get the default menu");
        return response;
    }
    public ResponseModel createOption(Options option){
        ResponseModel response=new ResponseModel();
        response.setStatus("201");
        response.setMessage("Option Created");
        response.setBody(optionsRepository.save(option));
        return response;
    }
    public ResponseModel getMostRecentSession(String phone){
        ResponseModel response=new ResponseModel();
        Optional<Sessions> session=sessionRepository.findFirstByPhoneOrderByUpdatedAtDesc(phone);
        if (session.isPresent()){
            if (session.get().getUpdatedAt().isAfter(LocalDateTime.now().minusHours(sessionExpiry))){
                response.setStatus("200");
                response.setMessage("Active Session Found");
                response.setBody(session.get());
                return response;
            }
        }
        response.setStatus("404");
        response.setMessage("No Active Session Found");
        return response;
    }
    public ResponseModel getAllMenus(int page,int limit){
        ResponseModel response=new ResponseModel();
        List<Menus> menu=menuRepository.findAll();
        if (menu.isEmpty()){
            response.setMessage("No Menu found");
            response.setStatus("404");
            return response;
        }
        response.setMessage("Menu found");
        response.setStatus("200");
        response.setBody(menu);
        return response;
    }
    public ResponseModel getAllOptions(int page,int limit){
        ResponseModel response=new ResponseModel();
        List<Options> options=optionsRepository.findAll();
        if (options.isEmpty()){
            response.setMessage("No Options found");
            response.setStatus("404");
            return response;
        }
        response.setMessage("Options found");
        response.setStatus("200");
        response.setBody(options);
        return response;
    }

}
