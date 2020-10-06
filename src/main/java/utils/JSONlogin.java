package utils;

import org.json.simple.JSONObject;

public class JSONlogin {
    public static void main(String[] args) {
        JSONObject login = new JSONObject();
        login.put("username", "webinar5");
        login.put("password", "webinar5");
        login.toJSONString();

        System.out.println(login.toString());
    }
}
