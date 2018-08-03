import DEVSModel.DEVSModel;
import DEVSModel.Port;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class Persistance {

    private static final String FILE_EXTENSION = ".graph";



    static public void saveModel(final File file, DEVSModel model) throws ParserConfigurationException, TransformerException {

//        String absolutePath = file.getAbsolutePath();
//        if (!absolutePath.endsWith(FILE_EXTENSION)) {
//            absolutePath += FILE_EXTENSION;
//        }

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        docBuilder = docFactory.newDocumentBuilder();


        Document doc = docBuilder.newDocument();

//        model.save_model(doc);


        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StreamResult result =  new StreamResult(System.out);
        // Output to console for testing
        // StreamResult result = new StreamResult(System.out);

        transformer.transform(source, result);

//        System.out.println("File saved!");



    }

    static public Element set_attribute_connector(NOC_Unit node, Document doc) {

        Element connectorElement = doc.createElement("connectors");

        switch (node.v_in_port.size()) {
            case 4:
                connectorElement.setAttribute("type", "----------------4----------------");
                break;
            case 3:
                connectorElement.setAttribute("type","----------------3----------------");
                break;
            case 2:
                connectorElement.setAttribute("type","----------------2----------------");
                break;
            case 1:
                connectorElement.setAttribute("type","----------------1----------------");
                break;
            default:
                break;
        }

        for (Port inPort :node.inPorts) {
        }

        for (Port outPort :node.outPorts) {

        }

        connectorElement.setAttribute("connection", Integer.toString(node.getInPorts().size()));

        return connectorElement;
    }
}
