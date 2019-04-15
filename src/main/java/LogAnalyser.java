import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class LogAnalyser {

    static HashMap<String, LogModel> map = new HashMap<>();

    public static void main(String[] args) {

        try {


            Gson gson = new GsonBuilder().create();
            Reader r = new InputStreamReader(Files.newInputStream(Paths.get("src/Log1.json")), StandardCharsets.UTF_8);
            JsonStreamParser p = new JsonStreamParser(r);

            while (p.hasNext()) {
                JsonElement e = p.next();
                if (e.isJsonObject()) {
                    LogModel m = gson.fromJson(e, LogModel.class);
                    if (map.containsKey(m.getId())) {
                        System.out.println(calculateEventTime(m, map.get(m.getId())));
                        calculateEventTime(m, map.get(m.getId()));
                    } else {
                        map.put(m.getId(), m);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static Long calculateEventTime(LogModel one, LogModel two) {
        map.remove(one.getId());
        return Math.abs(one.getTimestamp() - two.getTimestamp());
    }


}


