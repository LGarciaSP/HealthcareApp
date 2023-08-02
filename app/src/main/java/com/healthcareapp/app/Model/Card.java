package com.healthcareapp.app.Model;

public class Card {

    private String cardTitle="Hello";
    private String cardURL="";
    private String cardImageURL="";

    public Card(String cardTitle,String cardURL,String cardImageURL){

        this.cardTitle = cardTitle;
        this.cardURL = cardURL;
        this.cardImageURL = cardImageURL;
    }
    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public String getCardURL() {
        return cardURL;
    }

    public void setCardURL(String cardURL) {
        this.cardURL = cardURL;
    }

    public String getCardImageURL() {
        return cardImageURL;
    }

    public void setCardImageURL(String cardImageURL) {
        this.cardImageURL = cardImageURL;
    }

}
