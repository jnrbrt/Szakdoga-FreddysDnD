package DND.DND.ChatRoom;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Controller
public class WebsocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebsocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        // Üzenet küldése a szoba topic-jába, az üzenet szoba azonosítójának megfelelően
        messagingTemplate.convertAndSend("/topic/" + chatMessage.getRoomId(), chatMessage);
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Felhasználó hozzáadása egy szobához
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        // Felhasználó üzenetének küldése a szobához
        messagingTemplate.convertAndSend("/topic/" + chatMessage.getRoomId(), chatMessage);
    }
}