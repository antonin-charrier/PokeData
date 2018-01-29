package fr.iti.pokedata.Models;

/**
 * Created by Antonin on 29/01/2018.
 */

public class Pokemon {

    private String name1;
    private String name2;
    private String id;
    private String type1;
    private String type2;

    public Pokemon(String name1, String name2, String id, String type1, String type2) {
        this.name1 = name1;
        this.name2 = name2;
        this.id = id;
        this.type1 = type1;
        this.type2 = type2;
    }

    public String getName1() {
        return name1;
    }
    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }
    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getType1() {
        return type1;
    }
    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }
    public void setType2(String type2) {
        this.type2 = type2;
    }
}
