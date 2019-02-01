package server;

import java.util.Objects;

public class IpAddress {
    private String address;
    private int port;

    /**
     * Creates an IpAdress object from a String of the form <address>:<port>
     */
    public static IpAddress fromString(String address) {
        String[] components = address.trim().split(":");
        if (components.length != 2) {
            return null;
        }
        int port;
        try {
            port = Integer.valueOf(components[1]);
        } catch (NumberFormatException e) {
            System.err.println("Error while reading ip adress <" + address + ">: " + e.getMessage());
            return null;
        }
        return new IpAddress(components[0], port);
    }

    public IpAddress(String address) {
        this(address, Server.DEFAULT_PORT);
    }

    public IpAddress(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IpAddress)) return false;
        IpAddress ipAddress = (IpAddress) o;
        return port == ipAddress.port &&
                Objects.equals(address, ipAddress.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, port);
    }

    @Override
    public String toString() {
        return String.format("%s:%d", address, port);
    }
}
