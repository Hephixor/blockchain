package server.protocol;

public class Id extends Request {
    public int id;

    public Id(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("ID/%d/", id);
    }
}
