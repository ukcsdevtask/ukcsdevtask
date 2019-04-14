import java.util.Arrays;

public class LogModel {
    private String id;
    private String state;
    private String type;
    private String host;
    private Long timestamp;

/*
    public LogModel(String id, String state, String type, String host, Long ts) {
        this.id = id;
        finishEvent = (state.equals("STARTED"));
        this.type = type;
        this.host = host;
        this.timestamp = ts;
    }*/

    public String getId() {
        return this.id;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    @Override
    public String toString() {
        return id + " " + state + " " + timestamp + " " + type + " " + host;
    }

    public int hashCode() {
        return Arrays.hashCode(id.toCharArray());
    }

    public boolean equals(LogModel q) {
        return q.getId().equals(this.getId());
    }
}
