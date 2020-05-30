import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.samples.SimpleGraphDraw;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationImageServer;

import javax.swing.*;

import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.collections15.Transformer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class GraphDraw {
    static DirectedSparseGraph<Integer, Edge> directedGraph;
    private Layout<Integer, Edge> graphLayout;
    static BasicVisualizationServer<Integer,Edge> vs;
    LinkedHashMap<Edge, String> VecEdges = new LinkedHashMap<>();
    Vector<Integer> VisitedVect = new Vector<>();
    ArrayList<Edge> ResultGraph = new ArrayList<>();
    int Vertices, Edges, CurrVert=99999, PrevVert;
    static int Counter = 0;
    JFrame frame;
    JButton buttonNext;
    Graph graph;

    GraphDraw(int V, int E, Vector<Edge> ed){
        this.Vertices = V;
        this.Edges = E;
        directedGraph = new DirectedSparseGraph<>();
        for(int i=0; i<Vertices; i++){
            directedGraph.addVertex(i);
        }
        for(Edge e: ed){
            Edge myEdge = new Edge(e.capacity, e.source, e.dest);
            String EdgeW =  "0 / " + myEdge.capacity;
            VecEdges.put(myEdge, EdgeW);
            System.out.println(VecEdges.get(myEdge));
        }
        createGraph();
    }

    void drawFrame(){
        //Counter =0;
        frame = new JFrame();
        frame.getContentPane().add(vs);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(450,450));
        buttonNext = new JButton("Next");
        buttonNext.setEnabled(false);
        frame.add(buttonNext, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);

        buttonNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                if (Counter == 0){
//                    for(int i=0; i<directedGraph.getEdgeCount(); i++){
//                        for(Edge myEdge : directedGraph.getEdges()) {
//                            System.out.println(myEdge.capacity);
//                            directedGraph.removeEdge(myEdge);
//                        }
//                        System.out.println("still");
//                    }
//                    vs.repaint();
//                    Counter++;
//                    frame.invalidate();
//                    frame.validate();
//                    frame.repaint();
//                }
                if (ResultGraph.size() != 0){
                    Next();
                    vs.repaint();
                    frame.invalidate();
                    frame.validate();
                    frame.repaint();
                }
                else{
                    JOptionPane.showMessageDialog(frame, "We reached the result graph!",
                            "CONGRATS", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    void createGraph(){
        graphLayout = new CircleLayout(directedGraph);
        graphLayout.setSize(new Dimension(450,450));
        vs = new BasicVisualizationServer<Integer, Edge>(graphLayout);
        vs.setPreferredSize(new Dimension(450,450));
        drawVertices();
        drawEdges();
        drawFrame();
    }

    void drawVertices(){
        for(int i=0; i<Vertices; i++){

            Transformer<Integer, Paint> vertexPaint = new Transformer<Integer,Paint>() {
                public Paint transform(Integer i) {
                    return Color.GRAY;
                }
            };
            vs.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
            vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
            vs.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        }
    }

    void drawEdges(){
        for(Edge e: VecEdges.keySet()){
            directedGraph.addEdge(e, e.source, e.dest, EdgeType.DIRECTED);
            vs.getRenderContext().setEdgeLabelTransformer(new org.apache.commons.collections15.Transformer<Edge, String>(){
                public String transform(Edge link) {
                    String out = null;
                    for(Edge o : VecEdges.keySet()){
                        if(link.source == o.source && link.dest == o.dest)
                            out = VecEdges.get(o);
                    }
                    return out;
                }
            });
        }
    }

    void Next() {
        Edge CurrentEdg = null;
        boolean flag = false;
        int CurrEdgeW = 0;
        for(Edge y: VecEdges.keySet()){
            for(Edge k : ResultGraph) {
                if (y.source == k.source && y.dest == k.dest) {
                    //System.out.println( k.source + " --- " + ResultGraph.get(k));
                    CurrentEdg = k;
                    CurrEdgeW = y.capacity;
                    PrevVert = CurrentEdg.source;
                    CurrVert = CurrentEdg.dest;
                    EdgeValues(CurrentEdg, k.capacity, CurrEdgeW);
                    changeVertColour();
                    flag = true;
                    break;
                }
                if(flag) break;
            }
        }
        ResultGraph.remove(CurrentEdg);
    }

    //gets a hashmap for the changed edge and the changed value
    void getAnswer(ArrayList<Edge> Answer){
        if (Answer == null) {
            JOptionPane.showMessageDialog(frame, "There is no path from source to destination",
                    "Error", JOptionPane.WARNING_MESSAGE);
        } else {
            for (Edge i : Answer) {
                ResultGraph.add(i);
            }
            buttonNext.setEnabled(true);
        }
    }

    //todo redo this
    void EdgeValues(Edge edge, int change, int EdgeW){
        int taken = EdgeW - change;
        vs.getRenderContext().setEdgeLabelTransformer(new org.apache.commons.collections15.Transformer<Edge, String>(){
            public String transform(Edge link) {
                if(link.source == edge.source && link.dest == edge.dest)
                    VecEdges.put(link, taken + " / " +EdgeW);
                return VecEdges.get(link);
            }
        });
    }

    ///this function colours the source and destination from which the edge moves
    void changeVertColour(){
        //Transformer;
        Transformer<Integer,Paint> vertexPaint = new Transformer<Integer,Paint>() {
            public Paint transform(Integer i) {
                if(CurrVert == i || PrevVert ==i)
                    return Color.GREEN;
                else
                    return Color.GRAY;
            }
        };
        vs.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
    }
}
