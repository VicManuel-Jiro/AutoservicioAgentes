/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agentes;

import conceptos.Cliente;
import conceptos.Menu;
import conceptos.Orden;
import gui.interfaz;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import java.io.IOException;
import jade.core.*;
import jade.core.behaviours.*;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import ontologia.RestauranteOntologia;
import predicados.OfertaOrden;

/**
 *
 * @author Jiro
 */
public class Mesero extends Agent {

    interfaz gui = new interfaz();
    //guiMENU guiM = new guiMENU();
    static Socket s;
     ObjectInputStream entra;
     ObjectOutputStream sale;
    int numcli = 0;
    OfertaOrden of = new OfertaOrden();
    Orden o = new Orden();
    ACLMessage msg2;
    public Cliente cliente = new Cliente();
    String servidor;
    AID r = new AID();
    //comportamiento de agente
    private Codec codec = new SLCodec();
    private Ontology ontologia = RestauranteOntologia.getInstance();
    MessageTemplate mt = MessageTemplate.and(
            MessageTemplate.MatchLanguage(codec.getName()),
            MessageTemplate.MatchOntology(ontologia.getName()));
    

    class EnviarMensajeBehaviour extends SimpleBehaviour {

        private boolean finished = false;

        public EnviarMensajeBehaviour(Agent a) {
            super(a);
        }

        public void onStart() {
            /*guiM.pack();
            guiM.setVisible(true);
            guiM.setLocationRelativeTo(null);//*/
            gui.pack();
            gui.setVisible(true);
            gui.setLocationRelativeTo(null);
            
            //System.out.println(servidor.trim());
            //servidor="192.168.0.8";
            try {
                //System.out.println("intento de creacion del socket");
                s = new Socket( servidor, 9000);
                
                //System.out.println("se creo el socket con exito");
                
            } catch (IOException ex) {
                //System.out.println("error en el inicio del socket");
                Logger.getLogger(interfaz.class.getName()).log(Level.SEVERE, null, ex);
                
            }catch (Exception e){
            e.printStackTrace();
            }
        }

        @Override
        public void action() {
            //se omite el envio de mensajes entre plataformas mediante el agente debido a errores internos de jade que inpiden la comunicacion entre plataformas
            //System.out.println("entro a la accion del mesero");
            if (gui.fin) {
                //System.out.println("entro a gui.fin");
                try {
                    String mensaje = "";
                    //System.out.println("orden enviada");
                    String orden = gui.p;
                    //envia la orden al chef
                    
                sale = new ObjectOutputStream(s.getOutputStream());
                //System.out.println("se crearon las variables sale");
                numcli++;
                cliente.setNumeroCliente(numcli);
                            cliente.setNombreCliente("Autoservicio");
                    sale.writeObject(cliente.getNumeroCliente()+"$"+cliente.getNombreCliente()+"$"+ orden);
                    //System.out.println(cliente.getNumeroCliente()+"$"+cliente.getNombreCliente()+"$"+ orden);
                    gui.settext("Su orden se está cocinando, por favor espere...\n");
                    //espera respuesta del chef
                    while (!mensaje.equals("exit")) {
                        //System.out.println("entro a while");
                        entra = new ObjectInputStream(s.getInputStream());
               //System.out.println("se crearon las variables entra "); 
                        mensaje = "";
                        mensaje = (String) entra.readObject();
                        //System.out.println("se leyo el objeto");
                        //recibe respuesta del chef
                        if (mensaje.length() != 0) {
                            //envia la orden al cajero para hacer la cuenta
                            //System.out.println("mensaje recibido de chef");
                            
                            r.setLocalName("Cajero");
                            msg2 = new ACLMessage(ACLMessage.REQUEST);
                            
                            msg2.addReceiver(r);
                            
                            msg2.setLanguage(codec.getName());
                            msg2.setOntology(ontologia.getName());
                            msg2.setSender(getAID());
                            o.setPedido(mensaje);
                            o.setTotal(0);
                            o.setClienteOrden(cliente);
                            of.setOforden(o);
                            getContentManager().fillContent(msg2, of);
                            send(msg2);
                            //System.out.println("mensaje enviado a cajero");
                            ACLMessage msg = blockingReceive(mt);
                            
                            if (msg != null) {
                                //System.out.println("mensaje recibido de cajero");
                                if (msg.getPerformative() == ACLMessage.NOT_UNDERSTOOD) {
                                    //System.out.println("Mensaje NOT UNDERSTOOD recibido");
                                } else {
                                    if (msg.getPerformative() == ACLMessage.REQUEST) {
                                        ContentElement ec = getContentManager().extractContent(msg);
                                        //System.out.println("entro a msg.getPerformative() == ACLMessage.REQUEST");
                                        if (ec instanceof OfertaOrden) {
                                            // Recibido un INFORM con contenido correcto
                                            //se obtiene la cuenta total de lo consumido
                                            OfertaOrden ofc = (OfertaOrden) ec;
                                            /*System.out.println("desde mesero");
                                            System.out.println(ofc.getOforden().getPedido());
                                            System.out.println(ofc.getOforden().getClienteOrden().getNumeroCliente()+"");
                                            System.out.println(ofc.getOforden().getClienteOrden().getNombreCliente());
                                            System.out.println(ofc.getOforden().getTotal());//*/
                                            //obtenemos la orden con los platillos, bebidas, etc. junto al total a pagar
                                            gui.settext("Su pedido esta listo\nUsted pidio:\n\n");
                                            //imprime todo el pedido
                                            orden(ofc.getOforden().getPedido());
                                            //imprime el total del pedido obtenido del cajero
                                            
                                            gui.settext("El total es de: $ "+ofc.getOforden().getTotal()+"\n\n");
                                        } else {
                                            // Recibido un INFORM con contenido incorrecto
                                            //System.out.println("entro a else de instanceof");
                                            ACLMessage reply = msg.createReply();
                                            reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                                            reply.setContent("( UnexpectedContent (expected ping))");
                                            send(reply);
                                        }
                                    } else {
                                        // Recibida una performativa incorrecta
                                        //System.out.println("entro a else de msg.getPerformative() == ACLMessage.REQUEST");
                                        ACLMessage reply = msg.createReply();
                                        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                                        reply.setContent("( (Unexpected-act " + ACLMessage.getPerformative(msg.getPerformative()) + ")( expected (inform)))");
                                        send(reply);
                                    }
                                }
                            } else {
                                //System.out.println("No message received");
                            }
                            //recibe el mensaje del chef para entregar la comida
                            gui.settext("Disfrute su orden\n\n");
                            mensaje="exit";
                            //System.out.println("se creo exit");
                            //orden entregada al cliente
                            //gui.settext(mensaje);//*/
                            
                        }
                    }

                } catch (IOException ex) {
                    //System.out.println("1");
                    Logger.getLogger(Mesero.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    //System.out.println("2");
                    Logger.getLogger(Mesero.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Codec.CodecException ex) {
                    //System.out.println("3");
                    Logger.getLogger(Mesero.class.getName()).log(Level.SEVERE, null, ex);
                } catch (OntologyException ex) {
                    //System.out.println("4");
                    Logger.getLogger(Mesero.class.getName()).log(Level.SEVERE, null, ex);
                }
                gui.fin = false;
                gui.p="";
            }
        }

        public boolean done() {
            //System.out.println("finalizo agente");
            return finished;
        }

        public int onEnd() {
            // Hace que el comportamiento se reinicie al finalizar.
            //System.out.println("reseteo de agente");
            reset();
            myAgent.addBehaviour(this);
            return 0;
        }
    } // Fin de la clase EnviarMensajeBehaviour
private void orden(String p){
int index;
        //, total;
        Menu m = new Menu();
        String[] ej2 = p.split(",");
        int[] id = new int[ej2.length];
        String[] nombre = new String[ej2.length];
        int[] precio = new int[ej2.length];
        for (int i = 0; i < ej2.length; i++) {
            id[i] = Integer.parseInt(ej2[i]);
        }
        //total = 0;
        index = 0;
        for (int i = 0; i < ej2.length; i++) {
            index = m.getIndex(id[i]);
            //total += m.getCosto(index);//se obtiene el total del pedido para poder imprimir
            nombre[i] = m.getNombre(index);//se obtiene un arreglo de todos los nombres de los platillos para imprimir
            precio[i] = m.getCosto(index);//se obtiene un arreglo de todos los precios para poder imprimir
        }
        for (int i = 0; i < ej2.length; i++) {
            gui.settext(nombre[i]+" -> $ " + precio[i]+"\n");
        }
}
    @Override
    protected void setup() {
        servidor=javax.swing.JOptionPane.showInputDialog("Ingrese la direccion del servidor");
        //servidor="192.168.0.8";

        /**
         * Registrarse en el DF
         */
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Mesero");
        sd.setName(getName());
        sd.setOwnership("ARNOIA");
        dfd.setName(getAID());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            System.err.println(getLocalName() + " registration with DF unsucceeded. Reason: " + e.getMessage());
            doDelete();
        }
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontologia);
        EnviarMensajeBehaviour EnviarBehaviour = new EnviarMensajeBehaviour(this);
        addBehaviour(EnviarBehaviour);
    }

    @Override
    protected void takeDown() {
        //System.out.println("takedown");
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

}
