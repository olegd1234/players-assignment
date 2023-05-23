package com.assignment.players.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.*;

@Service
public class PlayersService {

    private final String csvFileLocation;
    private Map<String, Integer> idToOrderMap = new HashMap<>();
    private Map<Integer, String[]> orderToPropertiesValues = new HashMap<>();

    private String[] propertiesNames;

    @Autowired
    public PlayersService(@Value("${players.csv.file.location}") String csvFileLocation) {
        this.csvFileLocation = csvFileLocation;
    }

    @PostConstruct
    public void init() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try (BufferedReader reader = new BufferedReader(new FileReader(ResourceUtils.getFile(csvFileLocation)))) {

            String line = reader.readLine();
            propertiesNames = line.split(",");
            int count = 0;
            line = reader.readLine();

            while (line != null) {
                String[] propertiesValues = line.split(",");
                if (propertiesValues.length == propertiesNames.length) {
                    String id = propertiesValues[0];
                    idToOrderMap.put(id, count);
                    orderToPropertiesValues.put(count, propertiesValues);
                    count++;
                } //TODO else log error
                line = reader.readLine();
            }
        }
    }

    public Object getPlayer(String id) {

        Integer order = idToOrderMap.get(id);
        if (order == null) {
            return null;
        }

        Map<String, String> ret = getPlayerProperties(order);
        return ret;
    }

    public Object getPlayers(int pageSize, int pageNum) {

        List<Map<String, String>> ret = new ArrayList<>();
        for (int i = pageSize*(pageNum - 1); i < pageSize*pageNum; i++) { //no need to check limits here
            Map<String, String> playerProperties = getPlayerProperties(i);
            if (playerProperties != null) {
                ret.add(playerProperties);
            }
        }
        return ret;
    }

    private Map<String, String> getPlayerProperties(int order) {

        Map<String, String> ret = new LinkedHashMap<>();
        String[] propertiesValues = orderToPropertiesValues.get(order);
        if (propertiesValues == null) {
            return null;
        }
        for (int i=0; i < propertiesNames.length; i++) {
            ret.put(propertiesNames[i], propertiesValues[i]);
        }
        return ret;
    }

}
