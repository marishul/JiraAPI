package utils;

import org.json.simple.JSONObject;

public class JSONCommentBody {
    public static JSONObject commentBody() {
        JSONObject body = new JSONObject();
        body.put("body", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
        return body;
    }
}
