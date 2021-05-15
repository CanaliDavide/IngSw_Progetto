package it.polimi.ingsw.server;

import it.polimi.ingsw.client.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    @Test
    void startServer() {
        String[] args = new String[]{"lupo", "pollo"};
        new Thread(() -> Server.main(args)).start();
        new Thread(() -> Client.main(args)).start();
        new Thread(() -> Client.main(args)).start();
    }
}