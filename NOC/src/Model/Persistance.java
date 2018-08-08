package Model;

import DEVSModel.DEVSModel;
import DEVSModel.Port;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Vector;

public class Persistance {


    private static final java.lang.String FILE_EXTENSION = ".graph";
    private Document doc;
    private DOMSource source;
    private StreamResult result;
    private Transformer transformer;
    private Element rootElement;


    public Persistance(String path) {


        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);

        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        doc = docBuilder.newDocument();

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        source = new DOMSource(doc);
//        result = new StreamResult(System.out);
        result = new StreamResult(new File(path));


        rootElement = doc.createElement("graph:GModel");

        rootElement.setAttribute("xmlns:xmi", "http://www.omg.org/XMI");
        rootElement.setAttribute("xmlns:graph", "http://de.tesis.dynaware.grapheditor.model/1.0");
        rootElement.setAttribute("xmi:version", "2.0");

        doc.createElementNS("http://de.tesis.dynaware.grapheditor.model/1.0", "graph");

        doc.appendChild(rootElement);

    }


    public void saveNodes(NOC noc) {


		for (Vector<NOC_Unit> row: noc.getModel()) {
            for (NOC_Unit unitNode: row) {

                // nodes elements
                Element nodeElement = create_nodes_element(unitNode);
                set_attribute_connector(nodeElement, unitNode ,4,NOC_factory.Topology.MESH, doc);
            }
		}


        for (Map.Entry<Port, Vector<Port>> entry: NocNetwork.getInstance().getNocInstance().getIC().entrySet()) {

            Port src = entry.getKey();

            for (Port port : entry.getValue()) {
                DEVSModel modelSrc = src.getModel();
                DEVSModel modelDest = port.getModel();

                if (modelSrc instanceof  NOC_Unit) {
                    int srcNodePos = Util.nodeNumFromCoordinate(Util.nodeCoordinateFromNodeName(modelSrc.getName()), 4);
                    int destNodePos = Util.nodeNumFromCoordinate(Util.nodeCoordinateFromNodeName(modelDest.getName()), 4);

                    int srcConnectorPos = 0;
                    int destConnectorPos = 0;

                    String srcDirection = Util.getConnectorString(Util.nodeCoordinateFromNodeName(modelSrc.getName()), Util.nodeCoordinateFromNodeName(modelDest.getName()));
                    String destDirection = Util.getConnectorString(Util.nodeCoordinateFromNodeName(modelDest.getName()), Util.nodeCoordinateFromNodeName(modelSrc.getName()));

                    Map.Entry<Integer,Integer> srcRes = getPosFromType(modelSrc.getName(), srcDirection+"-output");
                    Map.Entry<Integer,Integer> destRes = getPosFromType(modelDest.getName(), destDirection+"-input");

                    if(srcRes == null || destRes == null) continue;

                    srcConnectorPos = srcRes.getValue();
                    destConnectorPos = destRes.getValue();

                    create_connection_element(srcNodePos, srcConnectorPos, destNodePos, destConnectorPos);

                } else {
                    System.err.println("Skipping, not an instance of NOC_UNIT");
                }

            }

        }

        setConnectionNumber();

        System.out.println("-------------------------------------");
    }



    private Map.Entry<Integer,Integer> getPosFromType(String nodeName, String connectionType) {
        XPath xPath = XPathFactory.newInstance().newXPath();
        try {

            NodeList nodes = (NodeList) xPath.evaluate("//nodes", doc, XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);

                if ( ! node.getAttributes().getNamedItem("id").getNodeValue().contains(nodeName) )
                    continue;

                NodeList attr = node.getChildNodes();
                for (int j = 0; j < attr.getLength(); j++) {
                    NamedNodeMap subelems = attr.item(j).getAttributes();
                    if (subelems.item(0).getNodeValue().contains(connectionType)) {
                        return new AbstractMap.SimpleEntry<>(i, j);
                    }

                }
            }
        }
            catch (XPathExpressionException e) {
            e.printStackTrace();
        }


    return null;
    }



    private int setConnectionNumber() {
        XPath xPath = XPathFactory.newInstance().newXPath();
        try {

            NodeList connections = (NodeList) xPath.evaluate("//connections", doc, XPathConstants.NODESET);

            for (int i = 0; i < connections.getLength(); i++) {
                Node connection = connections.item(i);
                NamedNodeMap attributes = connection.getAttributes();

                for (String attributeName : new String[]{"source", "target"}) {
                    AbstractMap.SimpleEntry<Integer, Integer> nodeAndConnectorNumber = Util.getConnectorFromConnectionString(attributes.getNamedItem(attributeName).getNodeValue());
                    Integer nodeNum = nodeAndConnectorNumber.getKey();
                    Integer connectorNum = nodeAndConnectorNumber.getValue();

                    XPath xPathNode = XPathFactory.newInstance().newXPath();
                    NodeList nodes = (NodeList) xPathNode.evaluate("//nodes", doc, XPathConstants.NODESET);

                    ((Element)nodes.item(nodeNum).getChildNodes().item(connectorNum)).setAttribute("connections", "//@connections."+i);
                }

            }
        }
        catch (XPathExpressionException e) {
            e.printStackTrace();
        }


        return 0;
    }


    private Element create_connection_element(int SrcNodeNum, int SrcNumPort, int destNodeNum, int destNumPort) {


        Element connectionElement = doc.createElement("connections");
        connectionElement.setAttribute("source", "//@nodes." + SrcNodeNum + "/@connectors." + SrcNumPort);
        connectionElement.setAttribute("target", "//@nodes." + destNodeNum + "/@connectors." + destNumPort);

        Element jointsElement = doc.createElement("joints");
        jointsElement.setAttribute("x", Integer.toString( Util.getCoordinateFromNodeNum(SrcNodeNum, SrcNumPort).getX()) );
        jointsElement.setAttribute("y", Integer.toString( Util.getCoordinateFromNodeNum(SrcNodeNum, SrcNumPort).getY()) );
        connectionElement.appendChild(jointsElement);

        Element jointsElement2 = doc.createElement("joints");
        jointsElement2.setAttribute("x", Integer.toString( Util.getCoordinateFromNodeNum(destNodeNum, destNumPort).getX()));
        jointsElement2.setAttribute("y", Integer.toString( Util.getCoordinateFromNodeNum(destNodeNum, destNumPort).getY()) );
        connectionElement.appendChild(jointsElement2);

        rootElement.appendChild(connectionElement);

        return connectionElement;
    }

    private Element create_nodes_element(Model.NOC_Unit unitNode) {
        Element nodeElement = doc.createElement("nodes");
        nodeElement.setAttribute("id", unitNode.getName());
        nodeElement.setAttribute("x", Integer.toString(unitNode.coordinate.getY()*200 + 500));
        nodeElement.setAttribute("y", Integer.toString(unitNode.coordinate.getX()*200 + 500));
        rootElement.appendChild(nodeElement);
        return nodeElement;
    }


    public void set_attribute_connector(Element DOMNode, NOC_Unit node, int rowSize, NOC_factory.Topology topology, Document doc) {

        NodeCoordinate coordinate = node.getCoordinate();
        NOC_Unit_factory.NODE_POSITION position = Util.getNodePosition(coordinate, rowSize, topology);

        if (position == NOC_Unit_factory.NODE_POSITION.CORNER) {
            if (coordinate.getX() == 0) {
                if (coordinate.getY() == 0) {

                    add_connector_left_input(DOMNode, doc);
                    add_connector_right_output(DOMNode, doc);
                    add_connector_right_input(DOMNode, doc);
                    add_connector_bottom_output(DOMNode, doc);
                    add_connector_bottom_input(DOMNode, doc);

                } else {

                    add_connector_left_input(DOMNode, doc);
                    add_connector_left_output(DOMNode, doc);
                    add_connector_bottom_output(DOMNode, doc);
                    add_connector_bottom_input(DOMNode, doc);

                }
            } else {
                if (coordinate.getY() == 0) {

                    add_connector_right_output(DOMNode, doc);
                    add_connector_right_input(DOMNode, doc);
                    add_connector_top_input(DOMNode, doc);
                    add_connector_top_output(DOMNode, doc);
                } else {

                    add_connector_left_input(DOMNode, doc);
                    add_connector_left_output(DOMNode, doc);
                    add_connector_top_input(DOMNode, doc);
                    add_connector_top_output(DOMNode, doc);
                }
            }
        } else if (position == NOC_Unit_factory.NODE_POSITION.EDGE) {

            if (coordinate.getY() == 0) {
                // gauche

                add_connector_bottom_output(DOMNode, doc);
                add_connector_bottom_input(DOMNode, doc);
                add_connector_right_output(DOMNode, doc);
                add_connector_right_input(DOMNode, doc);
                add_connector_top_input(DOMNode, doc);
                add_connector_top_output(DOMNode, doc);
            } else if (coordinate.getX() == 0) {
                // haut

                add_connector_left_input(DOMNode, doc);
                add_connector_left_output(DOMNode, doc);
                add_connector_bottom_output(DOMNode, doc);
                add_connector_bottom_input(DOMNode, doc);
                add_connector_right_output(DOMNode, doc);
                add_connector_right_input(DOMNode, doc);

            } else if (coordinate.getY() == rowSize-1) {
                // droite

                add_connector_left_input(DOMNode, doc);
                add_connector_left_output(DOMNode, doc);
                add_connector_bottom_output(DOMNode, doc);
                add_connector_bottom_input(DOMNode, doc);
                add_connector_top_input(DOMNode, doc);
                add_connector_top_output(DOMNode, doc);
            } else {
                // bas

                add_connector_left_input(DOMNode, doc);
                add_connector_left_output(DOMNode, doc);
                add_connector_right_output(DOMNode, doc);
                add_connector_right_input(DOMNode, doc);
                add_connector_top_input(DOMNode, doc);
                add_connector_top_output(DOMNode, doc);            }

        } else if (position == NOC_Unit_factory.NODE_POSITION.CENTER) {

            add_connector_left_input(DOMNode, doc);
            add_connector_left_output(DOMNode, doc);
            add_connector_bottom_output(DOMNode, doc);
            add_connector_bottom_input(DOMNode, doc);
            add_connector_right_output(DOMNode, doc);
            add_connector_right_input(DOMNode, doc);
            add_connector_top_input(DOMNode, doc);
            add_connector_top_output(DOMNode, doc);

        } else  {
            return;
        }
    }

    private  void add_connector_top_output(Element DOMNode, Document doc) {
        Element connectorElement8 = doc.createElement("connectors");
        connectorElement8.setAttribute("type", "top-output");
        DOMNode.appendChild(connectorElement8);
    }

    private  void add_connector_top_input(Element DOMNode, Document doc) {
        Element connectorElement7 = doc.createElement("connectors");
        connectorElement7.setAttribute("type", "top-input");
        DOMNode.appendChild(connectorElement7);
    }

    private  void add_connector_left_output(Element DOMNode, Document doc) {
        Element connectorElement2 = doc.createElement("connectors");
        connectorElement2.setAttribute("type", "left-output");
        DOMNode.appendChild(connectorElement2);
    }

    private  void add_connector_bottom_input(Element DOMNode, Document doc) {
        Element connectorElement5 = doc.createElement("connectors");
        connectorElement5.setAttribute("type", "bottom-input");
        DOMNode.appendChild(connectorElement5);
    }

    private  void add_connector_bottom_output(Element DOMNode, Document doc) {
        Element connectorElement4 = doc.createElement("connectors");
        connectorElement4.setAttribute("type", "bottom-output");
        DOMNode.appendChild(connectorElement4);
    }

    private  void add_connector_right_input(Element DOMNode, Document doc) {
        Element connectorElement3 = doc.createElement("connectors");
        connectorElement3.setAttribute("type", "right-input");
        DOMNode.appendChild(connectorElement3);
    }

    private  void add_connector_right_output(Element DOMNode, Document doc) {
        Element connectorElement2 = doc.createElement("connectors");
        connectorElement2.setAttribute("type", "right-output");
        DOMNode.appendChild(connectorElement2);
    }

    private  void add_connector_left_input(Element DOMNode, Document doc) {
        Element connectorElement = doc.createElement("connectors");
        connectorElement.setAttribute("type", "left-input");
        DOMNode.appendChild(connectorElement);
    }


    public void generate_output() {
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

}
