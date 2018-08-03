package Model;

import DEVSModel.DEVSModel;
import DEVSModel.Port;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Map;
import java.util.Vector;

public class Persistance {


    private static final String FILE_EXTENSION = ".graph";
    private Document doc;
    private DOMSource source;
    private StreamResult result;
    private Transformer transformer;


    public Persistance() {

//        String absolutePath = file.getAbsolutePath();
//        if (!absolutePath.endsWith(FILE_EXTENSION)) {
//            absolutePath += FILE_EXTENSION;
//        }

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
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

        source = new DOMSource(doc);
        result = new StreamResult(System.out);


        Element rootElement = doc.createElement("graph");
        doc.appendChild(rootElement);

    }


    public void saveNodes() {


		for (NOC_Unit node: NOCs) {
			// nodes elements
			Element nodeElement = doc.createElement("nodes");
			nodeElement.setAttribute("id", node.getName());
			nodeElement.setAttribute("x", "0");
			nodeElement.setAttribute("y", "0");
			rootElement.appendChild(nodeElement);

			Element connectorElement = Persistance.set_attribute_connector(node, doc);

			nodeElement.appendChild(connectorElement);

		}


		for (Map.Entry<Port, Vector<Port>> entry : this.getIC().entrySet()) {
			Port srcPort = entry.getKey();
			Vector<Port> destPorts = entry.getValue();


			destPorts.forEach(destPort -> {
				// connection elements
				Element connectionElement = doc.createElement("connections");
				connectionElement.setAttribute("source", srcPort.getModel().getName());
				connectionElement.setAttribute("target", destPort.getModel().getName());
				rootElement.appendChild(connectionElement);
			});

		}
    }


    public void generate_output() {
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

}
