package com.codershubham.quickcarhire.ClassFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String convertToDatabaseFormat(String inputDate) {
        try {
            // Input date format
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

            // Output date format suitable for database insertion
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            // Parse the input date
            Date date = inputFormat.parse(inputDate);

            // Format the date in the desired output format
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Handle the parsing exception appropriately
        }
    }
}
