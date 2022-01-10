package com.alchcalc;

public class Ingredient {
    private String name = "";
    private Boolean isEnabled = true;
    private Boolean isAdder = false;
    private Double DH = 0.0;
    private Double DHM = 0.0;
    private Double DP = 0.0;
    private Double DPM = 0.0;
    private Integer quantityInPotion = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAdder() {
        return isAdder;
    }

    public void setAdder(Boolean adder) {
        isAdder = adder;
    }

    public Double getDH() {
        return DH;
    }

    public void setDH(Double DH) {
        this.DH = DH;
    }

    public Double getDHM() {
        return DHM;
    }

    public void setDHM(Double DHM) {
        this.DHM = DHM;
    }

    public Double getDP() {
        return DP;
    }

    public void setDP(Double DP) {
        this.DP = DP;
    }

    public Double getDPM() {
        return DPM;
    }

    public void setDPM(Double DPM) {
        this.DPM = DPM;
    }

    public Integer getQuantityInPotion() {
        return quantityInPotion;
    }

    public void setQuantityInPotion(Integer quantityInPotion) {
        this.quantityInPotion = quantityInPotion;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }
}
