/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontologia;

/**
 *
 * @author Luis E
 */

import conceptos.Cliente;
import conceptos.Menu;
import predicados.OfertaOrden;
import conceptos.Orden;
import jade.content.onto.*;
import jade.content.schema.*;
 
public class RestauranteOntologia extends Ontology {
   // Nombre de la ontología
   public static final String ONTOLOGY_NAME = "ontología del restaurante";
 
  // Vocabulario de la ontología que van a manejar los agentes

  public static final String MENU = "Menu";
  public static final String MENU_COMPLETO = "menuCompleto";
  public static final String MENU_NUMERO = "numeroMenu";
  public static final String MENU_PRECIO = "precioMenu";
  
  public static final String CLIENTE = "Cliente";
  public static final String CLIENTE_NOMBRE = "nombreCliente";
  public static final String CLIENTE_NUMERO = "numeroCliente";
  
  public static final String ORDEN = "Orden";
  public static final String ORDEN_CLIENTE = "clienteOrden";
  public static final String ORDEN_PEDIDO = "pedido";
  public static final String ORDEN_TOTAL = "total";
  
 // public static final String OFERTA = "Oferta";
  
 // public static final String OFERTA_MENU_b = "ofmenub"; 
  //public static final String OFERTA_MENU_po = "ofmenupo"; 
  
  public static final String OFERTA_ORDEN = "ofertaOrden";
  public static final String OFERTA_ORDEN_o = "oforden"; 
  
  public static final String ORDENAR = "Ordenar";
  public static final String ORDENAR_ORDEN = "orden";
 
  // Instancia de la ontología (que será única)
  private static Ontology instancia = new RestauranteOntologia();
 
  // Método para acceder a la instancia de la ontología
  public static Ontology getInstance() {
     return instancia;
   }
 
   // Constructor privado
   private RestauranteOntologia() {

     super(ONTOLOGY_NAME, BasicOntology.getInstance());
 
     try {
       // Añade los elementos
       add(new ConceptSchema(MENU), Menu.class);
       add(new ConceptSchema(CLIENTE), Cliente.class);
       add(new ConceptSchema(ORDEN), Orden.class);
       add(new PredicateSchema(OFERTA_ORDEN), OfertaOrden.class);
       
       // Estructura del esquema para el concepto MENU
      ConceptSchema csMenu = (ConceptSchema) getSchema(MENU);
      csMenu.add(MENU_COMPLETO, (PrimitiveSchema) getSchema(BasicOntology.STRING));
      csMenu.add(MENU_PRECIO, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
      csMenu.add(MENU_NUMERO, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
       
       // Estructura del esquema para el concepto CLIENTE
       ConceptSchema csCliente = (ConceptSchema) getSchema(CLIENTE);
       csCliente.add(CLIENTE_NOMBRE, (PrimitiveSchema) getSchema(BasicOntology.STRING));
       csCliente.add(CLIENTE_NUMERO, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
       
       // Estructura del esquema para el concepto ORDEN
       ConceptSchema csOrden = (ConceptSchema) getSchema(ORDEN);
       csOrden.add(ORDEN_PEDIDO, (PrimitiveSchema) getSchema(BasicOntology.STRING));
       csOrden.add(ORDEN_CLIENTE, (ConceptSchema) getSchema(CLIENTE));//ORDEN_TOTAL
       csOrden.add(ORDEN_TOTAL, (PrimitiveSchema) getSchema(BasicOntology.FLOAT));
       // Estructura del esquema para el predicado OFERTA_CUENTA
       
       // Estructura del esquema para el predicado OFERTA_ORDEN
       PredicateSchema psOrden = (PredicateSchema) getSchema(OFERTA_ORDEN);
       psOrden.add(OFERTA_ORDEN_o, (ConceptSchema) getSchema(ORDEN));
       

     }
     catch (Exception oe) {
       oe.printStackTrace();
     }
   }
}
 
