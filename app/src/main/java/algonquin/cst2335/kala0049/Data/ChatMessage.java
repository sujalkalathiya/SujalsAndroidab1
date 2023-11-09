package algonquin.cst2335.kala0049.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {

    @PrimaryKey(autoGenerate=true)
    public int id;

    @ColumnInfo(name="message")
    String message;

    @ColumnInfo(name="timeSent")
    String timeSent;

    @ColumnInfo(name="isSentButton")
    boolean isSentButton;

    public String getMessage() {
        return message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public boolean isSentButton() {
        return isSentButton;
    }

    @Ignore
    public ChatMessage() {
    }

    public ChatMessage(String message, String timeSent, boolean isSentButton) {
        this.message = message;
        this.timeSent = timeSent;
        this.isSentButton = isSentButton;
    }
}
