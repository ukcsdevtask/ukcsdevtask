
public class LogModel {
    private String id;
    private String state;
    private String type;
    private String host;
    private Long timestamp;

    public String getId() {
        return this.id;
    }

    public String getHost() {
        return host == null ? "" : host;
    }

    public String getType() {
        return type == null ? "" : type;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    @Override
    public String toString() {
        return id + " " + state + " " + timestamp + " " + type + " " + host;
    }

}
