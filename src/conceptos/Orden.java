/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conceptos;

import java.util.ArrayList;

/**
 *
 * @author maria
 */
public class Orden {
    private Cliente clienteOrden;
    private String pedido;
    private float total;

    public String getPedido() {
        return pedido;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public Cliente getClienteOrden() {
        return clienteOrden;
    }

    public void setClienteOrden(Cliente clienteOrden) {
        this.clienteOrden = clienteOrden;
    }



}
