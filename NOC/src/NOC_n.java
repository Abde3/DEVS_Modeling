import java.util.Map;
import java.util.Vector;
import DEVSModel.DEVSCoupled;
import DEVSModel.Port;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class NOC_n extends DEVSCoupled implements Model{
	@Override
	public void setSelectPriority() {

	}

	@Override
	public void save_model(Document doc) {

	}

//
////	public enum Topology {LINEAR, MESH};
//	private enum RowPosition {TOP, MIDDLE, BOTTOM};
//
//	private Vector<NOC_Unit> NOCs = new Vector<>();
//	private Generator_Task Gen;
//
//	private int size_noc;
//
//	public NOC_n(int dimension, Model.NOC_factory.Topology topology){
//
//		super();
//
//		this.name = "NOC_" + topology + "_" + dimension;
//		this.size_noc = dimension * dimension;
//
//
//		/* ADD the generator */
//		Gen = new Generator_Task("Gen");
//		this.getSubModels().add(Gen);
//
//
//		/** GENERATE ALL NOCs UNIT **/
//		for (int i = 0; i < size_noc; i += dimension) {
//
//			for (int j = 0; j < dimension; j++) {
//				int nodeIndex = i+j;
//
//				NOC_Unit NQ_tmp = new NOC_Unit("NQ", nodeIndex, dimension);
//				NOCs.addElement(NQ_tmp);
//				this.getSubModels().add(NQ_tmp);
//			}
//
//		}
//
//
//		/** LINES EDGES **/
//		for (int i = 0; i < size_noc; i+=dimension) {
//			int from = i;
//
//			for (int j = 1; j < dimension; j++) {
//				int to = i + j;
//
//				/** FORWARD LINKS **/
//				this.addIC(NOCs.get(from).getOutPort("out_NCUnit-0"), NOCs.get(to).getInPort("in_NCUnit-0"));
//
//				/** BACKWARD LINKS **/
//				this.addIC(NOCs.get(to).getOutPort("out_NCUnit-1"), NOCs.get(from).getInPort("in_NCUnit-1"));
//
//				from = to;
//			}
//		}
//
//
//		/** FORWARD COLUMNS EDGES **/
//		for (int i = 0; i < dimension; i++) {
//			int from = i;
//
//			for (int j = 1; j < dimension; j++) {
//				int to = i + j * dimension;
//
//				/** FORWARD LINKS **/
//				this.addIC(NOCs.get(from).getOutPort("out_NCUnit-2"), NOCs.get(to).getInPort("in_NCUnit-2"));
//
//				/** BACKWARD LINKS **/
//				this.addIC(NOCs.get(to).getOutPort("out_NCUnit-3"), NOCs.get(from).getInPort("in_NCUnit-3"));
//
//				from = to;
//			}
//		}
//
//
//		this.addIC(Gen.getOutPort("out"), NOCs.get(1).getInPort("in_NCUnit-0"));
//
//		//this.setSelectPriority();
//
//	}
//
//
//
//	@Override
//	public void setSelectPriority() {
//
//
//		/****************************************************************************************************/
//		/**********************************************************| CONFLICTING SET |****|SELECT|*********** /
//		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(Gen, NQ0, NQ1) ),  	NQ0);
//		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(Gen, NQ0, NQ2) ),  	NQ0);
//		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(Gen, NQ1, NQ2) ),  	NQ1);
//		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(NQ0, NQ1, NQ2) ),  	NQ0);
//		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(Gen, NQ0) ), 	 	 	NQ0);
//		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(Gen, NQ1) ),  			NQ1);
//		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(Gen, NQ2) ), 			NQ2);
//		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(NQ0, NQ1) ), 			NQ0);
//		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(NQ0, NQ2) ), 			NQ0);
//		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(NQ1, NQ2) ), 			NQ1);
//		/**********************************************************| CONFLICTING SET |****|SELECT|***********/
//		/****************************************************************************************************/
//
//	}
//
//
//
//	@Override
//	public void save_model(Document doc) {
//		Element rootElement = doc.createElement("graph");
//		doc.appendChild(rootElement);
//
//		for (NOC_Unit node: NOCs) {
//			// nodes elements
//			Element nodeElement = doc.createElement("nodes");
//			nodeElement.setAttribute("id", node.getName());
//			nodeElement.setAttribute("x", "0");
//			nodeElement.setAttribute("y", "0");
//			rootElement.appendChild(nodeElement);
//
//			Element connectorElement = Persistance.set_attribute_connector(node, doc);
//
//			nodeElement.appendChild(connectorElement);
//
//
//		}
//
//
//		for (Map.Entry<Port, Vector<Port>> entry : this.getIC().entrySet()) {
//			Port srcPort = entry.getKey();
//			Vector<Port> destPorts = entry.getValue();
//
//
//			destPorts.forEach(destPort -> {
//				// connection elements
//				Element connectionElement = doc.createElement("connections");
//				connectionElement.setAttribute("source", srcPort.getModel().getName());
//				connectionElement.setAttribute("target", destPort.getModel().getName());
//				rootElement.appendChild(connectionElement);
//			});
//
//		}
//	}
//
}
