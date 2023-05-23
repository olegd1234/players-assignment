package com.assignment.players.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlayersServiceTest {

    private final static String FIRST_RECORD_ID = "aardsda01";
    private final static String LAST_RECORD_ID = "adairbi99";
    private final static String SOME_RECORD_ID = "acostma01";

    private final static int NUM_OF_RECORDS_IN_TEST_FILE = 57;
    private static final String PLAYER_ID_PROPERTY_NAME = "playerID";

    private static PlayersService service;

    @BeforeAll
    static void init() throws IOException {
        service = new PlayersService("classpath:test_data/player_test.csv");
        service.init();
    }

    @Test
    void getPlayerByIdOk() {
        Object playerData = service.getPlayer(SOME_RECORD_ID);
        assertNotNull(playerData);
    }


    @Test
    void getPlayerByIdLastInFileOk() throws JsonProcessingException {
        Object playerData = service.getPlayer(LAST_RECORD_ID);
        assertNotNull(playerData);
        ObjectMapper mapper = new ObjectMapper();
        String playerString = mapper.writeValueAsString(playerData);
        assertTrue(playerString.contains("Marion Danne"));
    }

    @Test
    void getPlayerByIdNotFound() {
        Object playerData = service.getPlayer("foo");
        assertNull(playerData);
    }

    @Test
    void getPlayersAll() {
        List<Map<String, String>> playersData = (List<Map<String, String>>)service.getPlayers(NUM_OF_RECORDS_IN_TEST_FILE, 1);
        assertEquals(NUM_OF_RECORDS_IN_TEST_FILE, playersData.size());
        assertEquals(FIRST_RECORD_ID, playersData.get(0).get(PLAYER_ID_PROPERTY_NAME));
        assertEquals(LAST_RECORD_ID, playersData.get(playersData.size() - 1).get(PLAYER_ID_PROPERTY_NAME));
    }


    @Test
    void getPlayersCertainPage() {
        int pageSize = 10;
        int pageNum = 3;
        List<Map<String, String>> allPlayersData = (List<Map<String, String>>)service.getPlayers(NUM_OF_RECORDS_IN_TEST_FILE, 1);
        List<Map<String, String>> pagePlayersData = (List<Map<String, String>>)service.getPlayers(pageSize, pageNum);
        assertEquals(10, pagePlayersData.size());
        assertEquals(allPlayersData.get(pageSize*(pageNum-1)).get(PLAYER_ID_PROPERTY_NAME),
                pagePlayersData.get(0).get(PLAYER_ID_PROPERTY_NAME));
        assertEquals(allPlayersData.get(pageSize*pageNum - 1).get(PLAYER_ID_PROPERTY_NAME),
                pagePlayersData.get(pagePlayersData.size() - 1).get(PLAYER_ID_PROPERTY_NAME));
    }
}