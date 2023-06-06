package com.example.communication.whatsapp.services.impl;

import com.example.communication.shared.persistance.entities.Sessions;
import com.example.communication.menu.persistence.models.Menu;
import com.example.communication.menu.services.MenuService;
import com.example.communication.shared.persistance.models.PostEntity;
import com.example.communication.shared.persistance.models.ResponseModel;
import com.example.communication.shared.services.RestEntity;
import com.example.communication.shared.services.SessionService;
import com.example.communication.whatsapp.persistance.entities.Conversation;
import com.example.communication.whatsapp.persistance.models.*;
import com.example.communication.whatsapp.persistance.repositories.ConversationRepository;
import com.example.communication.whatsapp.services.MetaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@Service
@EnableAsync
public class MetaClient<T> implements MetaService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String faceBookResourceUrl = "https://graph.facebook.com/v16.0/";
    private static final String instanceId = "109266945127952";
    ObjectMapper objectMapper=new ObjectMapper();
    ModelMapper modelMapper=new ModelMapper();
    @Autowired
    ConversationRepository conversationRepository;
    @Autowired
    MenuService menuService;
    @Autowired
    SessionService sessionService;
    @Autowired
    RestEntity restEntity;
    public void webHook(String whatsAppMessage){
        try {
            WebHook webHook=objectMapper.readValue(whatsAppMessage, WebHook.class);
            Entry entry=webHook.getEntry().get(0);
            Contact contact = null;
            Status status = null;

            List<Change> changes = entry.getChanges();
            if (!changes.isEmpty()) {
                Change firstChange = changes.get(0);
                Value value = firstChange.getValue();

                List<Contact> contacts = value.getContacts();
                if (contacts!=null) {
                    contact = contacts.get(0);
                    String conversationId=entry.getId();
                    String name = contact.getProfile().getName();
                    String waId = contact.getWa_id();
                    Message message = entry.getChanges().get(0).getValue().getMessages().get(0);
                    String id = message.getId();
                    String textBody = message.getText().getBody();
                    //Process the message and show menus
                    //TODO: create save and process event here
                    this.saveMessage(waId,name,textBody,conversationId,id);
                    this.userInteraction(waId,textBody,name);
                }

                List<Status> statuses = value.getStatuses();
                if (statuses!=null) {
                    status = statuses.get(0);
                    //TODO:process status
                    log.info("Message status received {}",status.getStatus());
                }
            }


        }catch (Exception e){
            log.error("Error processing the webhook {}",e.getMessage());
        }
    }
    @Async
    private void userInteraction(String phone,String message,String user){
        String footer="\n00.Home \n000.logout";
        if (message.equalsIgnoreCase("000")){
            footer="";
        }

        String displayString=this.getMenu(phone,message,user)+footer;
        //save message and send response to user
        //TODO:save the response
        this.sendWhatsappMessage(this.instanceId,phone,displayString);
    }
    private String getMenu(String phone,String command,String user){
        Sessions session=new Sessions();
        Menu menu=null;
        ResponseModel activeSession=sessionService.getMostRecentSession(phone);
        if (activeSession.getStatus().equalsIgnoreCase("200")){
            //use the current session
            session=modelMapper.map(activeSession.getBody(),Sessions.class);
            ResponseModel menuResponse=menuService.getOption(session.getId(),command);
            if (menuResponse.getStatus().equalsIgnoreCase("200")){
                menu=modelMapper.map(menuResponse.getBody(), Menu.class);
                //return menu value
                return menu.getMenuValue();
            }
            //return the message
            return menuResponse.getMessage();
        }else {
            //create a new session
            session.setPhone(phone);
            sessionService.createSession(session);
            //show default menu
            return "Welcome "+user+", Choose a menu to proceed \n"+modelMapper.map(menuService.getDefaultMenu().getBody(), Menu.class).getMenuValue();
        }
    }
   @Async
   private void saveMessage(String phone,String name,String message,String conversationId,String messageId){
       Conversation conversation=new Conversation();
       conversation.setMessage(message);
       conversation.setConversationId(conversationId);
       conversation.setName(name);
       conversation.setPhone(phone);
       conversation.setResponse(false);
       conversation.setExternalId(messageId);
       conversationRepository.save(conversation);
   }
   private String getToken(){
        //TODO:meta authentication to be added here
        return "EAAPeqX2f6msBAC3bhCbavD7QpDz4aFZCZAu3yuCaCIe0c2jA9VfaagqmyzSNOoWnkiTsgugPdlH1mHZCbMh6Q4DV0MzVpxAkAFJOad4czIL8gzGZAHxbHx7KwHILi99f0LmhydnkMPKTgAYchKZCNAatExNbwxWCZCRUbjUZA1uXlDFuYzDuArmKgtZC5dmmfAHJeHEDjIjE2wp3k1BnPX1tS1tleM8QMvoZD";
   }
   private void sendWhatsappMessage(String instanceId,String to,String message){
       String token=this.getToken();
       PostEntity postEntity=new PostEntity();
       postEntity.setUrl(faceBookResourceUrl);
       postEntity.setEndpoint(instanceId+"/messages");

       // Set the request headers
       HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON);
       headers.set("Authorization", "Bearer "+token);
       postEntity.setHeaders(headers);
       //create http request body
       RequestText text=new RequestText();
       text.setBody(message);
       WhatsAppRequest requestBody=new WhatsAppRequest();
       requestBody.setTo(to);
       requestBody.setMessaging_product("whatsapp");
       requestBody.setPreview_url(false);
       requestBody.setRecipient_type("individual");
       requestBody.setText(text);
       requestBody.setType("text");
       /**The below is a sample body utilizing the meta templates
        * String requestBody =
        * "{\"messaging_product\": \"whatsapp\", \"to\": \"254743696250\", \"type\": \"template\", \"template\": { \"name\": \"hello_world\", \"language\": { \"code\": \"en_US\" } } }";
        *
        */
       postEntity.setBody(requestBody);
       ResponseEntity<T> response=restEntity.post(postEntity);
       // Print the response
       log.info("Message sent with status code: {} ,message: {}",response.getStatusCode(),response.getBody());
   }
}
