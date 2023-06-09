package com.example.communication.ussd.services.impl;

import com.example.communication.menu.persistence.models.Menu;
import com.example.communication.menu.services.MenuService;
import com.example.communication.shared.persistance.entities.Sessions;
import com.example.communication.shared.persistance.models.ResponseModel;
import com.example.communication.shared.services.SessionService;
import com.example.communication.ussd.services.USSDService;
import com.example.communication.whatsapp.persistance.entities.Conversation;
import com.example.communication.whatsapp.persistance.repositories.ConversationRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;


@Service
@EnableAsync
@Slf4j
public class UssdClient<T> implements USSDService {


    private final ModelMapper modelMapper = new ModelMapper();

    private final ConversationRepository conversationRepository;

    private final MenuService menuService;

    private final SessionService sessionService;

    public UssdClient(ConversationRepository conversationRepository, MenuService menuService, SessionService sessionService) {
        this.conversationRepository = conversationRepository;
        this.menuService = menuService;
        this.sessionService = sessionService;

    }

    public String webHook(String networkCode, String phoneNumber, String serviceCode, String sessionId, String text) {
        //Process the message and show menus
        return this.userInteraction(phoneNumber, text, sessionId);
    }

    @Async
    public String userInteraction(String phone, String message, String sessionId) {
        String str = message;
        // Find the index of the last *
        int lastIndex = str.lastIndexOf("*");
        if (lastIndex != -1) {
            // Extract the substring after the last *
            String lastPart = str.substring(lastIndex + 1);
            message = lastPart;
        }
        String ussdHeader = "CON ";
        String ussdFooter = "\n00.Home \n000.logout";
        if (message.equalsIgnoreCase("000")) {
            ussdHeader = "END ";
            ussdFooter = "";
        }
        String displayString = ussdHeader + this.getMenu(phone, message, sessionId) + ussdFooter;
        //save message and send response to user
        //TODO:save the response
        //this.sendWhatsappMessage(this.instanceId,phone,displayString);
        return displayString;
    }

    private String getMenu(String phone, String command, String sessionId) {
        Sessions session = new Sessions();
        session.setUssdSessionId(sessionId);
        Menu menu = null;
        ResponseModel activeSession = sessionService.getMostRecentSession(phone);


        if (!activeSession.getStatus().equalsIgnoreCase("200")) {
            session.setPhone(phone);
            sessionService.createSession(session);
            //show default menu
            return modelMapper.map(menuService.getDefaultMenu().getBody(), Menu.class).getMenuValue();
        }

        //use the current session
        session = modelMapper.map(activeSession.getBody(), Sessions.class);
        ResponseModel menuResponse = menuService.getOption(session.getId(), command);

        if (menuResponse.getStatus().equalsIgnoreCase("200")) {
            menu = modelMapper.map(menuResponse.getBody(), Menu.class);
            //return menu value
            return menu.getMenuValue();
        }
        //return the message
        return menuResponse.getMessage();

    }

    @Async
    public void saveMessage(String phone, String name, String message, String conversationId, String messageId) {
        Conversation conversation = new Conversation(name, phone, messageId, conversationId, message, false);
        conversationRepository.save(conversation);
    }
}
