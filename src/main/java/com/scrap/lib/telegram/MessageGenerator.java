package com.scrap.lib.telegram;

import com.scrap.lib.Endpoint;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageGenerator {

    public static String generateEndpointLoading(Endpoint endpoint) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateNow = sdf.format(new Date());

        String stringBuilder =
                "<code>" + dateNow + ": Inicializando tracker " + endpoint.getName() + " ...</code>";

        return stringBuilder;
    }

    public static String generateFirstTracking(Endpoint endpoint, int numberOfIgnored) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateNow = sdf.format(new Date());

        String stringBuilder =
                "\n" +
                "<code>" + dateNow + ": He terminado el análisis del primer arranque del tracker: " + endpoint.getName() + "</code>" +
                "\n" +
                "<code>Número de anuncios ignorados: " + numberOfIgnored + "</code>" +
                "\n" +
                "<code>A partir de este momento, estoy buscando nuevas oportunidades y te avisaré de ello. ¡Activa las notificaciones!</code>";

        return stringBuilder;
    }

    public static String generateNewHome(NewHomeMessage newHome, Endpoint endpoint) {
        String stringBuilder = "\uD83C\uDFE0<b>" + newHome.getDescription() + "</b> " +
                "\n" +
                "\uD83C\uDF0E https://www." + endpoint.getType() + ".com/inmueble/" + newHome.getItem().getAds().getFirst().getAdId() + "/" +
                "\n" +
                "\uD83D\uDCB2<b>" + newHome.getItem().getAds().getFirst().getPriceText() + "</b>" +
                "\n" +
                "\uD83D\uDEAA<b>" + newHome.getRooms() + " </b>" +
                "\n" +
                "\uD83D\uDCD0<b>" + newHome.getArea() + "</b>" +
                "\n" +
                "\uD83D\uDCDE<b>" + newHome.getContactPhone() + " (" + newHome.getNameOwner() + ") </b>" +
                "\n";
        return stringBuilder;
    }

    public static ReplyKeyboard generateReplyMarkup(String urlBrowser) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton buttonOpenUrl = new InlineKeyboardButton();
        buttonOpenUrl.setText("↗\uFE0FAbrir Navegador");
        buttonOpenUrl.setUrl(urlBrowser);
        row.add(buttonOpenUrl);

        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }
}
