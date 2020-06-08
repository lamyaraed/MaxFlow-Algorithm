import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;

public class MaxFlowGUI {
    private static JFrame jFrame;
    private JPanel GUIPanel;
    private JTextField nVertices;
    private JTextField nEdges;
    private JButton buttonSetTable;
    private JTable table1;
    private JScrollPane tableScrollPane;
    private JTextField startV;
    private JTextField endV;
    private JButton buttonStart;
    private JTextField textFieldResult;
    private JButton buttonResult;

    private int nVert;
    private int nEdg;
    private int Result;
    private int dircType;
    private int sVertex;
    private int eVertex;

    Graph graph;
    Utils utils;
    GraphDraw graphDraw;

    public static void main(String[] args) {
        jFrame = new JFrame("Dijkstra Graph Algorithm");
        jFrame.setContentPane(new MaxFlowGUI().GUIPanel);
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }
    MaxFlowGUI(){
        String[] column ={"Edges","Source","Destination","Capacity"};
        DefaultTableModel dtm = new DefaultTableModel(column,0);
        table1.setModel(dtm);
        buttonStart.setEnabled(false);
        //textFieldResult.setEnabled(false);
        textFieldResult.setEditable(false);

        buttonSetTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Vertices = nVertices.getText();
                String Edges = nEdges.getText();

                if (Vertices.equals("") || Edges.equals("")){
                    // Code To popup an ERROR_MESSAGE Dialog.
                    JOptionPane.showMessageDialog(jFrame, "You must provide valid values for vertices, edges, and graph type!",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    nVert = Integer.parseInt(Vertices);
                    nEdg = Integer.parseInt(Edges);

                    DefaultTableModel dtm = new DefaultTableModel(column, nEdg);
                    for(int rows=0; rows<nEdg; rows++){
                        dtm.setValueAt(rows+1, rows, 0);
                    }
                    table1.setModel(dtm);
                    buttonStart.setEnabled(true);

                }
            }
        });

        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String startVert = startV.getText();
                String endVert = endV.getText();
                if (startVert.equals("") || endVert.equals("") || Integer.parseInt(startVert)>= nVert || Integer.parseInt(endVert) >= nVert){
                    // Code To popup an ERROR_MESSAGE Dialog.
                    JOptionPane.showMessageDialog(jFrame, "You must provide valid values for start and end vertices",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    sVertex = Integer.parseInt(startVert);
                    eVertex = Integer.parseInt(endVert);
                    getTableData();
                }
            }
        });
    }

    void getTableData() {
        graph = new Graph(nVert);
        utils = new Utils();

        boolean dataProv = false;
        for (int ed = 0; ed <nEdg; ed++) {
            for (int val = 1; val < 4; val++) {
                if (table1.getModel().getValueAt(ed, val) == null) {
                    JOptionPane.showMessageDialog(jFrame, "There is one or more cells in table empty.",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                    dataProv = false;
                    break;
                } else
                    dataProv = true;
            }
            if(!dataProv) break;
        }

        boolean valid = true;
        Edge Tempedge;
        if (dataProv) {
            for (int i = 0; i <nEdg; i++) {
                int sVal, dVal, wVal;
                int col = 1;
                int row = i;
                String value = table1.getModel().getValueAt(row, col).toString();
                if (value.equals("") || Integer.parseInt(value) < 0 || Integer.parseInt(value) >= graph.Vertex) {
                    JOptionPane.showMessageDialog(jFrame, "You must provide valid value for source vertex(from 0 to " + (nVert-1) + ")",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                    valid = false;
                    break;
                } else
                    sVal = Integer.parseInt(value);

                col = 2;
                row = i;
                String Destvalue = table1.getModel().getValueAt(row, col).toString();
                if (Destvalue.equals("") || Integer.parseInt(Destvalue) < 0 || Integer.parseInt(Destvalue) >= graph.Vertex) {
                    JOptionPane.showMessageDialog(jFrame, "You must provide valid value for destination vertex (from 0 to " + (nVert-1) + ")",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                    valid = false;
                    break;
                } else
                    dVal = Integer.parseInt(Destvalue);

                col = 3;
                row = i;
                String CapValue = table1.getModel().getValueAt(row, col).toString();
                if (CapValue.equals("") || Integer.parseInt(CapValue) < 0) {
                    JOptionPane.showMessageDialog(jFrame, "vertex capacity must be 0 or greater",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                    valid = false;
                    break;
                } else {
                    wVal = Integer.parseInt(CapValue);
                }

                Tempedge = new Edge(wVal,sVal,dVal);
                graph.edges.add(Tempedge);

            }
            if(valid) {


                graphDraw = new GraphDraw(nVert,nEdg,graph.edges,sVertex,eVertex);
                Result = utils.fordFulkerson(graph, sVertex, eVertex);
                Vector<Edge> path = new Vector<>();
                path = utils.getPath();
                ArrayList<Edge> changes = new ArrayList<>();
                changes.addAll(path);
                graphDraw.getAnswer(changes);
                textFieldResult.setText(Integer.toString(Result));
            }
        }
    }

}
