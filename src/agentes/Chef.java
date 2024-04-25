/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agentes;

import conceptos.*;

import gui.chefConsola;
import jade.core.*;
import jade.core.behaviours.*;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Jiro
 */
public class Chef extends Agent {

    public chefConsola guichef = new chefConsola();
/*
    private Codec codec = new SLCodec();
    private Ontology ontologia = RestauranteOntologia.getInstance();
    MessageTemplate mt = MessageTemplate.and(
            MessageTemplate.MatchLanguage(codec.getName()),
            MessageTemplate.MatchOntology(ontologia.getName()));
    //*/
    Socket socket;
    int puerto = 9000;
    ServerSocket server;
    ObjectOutputStream salida;
    ObjectInputStream entrada;
    String mensaje;
    String[] datos;

    class WaitPingAndReplyBehaviour extends SimpleBehaviour {

        private boolean finished = false;

        public WaitPingAndReplyBehaviour(Agent a) {
            super(a);
        }

        public void onStart() {
            guichef.pack();
            guichef.setVisible(true);
            guichef.consola.append("Chef:Esperando orden del mesero....");
            try {
                server = new ServerSocket(puerto);
                socket = new Socket();
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Chef.class.getName()).log(Level.SEVERE, null, ex);

            }
        }

        @Override
        public void action() {

            try {
                socket = server.accept();
                
                
                mensaje = "";
                
                while (!mensaje.equals("exit")) {
                    entrada = new ObjectInputStream(socket.getInputStream());
                    mensaje = "";
                    mensaje = (String) entrada.readObject();
                    if (mensaje.length() > 0) {
                        guichef.consola.append("\nChef:Orden recibida:\n");
                        datos=mensaje.split("\\$");
                        guichef.consola.append("\nChef: Orden: "+datos[0]+"\n");
                        guichef.consola.append("\nChef: Cliente: "+datos[1]+"\n");
                        orden(datos[2]);
                        guichef.consola.append("\nChef:Cocinando...");
                        guichef.consola.append("\nChef:Orden lista.");
                        guichef.consola.append("\nChef:Orden despachada." + "\n");
                        salida = new ObjectOutputStream(socket.getOutputStream());
                        salida.writeObject(datos[2]);
                        ////////////////////////////////////////////////////////////////////////////////////
                       /* se elimina funcionalidad de mensajes acl debido a problemas con la plataforma jade
                        ACLMessage msg2 = new ACLMessage(ACLMessage.REQUEST);
                        msg2.setLanguage(codec.getName());
                        msg2.setOntology(ontologia.getName());
                        msg2.setSender(getAID());
                        AID r = new AID();
                        r.setLocalName("Cajero");
                        msg2.addReceiver(r);
                        OfertaOrden of = new OfertaOrden();
                        Cliente c = new Cliente();
                        c.setNombreCliente("Ledezma");
                        c.setNumeroCliente(123123);
                        Orden o = new Orden();
                        o.setPedido(mensaje);
                        of.setOforden(o);
                        o.setClienteOrden(c);
                        getContentManager().fillContent(msg2, of);
                        send(msg2);
                         ACLMessage msg = blockingReceive(mt);
                        try {
                            if (msg != null) {
                                if (msg.getPerformative() == ACLMessage.NOT_UNDERSTOOD) {
                                    System.out.println("Mensaje NOT UNDERSTOOD recibido");
                                } else {
                                    if (msg.getPerformative() == ACLMessage.INFORM) {
                                        ContentElement ec = getContentManager().extractContent(msg);
                                        if (ec instanceof OfertaCuenta) {
                                            // Recibido un INFORM con contenido correcto
                                            OfertaCuenta oc = (OfertaCuenta) ec;
                                            Cuenta cuenta = oc.getOfcuenta();
                                            System.out.println(cuenta.getTotal());
                                            salida.writeUTF(cuenta.getTotal() + "");

                                        } else {
                                            // Recibido un INFORM con contenido incorrecto
                                            ACLMessage reply = msg.createReply();
                                            reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                                            reply.setContent("( UnexpectedContent (expected ping))");
                                            send(reply);
                                        }
                                    } else {
                                        // Recibida una performativa incorrecta
                                        ACLMessage reply = msg.createReply();
                                        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                                        reply.setContent("( (Unexpected-act " + ACLMessage.getPerformative(msg.getPerformative()) + ")( expected (inform)))");
                                        send(reply);
                                    }
                                }
                            } else {
                                //System.out.println("No message received");
                            }

                        } catch (jade.content.lang.Codec.CodecException ce) {
                            System.out.println(ce);
                        } catch (jade.content.onto.OntologyException oe) {
                            System.out.println(oe);
                        }//*/
                    }
                }

                //salida.writeUTF(mensaje);
                socket.close();
            } catch (IOException ex) {
                //Logger.getLogger(socketS.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Chef.class.getName()).log(Level.SEVERE, null, ex);
            } /*catch (Codec.CodecException ex) {
                Logger.getLogger(Chef.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Chef.class.getName()).log(Level.SEVERE, null, ex);
            }//*/ 
        }

        public boolean done() {

            return finished;

        }
    } // Fin de la clase EnviarMensajeBehaviour

    @Override
    protected void setup() {

        /**
         * Registrarse en el DF
         */
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Chef");
        sd.setName(getName());
        sd.setOwnership("ARNOIA");
        dfd.setName(getAID());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (Exception e) {
            System.err.println(getLocalName() + " registration with DF unsucceeded. Reason: " + e.getMessage());
            doDelete();
        }
/*
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontologia);//*/

        WaitPingAndReplyBehaviour PingBehaviour;
        PingBehaviour = new WaitPingAndReplyBehaviour(this);
        addBehaviour(PingBehaviour);

    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

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
            guichef.consola.append("- "+nombre[i] +"\n");
        }
}
    
    
}
