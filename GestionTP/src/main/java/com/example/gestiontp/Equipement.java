package com.example.gestiontp;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Equipement {
    private final StringProperty code = new SimpleStringProperty("");
    private final StringProperty marque = new SimpleStringProperty("");
    private final StringProperty ram = new SimpleStringProperty("");
    private final StringProperty se = new SimpleStringProperty("");
    private final StringProperty logiciels = new SimpleStringProperty("");
    private final StringProperty disque = new SimpleStringProperty("");
    private final StringProperty cpu = new SimpleStringProperty("");

    // Getters pour propriétés
    public StringProperty codeProperty() { return code; }
    public StringProperty marqueProperty() { return marque; }
    public StringProperty ramProperty() { return ram; }
    public StringProperty seProperty() { return se; }
    public StringProperty logicielsProperty() { return logiciels; }
    public StringProperty disqueProperty() { return disque; }
    public StringProperty cpuProperty() { return cpu; }
    private final StringProperty salleId = new SimpleStringProperty("");



    public void setCode(String code) { this.code.set(code); }
    public void setMarque(String marque) { this.marque.set(marque); }
    public void setRam(String ram) { this.ram.set(ram); }
    public void setSe(String se) { this.se.set(se); }
    public void setDisque(String disque) { this.disque.set(disque); }
    public void setCpu(String cpu) { this.cpu.set(cpu); }
    public StringProperty salleIdProperty() { return salleId; }
    public void setSalleId(String salleId) { this.salleId.set(salleId); }


}

