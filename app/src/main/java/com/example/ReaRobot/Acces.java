package com.example.ReaRobot;

import java.util.Map;

public class Acces {
    private String fullName;
    private String dateHeure;
    private Map<String, Object> accessData;

    public Acces() {
    }

    public Acces(String fullName, String dateHeure) {
        this.fullName = fullName;
        this.dateHeure = dateHeure;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(String dateHeure) {
        this.dateHeure = dateHeure;
    }
}
