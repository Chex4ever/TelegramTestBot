package pro.sky.exever.telegramtestbot;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "notification_task")
public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "note")
    private String note;
    @Column(name = "date_to_send")
    private LocalDateTime dateToSend;
    @Column(name = "date_sent")
    private LocalDateTime dateSent;
    @Column(name = "date_created")
    private LocalDateTime dateCreated;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private NotificationTaskStatus status = NotificationTaskStatus.SCHEDULED;

    public NotificationTask() {}
    public NotificationTask(String text, LocalDateTime dateToSend) {
        this.note = text;
        this.dateToSend = dateToSend;
        this.dateCreated = LocalDateTime.now();
    }

    public void setAsSent(){
        status = NotificationTaskStatus.SENT;
        dateSent=LocalDateTime.now();
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }



    public long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getDateToSend() {
        return dateToSend;
    }

    public void setdateToSend(LocalDateTime date) {
        this.dateToSend = date;
    }

    public LocalDateTime getDateSent() {
        return dateSent;
    }

    public void setDateSent(LocalDateTime dateSent) {
        this.dateSent = dateSent;
    }

    public NotificationTaskStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(chatId, that.chatId)
                && Objects.equals(note, that.note)
                && Objects.equals(dateToSend, that.dateToSend)
                && Objects.equals(dateSent, that.dateSent)
                && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, note, dateToSend, dateSent, status);
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", note='" + note + '\'' +
                ", dateToSend=" + dateToSend +
                ", dateSent=" + dateSent +
                ", status=" + status +
                '}';
    }
}