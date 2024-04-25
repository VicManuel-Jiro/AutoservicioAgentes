/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package predicados;

import conceptos.Orden;
import jade.content.Predicate;

/**
 *
 * @author maria
 */
public class OfertaOrden implements Predicate {
    
    private Orden orden;

    public Orden getOforden() {
        return orden;
    }

    public void setOforden(Orden orden) {
        this.orden = orden;
    }
    
}
