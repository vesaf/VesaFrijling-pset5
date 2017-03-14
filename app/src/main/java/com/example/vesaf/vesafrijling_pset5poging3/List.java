package com.example.vesaf.vesafrijling_pset5poging3;

import java.io.Serializable;

/**
 * Created by vesaf on 3/13/2017.
 */

public class List implements Serializable {
    private String name;
    private int id;

    public List(String listName, int listId) {
        name = listName;
        id = listId;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setId(int newId) {
        id = newId;
    }
}
