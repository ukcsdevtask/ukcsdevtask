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

    static HashMap<Integer, LogModel> map = new HashMap<>();

    public static void main(String[] args) {

        try {


            Gson gson = new GsonBuilder().create();
            Reader r = new InputStreamReader(Files.newInputStream(Paths.get("src/Log1.json")), StandardCharsets.UTF_8);
            JsonStreamParser p = new JsonStreamParser(r);

            while (p.hasNext()) {
                JsonElement e = p.next();
                if (e.isJsonObject()) {
                    LogModel m = gson.fromJson(e, LogModel.class);
//                    System.out.println(m.toString());
//                    System.out.println(m.hashCode());
                    if (map.containsKey(m.hashCode())) {
                        System.out.println(calculateEventTime(m, map.get(m.hashCode())));
                        calculateEventTime(m, map.get(m.hashCode()));
                    } else {
                        map.put(m.hashCode(), m);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static Long calculateEventTime(LogModel one, LogModel two) {
        return Math.abs(one.getTimestamp() - two.getTimestamp());
    }


}


