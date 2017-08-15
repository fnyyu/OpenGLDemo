package com.fny.program.opengldemo.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by cvter on 2017/7/25.
 */

public class ResourceReader {

    public static String readFileFromResource(Context context, int resourceID) {
        StringBuilder body = new StringBuilder();

        try {
            InputStream inputStream = context.getResources().openRawResource(resourceID);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String nextLine;

            while ((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine);
                body.append("\n");
            }

        } catch (IOException | Resources.NotFoundException e) {
            throw new ThrowError(e.getMessage());
        }

        return body.toString();
    }

}
