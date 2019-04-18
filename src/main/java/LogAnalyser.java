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
import java.sql.*;
import java.util.HashMap;

public class LogAnalyser {

    static Connection connection;
    static HashMap<String, LogModel> map = new HashMap<>();

    public static void main(String[] args) {

        setupDb();
        logEvents(args[0]);
    }

    private static void logEvents(String logLocation) {
        try {
            Gson gson = new GsonBuilder().create();
            Reader r = new InputStreamReader(Files.newInputStream(Paths.get(logLocation)), StandardCharsets.UTF_8);
            JsonStreamParser p = new JsonStreamParser(r);
            String insert = "INSERT INTO Event (EventId, Duration, Type, Host, Alert) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement insertEvent = connection.prepareStatement(insert);

            while (p.hasNext()) {
                JsonElement e = p.next();
                if (e.isJsonObject()) {
                    LogModel m = gson.fromJson(e, LogModel.class);
                    if (map.containsKey(m.getId())) {
                        insertEvent.setString(1, m.getId());
                        insertEvent.setString(3, m.getType());
                        insertEvent.setString(4, m.getHost());

                        Long eventTime = calculateEventTime(m, map.get(m.getId()));
                        insertEvent.setLong(2, eventTime);
                        insertEvent.setBoolean(5, eventTime > 4);
                        insertEvent.execute();
                        map.remove(m.getId());
                    } else {
                        map.put(m.getId(), m);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void setupDb() {
        try {
            System.setProperty("textdb.allow_full_path", "true");
            String logsFile = System.getProperty("user.dir") + "\\EventsLog";
            connection = DriverManager.getConnection(
                    "jdbc:hsqldb:file:/opt/db/testdb;", "SA", "");
            Statement statement = connection.createStatement();
            statement.executeQuery("CREATE TEXT TABLE Event (EventId VARCHAR(255), Duration BIGINT, Type VARCHAR(255), Host VARCHAR(255), Alert BOOLEAN)");
            statement.executeQuery("SET TABLE Event SOURCE \"" + logsFile + "\"");
            statement.closeOnCompletion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static Long calculateEventTime(LogModel one, LogModel two) {
        return Math.abs(one.getTimestamp() - two.getTimestamp());
    }


}


