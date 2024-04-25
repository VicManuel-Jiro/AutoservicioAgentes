/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conceptos;

import jade.content.Concept;

/**
 *
 * @author maria
 */
public class Cliente implements Concept {
    
private String nombreCliente;
private int numCliente;

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public int getNumeroCliente() {
        return numCliente;
    }

    public void setNumeroCliente(int numCliente) {
        this.numCliente = numCliente;
    }
    
    
}
