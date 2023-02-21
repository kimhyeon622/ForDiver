package com.first.project.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class ChatHandler extends TextWebSocketHandler {

    private static List<WebSocketSession> list = new ArrayList<>();
    private static List<String> userName = new ArrayList<>();
    private static List<String> userPro = new ArrayList<>();
    private static List<String> userCate = new ArrayList<>();


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload : " + payload);

        if(payload.equals("_")){
            session.sendMessage(new TextMessage("1: " +userName.toString().substring(1,userName.toString().length()-1)));
            session.sendMessage(new TextMessage("2: " +userPro.toString().substring(1,userPro.toString().length()-1)));
            session.sendMessage(new TextMessage("3: " +userCate.toString().substring(1,userCate.toString().length()-1)));
            return;
        }
        list.add(session);
        String aaa = message.getPayload();
        String aa[] =  aaa.split(",");
        userName.add(aa[0]);
        userPro.add(aa[1]);
        userCate.add(aa[2]);


        System.out.println(userName);


        for(WebSocketSession sess: list) {
            sess.sendMessage(message);
        }
    }

    /* Client가 접속 시 호출되는 메서드 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        log.info(session + " 클라이언트 접속");
    }

    /* Client가 접속 해제 시 호출되는 메서드드 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        log.info(session + " 클라이언트 접속 해제");

        int a =list.indexOf(session);
        try{
            userName.remove(a);
            userPro.remove(a);
            userCate.remove(a);
        }catch(Exception e){
        }

        list.remove(session);




    }



}
