package com.autoever.apay.utils;

import com.autoever.apay.models.Terms;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

    public static String toJSon(Terms terms) {
        try {

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("term01", terms.getTerms().get("term01"));
            jsonObj.put("term02", terms.getTerms().get("term02"));
            jsonObj.put("term03", terms.getTerms().get("term03"));
            jsonObj.put("term04", terms.getTerms().get("term04"));

            return jsonObj.toString();

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return null;

    }
}
