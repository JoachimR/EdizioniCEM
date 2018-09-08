package de.reiss.edizioni.architecture;


import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class EdizioniCEMDateJsonAdapter extends JsonAdapter<Date> {

    private static final String format = "yyyy-MM-dd HH:mm:ss";
    private static final String timeZone = "GMT";

    private final DateFormat dateFormat;

    public EdizioniCEMDateJsonAdapter() {
        dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
    }

    @Override
    public synchronized Date fromJson(JsonReader reader) throws IOException {
        String string = reader.nextString();
        try {
            return dateFormat.parse(string);
        } catch (ParseException e) {
            throw new IOException("could not parse string");
        }
    }

    @Override
    public synchronized void toJson(JsonWriter writer, Date value) throws IOException {
        String string = dateFormat.format(value);
        writer.value(string);
    }
}
