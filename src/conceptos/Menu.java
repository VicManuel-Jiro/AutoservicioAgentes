
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conceptos;

import jade.content.Concept;
import java.util.ArrayList;

/**
 *
 * @author maria
 */
public class Menu implements Concept {

    private ArrayList<Integer> precio;
    private ArrayList<String> nombre;
    private int precioMenu;
    private ArrayList<Integer> numero;
    private int numeroMenu;
    private String menuCompleto = "*****MENU COMPLETO*****\n\n"
            + "*****Comidas*****\n"
            + "1. Consomé de arroz\n"
            + "2. Mole verde\n"
            + "3. Mole rojo\n"
            + "4. Enfrijoladas\n"
            + "5. Enmoladas\n"
            + "6. Sopes\n"
            + "7. Pozole\n"
            + "8. Quesadillas\n"
            + "9. Pambazos\n"
            + "10. Enchiladas\n"
            + "11. Chilquiles\n"
            + "12. Tostadas\n"
            + "*****Bebidas*****\n"
            + "13. Refresco\n"
            + "14. Agua de sabor\n"
            + "15. Café\n"
            + "16. Jugo\n"
            + "17. Té\n"
            + "18. Cerveza\n"
            + "*****Postres*****\n"
            + "19. Arroz con leche\n"
            + "20. Flan napolitano\n"
            + "21. Rebanada de pastel\n";

    public ArrayList<Integer> getPrecio() {
        return precio;
    }

    public void setPrecio(ArrayList<Integer> precio) {
        this.precio = precio;
    }

    public ArrayList<Integer> getNumero() {
        return numero;
    }

    public void setNumero(ArrayList<Integer> numero) {
        this.numero = numero;
    }

    public String getMenuCompleto() {
        return menuCompleto;
    }

    public void setMenuCompleto(String menuCompleto) {
        this.menuCompleto = menuCompleto;
    }

    public int getPrecioMenu() {

        return precioMenu;
    }

    public void setPrecioMenu(int precio) {
        this.precioMenu = precio;
    }

    public int getNumeroMenu() {

        return numeroMenu;
    }

    public void setNumeroMenu(int numero) {
        this.numeroMenu = numero;
    }

    public ArrayList<String> getNombre() {
        return nombre;
    }

    public void setNombre(ArrayList<String> nombre) {
        this.nombre = nombre;
    }

    public String getNombre(int index) {
        nombre = new ArrayList();
        nombre.add("Consomé de arroz");
        nombre.add("Mole verde");
        nombre.add("Mole rojo");
        nombre.add("Enfrijoladas");
        nombre.add("Enmoladas");
        nombre.add("Sopes");
        nombre.add("Pozole");
        nombre.add("Quesadillas");
        nombre.add("Pambazos");
        nombre.add("Enchiladas");
        nombre.add("Chilquiles");
        nombre.add("Tostadas");
        nombre.add("Refresco");
        nombre.add("Agua de sabor");
        nombre.add("Café");
        nombre.add("Jugo");
        nombre.add("Té");
        nombre.add("Cerveza");
        nombre.add("Arroz con leche");
        nombre.add("Flan napolitano");
        nombre.add("Rebanada de pastel");
        String n = nombre.get(index);
        return n;
    }

    public int getIndex(int id) {
        numero = new ArrayList();
        numero.add(1);
        numero.add(2);
        numero.add(3);
        numero.add(4);
        numero.add(5);
        numero.add(6);
        numero.add(7);
        numero.add(8);
        numero.add(9);
        numero.add(10);
        numero.add(11);
        numero.add(12);
        numero.add(13);
        numero.add(14);
        numero.add(15);
        numero.add(16);
        numero.add(17);
        numero.add(18);
        numero.add(19);
        numero.add(20);
        numero.add(21);
        int index = 0;
        index = numero.indexOf(id);
        return index;
    }

    public int getCosto(int index) {
        precio = new ArrayList();
        precio.add(30); 
        precio.add(50);
        precio.add(50);
        precio.add(45); 
        precio.add(45);
        precio.add(30);
        precio.add(60);
        precio.add(20); 
        precio.add(40);
        precio.add(50);
        precio.add(50); 
        precio.add(35); 
        precio.add(17);
        precio.add(17);
        precio.add(17);
        precio.add(20); 
        precio.add(15); 
        precio.add(40); 
        precio.add(20); 
        precio.add(20); 
        precio.add(30); 
        int costo = 0;
        costo = precio.get(index);
        return costo;
    }
}
