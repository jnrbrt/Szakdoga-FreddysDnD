package DND.DND.ChatRoom;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    private String content;  // Az üzenet tartalma
    private String sender;   // A felhasználó neve
    private MessageType type; // Üzenet típusa (pl. szöveg, belépés)
    private String roomId;   // A szoba azonosítója, amelyhez az üzenet tartozik
}
