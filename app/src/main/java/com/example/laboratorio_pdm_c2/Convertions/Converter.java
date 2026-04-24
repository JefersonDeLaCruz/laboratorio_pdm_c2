package com.example.laboratorio_pdm_c2.Convertions;

import androidx.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Converter {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @TypeConverter
    public static String fromDate(Date date){
        return date == null ? null : FORMAT.format(date);
    }

    @TypeConverter
    public static Date fromDate(String date){
        try {
            return date == null ? null : FORMAT.parse(date);
        } catch (Exception e) {
            return null;
        }
    }
}
