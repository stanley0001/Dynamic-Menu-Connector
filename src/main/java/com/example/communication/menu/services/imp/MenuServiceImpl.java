package com.example.communication.menu.services.imp;

import com.example.communication.journey.services.JourneyService;
import com.example.communication.menu.persistence.entities.Menus;
import com.example.communication.menu.persistence.entities.Options;
import com.example.communication.menu.persistence.models.Menu;
import com.example.communication.menu.persistence.repositories.MenuRepository;
import com.example.communication.menu.persistence.repositories.OptionsRepository;
import com.example.communication.menu.services.MenuService;
import com.example.communication.shared.persistance.entities.Sessions;
import com.example.communication.shared.persistance.models.ResponseModel;
import com.example.communication.shared.services.SessionService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService {
    private final ModelMapper mapper = new ModelMapper();

    private final MenuRepository menuRepository;

    private final OptionsRepository optionsRepository;

    private final SessionService sessionService;

    private final JourneyService journeyService;

    public MenuServiceImpl(MenuRepository menuRepository, OptionsRepository optionsRepository, SessionService sessionService, JourneyService journeyService) {
        this.menuRepository = menuRepository;
        this.optionsRepository = optionsRepository;
        this.sessionService = sessionService;
        this.journeyService = journeyService;
    }

    public ResponseModel getDefaultMenu() {
        ResponseModel response = new ResponseModel<>();
        Optional<Menus> menu = menuRepository.findTopByLevel("DEFAULT");
        if (menu.isPresent()) {
            Menu displayMenu = mapper.map(menu.get(), Menu.class);
            response.setBody(displayMenu);
            response.setStatus("200");
            response.setMessage("Default Menu Found");
            return response;
        }
        response.setMessage("No Default Menu found");
        response.setStatus("404");
        return response;
    }

    public ResponseModel getMenuByName(String name) {
        ResponseModel response = new ResponseModel<>();
        Optional<Menus> menu = menuRepository.findTopByName(name);
        if (menu.isPresent()) {
            Menu displayMenu = mapper.map(menu.get(), Menu.class);
            response.setBody(displayMenu);
            response.setStatus("200");
            response.setMessage("Menu Found");
            return response;
        }
        response.setMessage("No Menu found");
        response.setStatus("404");
        return response;
    }

    public ResponseModel createMenu(Menus menu) {
        ResponseModel response = new ResponseModel();
        Optional<Menus> preExistingMenu = menuRepository.findByName(menu.getName());
        if (preExistingMenu.isPresent()) {
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

    public ResponseModel updateMenu(Menus menu) {
        ResponseModel response = new ResponseModel();
        Optional<Menus> preExistingMenu = menuRepository.findByName(menu.getName());
        if (preExistingMenu.isPresent()) {
            response.setStatus("200");
            response.setBody(menuRepository.save(menu));
            response.setMessage("Menu updated");
            return response;
        }
        response.setStatus("404");
        response.setMessage("Menu not Found");
        return response;
    }

    public ResponseModel getOption(Sessions session, String option) {
        if (option.equalsIgnoreCase("0"))
            return this.getMenuByName(session.getPreviousMenu());
        if (option.equalsIgnoreCase("000"))
            return sessionService.closeSession(session);
        ResponseModel response = new ResponseModel();
        if (option.equalsIgnoreCase("00")) {
            Menu displayMenu = mapper.map(this.getDefaultMenu().getBody(), Menu.class);
            response.setStatus("200");
            response.setMessage("Option Found");
            response.setBody(displayMenu);
            return response;
        }
        if (session.getIsAnsweringQuestions())
            return journeyService.handleRequest(session.getId(), option);
        Optional<Options> menuOption = optionsRepository.findByMenuAndOption(session.getCurrentMenu(), option);
        if (menuOption.isPresent()) {
            if (menuOption.get().getIsJourney())
                return journeyService.handleRequest(session.getId(), menuOption.get().getJourney());
            Menu displayMenu = mapper.map(menuOption.get().getDisplayMenu(), Menu.class);
            response.setStatus("200");
            response.setMessage("Option Found");
            response.setBody(displayMenu);
            return response;
        }
        Optional<Options> menuTextOption = optionsRepository.findByMenuAndText(session.getCurrentMenu(), option);
        if (menuTextOption.isPresent()) {
            if (menuTextOption.get().getIsJourney())
                return journeyService.handleRequest(session.getId(), menuTextOption.get().getJourney());
            Menu displayMenu = mapper.map(menuTextOption.get().getDisplayMenu(), Menu.class);
            response.setStatus("200");
            response.setMessage("Option Found");
            response.setBody(displayMenu);
            return response;
        }
        response.setStatus("404");
        response.setMessage("Option not found");
        return response;
    }

    public ResponseModel getOption(Long sessionId, String command) {
        Sessions session = sessionService.findSessionById(sessionId);
        ResponseModel response = this.getOption(session, command);
        if (response.getStatus().equalsIgnoreCase("200")) {
            //update menus
            session.setPreviousMenu(session.getCurrentMenu());
            session.setUpdatedAt(LocalDateTime.now());
            session.setCurrentMenu(mapper.map(response.getBody(), Menu.class).getName());
            sessionService.updateSession(session);
        }
        return response;
    }

    public ResponseModel createOption(Options option) {
        ResponseModel response = new ResponseModel();
        response.setStatus("201");
        response.setMessage("Option Created");
        if (option.getIsJourney() == null)
            option.setIsJourney(false);
        response.setBody(optionsRepository.save(option));
        return response;
    }

    public ResponseModel getAllMenus(int page, int limit) {
        ResponseModel response = new ResponseModel();
        List<Menus> menu = menuRepository.findAll();
        if (menu.isEmpty()) {
            response.setMessage("No Menu found");
            response.setStatus("404");
            return response;
        }
        response.setMessage("Menu found");
        response.setStatus("200");
        response.setBody(menu);
        return response;
    }

    public ResponseModel getAllOptions(int page, int limit) {
        ResponseModel response = new ResponseModel();
        List<Options> options = optionsRepository.findAll();
        if (options.isEmpty()) {
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
