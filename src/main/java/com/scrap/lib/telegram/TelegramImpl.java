package com.scrap.lib.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public class TelegramImpl extends TelegramLongPollingBot {

    private final String telegram_bot_token;
    private final String telegram_notify_user;
    private final String telegram_bot_username;

    public TelegramImpl(String telegram_bot_token, String telegram_notify_user, String telegram_bot_username) {
        this.telegram_bot_token = telegram_bot_token;
        this.telegram_notify_user = telegram_notify_user;
        this.telegram_bot_username = telegram_bot_username;
    }

    @Override
    public void onUpdateReceived(Update update) {
    }

    public void sendMessageToUserId(String msg) throws Exception {
        sendMessageToUserId(msg, null);
    }


    public void sendContact(String phoneNumber, String firstName) throws Exception {
        SendContact contact = new SendContact();
        contact.setPhoneNumber(phoneNumber);
        contact.setChatId(this.telegram_notify_user);
        contact.setFirstName(firstName);
        execute(contact);
    }

    public void sendMessageToUserId(String msg, ReplyKeyboard replyMarkup) throws Exception {
        SendMessage msgObj = new SendMessage(this.telegram_notify_user, msg);
        msgObj.enableMarkdownV2(true);
        msgObj.setParseMode("HTML");
        if (replyMarkup != null) {
            msgObj.setReplyMarkup(replyMarkup);
        }
        execute(msgObj);
    }

    public void sendLocation(Double latitude, Double longitude) throws Exception {
        SendLocation location = new SendLocation(this.telegram_notify_user, latitude, longitude);
        execute(location);
    }

    @Override
    public String getBotUsername() {
        return this.telegram_bot_username;
    }

    @Override
    public String getBotToken() {
        return this.telegram_bot_token;
    }

}
