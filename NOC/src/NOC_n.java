import java.util.Vector;
import DEVSModel.DEVSCoupled;

public class NOC_n extends DEVSCoupled {

	public enum Topology {LINEAR, MESH};

	private Vector<NOC_Unit> NOCs = new Vector<NOC_Unit>();
	private Generator_Task Gen;
	
	private int size_noc;

	public NOC_n(int dimension, Topology topology){
		
		super();
		
		this.name = "NOC_" + topology + "_" + dimension;
		this.size_noc = dimension * dimension;
				
		Gen = new Generator_Task("Gen");
		this.getSubModels().add(Gen);

//		System.out.println("digraph G {\n");
		
		
		/** GENERATE ALL NOCs UNIT **/
		for (int i = 0; i < size_noc; i += dimension) {
			
//			System.out.print("{ rank = same");
			
			for (int j = 0; j < dimension; j++) {
				int nodeIndex = i+j;
				
				NOC_Unit NQ_tmp = new NOC_Unit("NQ", nodeIndex, dimension);
				NOCs.addElement(NQ_tmp);
				this.getSubModels().add(NQ_tmp);
				
//				System.out.print("; " + nodeIndex);

			}
//			System.out.println(" }");
		}
		
		
		/** LINES EDGES **/
		for (int i = 0; i < size_noc; i+=dimension) {
			int from = i;

			for (int j = 1; j < dimension; j++) {
				int to = i + j;
				
				/** FORWARD LINKS **/
//				System.out.println("" + from + " -> " + "" + to);
				this.addIC(NOCs.get(from).getOutPort("out_NCUnit-0"), NOCs.get(to).getInPort("in_NCUnit-0"));

				/** BACKWARD LINKS **/
//				System.out.println("" + to + " -> " + "" + from );
				this.addIC(NOCs.get(to).getOutPort("out_NCUnit-1"), NOCs.get(from).getInPort("in_NCUnit-1"));
				
				from = to;
			}
		}
		
		
		/** FORWARD COLUMNS EDGES **/
		for (int i = 0; i < dimension; i++) {
			int from = i;

			for (int j = 1; j < dimension; j++) {
				int to = i + j * dimension;
				
				/** FORWARD LINKS **/
//				System.out.println("" + from + " -> " + "" + to);
				this.addIC(NOCs.get(from).getOutPort("out_NCUnit-2"), NOCs.get(to).getInPort("in_NCUnit-2"));
				
				/** BACKWARD LINKS **/
//				System.out.println("" + to + " -> " + "" + from);
				this.addIC(NOCs.get(to).getOutPort("out_NCUnit-3"), NOCs.get(from).getInPort("in_NCUnit-3"));
				
				from = to;
			}
		}
		
//		System.out.println("}");
		
		this.addIC(Gen.getOutPort("out"), NOCs.get(1).getInPort("in_NCUnit-0"));
				
		//this.setSelectPriority();
	
	}

	@Override
	public void setSelectPriority() {
		
		
		/****************************************************************************************************/
		/**********************************************************| CONFLICTING SET |****|SELECT|*********** /
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(Gen, NQ0, NQ1) ),  	NQ0);
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(Gen, NQ0, NQ2) ),  	NQ0);
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(Gen, NQ1, NQ2) ),  	NQ1);
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(NQ0, NQ1, NQ2) ),  	NQ0);
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(Gen, NQ0) ), 	 	 	NQ0);
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(Gen, NQ1) ),  			NQ1);
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(Gen, NQ2) ), 			NQ2);
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(NQ0, NQ1) ), 			NQ0);
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(NQ0, NQ2) ), 			NQ0);
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(NQ1, NQ2) ), 			NQ1);
		/**********************************************************| CONFLICTING SET |****|SELECT|***********/
		/****************************************************************************************************/
		
	}
	
}
