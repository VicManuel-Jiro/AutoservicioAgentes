/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agentes;

import conceptos.Cliente;
import conceptos.Menu;
import conceptos.Orden;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontologia.RestauranteOntologia;
import predicados.OfertaOrden;
import gui.chefConsola;
import jade.core.AID;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Jiro
 */
public class Cajero extends Agent {

    public chefConsola guichef = new chefConsola();
    private Codec codec = new SLCodec();
    private Ontology ontologia = RestauranteOntologia.getInstance();
    private Menu m = new Menu();
    float total = 0;
    int index;
    static Socket s;
    static ObjectInputStream entra;
    static ObjectOutputStream sale;

    class WaitPingAndReplyBehaviour extends SimpleBehaviour {

        private boolean finished = false;

        public WaitPingAndReplyBehaviour(Agent a) {
            super(a);
        }

        @Override
        public void action() {
            guichef.pack();
            guichef.setVisible(true);
            guichef.consola.append("Cajero:Esperando.\n");
            //System.out.println("\nEsperando cuenta del mesero....");

            MessageTemplate mt = MessageTemplate.and(
                    MessageTemplate.MatchLanguage(codec.getName()),
                    MessageTemplate.MatchOntology(ontologia.getName()));
            ACLMessage msg = blockingReceive(mt);
            try {

                if (msg != null) {
                    if (msg.getPerformative() == ACLMessage.NOT_UNDERSTOOD) {
                        //System.out.println("Mensaje NOT UNDERSTOOD recibido");
                    } else {
                        if (msg.getPerformative() == ACLMessage.REQUEST) {
                            //System.out.println("mensaje de mesero recibido");
                            ContentElement ec = getContentManager().extractContent(msg);
                            if (ec instanceof OfertaOrden) {
                                // Recibido un INFORM con contenido correcto
                                guichef.consola.append("\nCajero: Orden recibida,cobrando.\n");
                                OfertaOrden of = (OfertaOrden) ec;
                                Orden orden = of.getOforden();
                                String pedido = orden.getPedido();
                                String[] ej2 = pedido.split(",");
                                int[] id = new int[ej2.length];
                                String[] nombre = new String[ej2.length];
                                int[] precio = new int[ej2.length];
                                for (int i = 0; i < ej2.length; i++) {
                                    id[i] = Integer.parseInt(ej2[i]);
                                }
                                total = 0;
                                index = 0;
                                for (int i = 0; i < ej2.length; i++) {
                                    index = m.getIndex(id[i]);
                                    total += m.getCosto(index);//se obtiene el total del pedido para poder imprimir
                                    nombre[i] = m.getNombre(index);//se obtiene un arreglo de todos los nombres de los platillos para imprimir
                                    precio[i] = m.getCosto(index);//se obtiene un arreglo de todos los precios para poder imprimir
                                }
                                guichef.consola.append("Cajero: La orden fue: \n\n");
                                for (int i = 0; i < ej2.length; i++) {
                                    guichef.consola.append(nombre[i] + " -> $" + precio[i] + "\n");
                                }
                                guichef.consola.append("Cajero: total: " + total + "\n\n");
                                of.getOforden().setTotal(total);

                                ACLMessage msg2 = new ACLMessage(ACLMessage.REQUEST);
                                //System.out.println(of.getOforden().getTotal());
                                msg2.setLanguage(codec.getName());
                                msg2.setOntology(ontologia.getName());
                                AID r = new AID();
                                r.setLocalName("Mesero");
                                msg2.setSender(getAID());
                                msg2.addReceiver(r);

                                OfertaOrden ofc = new OfertaOrden();
                                Orden o = new Orden();
                                Cliente c = new Cliente();

                                c.setNombreCliente(of.getOforden().getClienteOrden().getNombreCliente());
                                c.setNumeroCliente(of.getOforden().getClienteOrden().getNumeroCliente());
                                o.setClienteOrden(c);
                                o.setPedido(of.getOforden().getPedido());
                                o.setTotal(total);
                                ofc.setOforden(o);
                                /*System.out.println("desde cajero");
                                System.out.println(ofc.getOforden().getPedido());
                                System.out.println(ofc.getOforden().getClienteOrden().getNumeroCliente() + "");
                                System.out.println(ofc.getOforden().getClienteOrden().getNombreCliente());
                                System.out.println(ofc.getOforden().getTotal());//*/
                                getContentManager().fillContent(msg2, of);
                                //System.out.println("enviando desde cajero");
                                send(msg2);
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
            }

        }

        public boolean done() {

            return finished;

        }
    } // Fin de la clase EnviarMensajeBehaviour

    @Override
    protected void setup() {
        /*try {
            s = new Socket("192.168.0.33", 9000);
            entra = new ObjectInputStream(s.getInputStream());
            sale = new ObjectOutputStream(s.getOutputStream());

        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(chefConsola.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }//*/

        /**
         * Registrarse en el DF
         */
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Cajero");
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

}
