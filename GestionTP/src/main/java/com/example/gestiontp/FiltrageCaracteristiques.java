package com.example.gestiontp;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FiltrageCaracteristiques {
    private final StringProperty ram;
    private final StringProperty logiciels;
    private final StringProperty capaciteSalle;
    private final StringProperty autre1;
    private final StringProperty autre2;

    public FiltrageCaracteristiques(String ram, String logiciels, String capaciteSalle, String autre1, String autre2) {
        this.ram = new SimpleStringProperty(ram);
        this.logiciels = new SimpleStringProperty(logiciels);
        this.capaciteSalle = new SimpleStringProperty(capaciteSalle);
        this.autre1 = new SimpleStringProperty(autre1);
        this.autre2 = new SimpleStringProperty(autre2);
    }

    public String getRam() { return ram.get(); }
    public void setRam(String value) { ram.set(value); }
    public StringProperty ramProperty() { return ram; }

    public String getLogiciels() { return logiciels.get(); }
    public void setLogiciels(String value) { logiciels.set(value); }
    public StringProperty logicielsProperty() { return logiciels; }

    public String getCapaciteSalle() { return capaciteSalle.get(); }
    public void setCapaciteSalle(String value) { capaciteSalle.set(value); }
    public StringProperty capaciteSalleProperty() { return capaciteSalle; }
}
