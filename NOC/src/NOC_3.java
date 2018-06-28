import java.util.Arrays;
import java.util.Vector;

import DEVSModel.DEVSCoupled;
import DEVSModel.DEVSModel;

public class NOC_3 extends DEVSCoupled {

	private NOC_Unit NQ1;
	private NOC_Unit NQ2;
	private NOC_Unit NQ3;
	private Generator_Task Gen;

	
	public NOC_3(String name){
		
		super();
		
		this.name = name;
		NQ1 = new NOC_Unit("NQ", 1, 1);
		NQ2 = new NOC_Unit("NQ", 2, 1);
		NQ3 = new NOC_Unit("NQ", 3, 1);
		Gen = new Generator_Task("Gen");

		this.getSubModels().add(NQ1);
		this.getSubModels().add(NQ2);
		this.getSubModels().add(NQ3);
		this.getSubModels().add(Gen);
		
		
		this.addIC(NQ1.getOutPort("out_NCUnit"), NQ2.getInPort("in_NCUnit"));
		this.addIC(NQ2.getOutPort("out_NCUnit"), NQ3.getInPort("in_NCUnit"));
		this.addIC(NQ3.getOutPort("out_NCUnit"), NQ1.getInPort("in_NCUnit"));
		this.addIC(Gen.getOutPort("out"), 		 NQ1.getInPort("in_NCUnit"));
				
		this.setSelectPriority();
	
	}

	@Override
	public void setSelectPriority() {
		
		
		/****************************************************************************************************/
		/**********************************************************| CONFLICTING SET |****|SELECT|***********/
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(Gen, NQ1, NQ2) ),  	NQ1);
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(Gen, NQ1, NQ3) ),  	NQ1);
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(Gen, NQ2, NQ3) ),  	NQ2);
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(NQ1, NQ2, NQ3) ),  	NQ1);
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(Gen, NQ1) ), 	 	 	NQ1);
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(Gen, NQ2) ),  			NQ2);
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(Gen, NQ3) ), 			NQ3);
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(NQ1, NQ2) ), 			NQ1);
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(NQ1, NQ3) ), 			NQ1);
		this.selectPriority.put(new Vector<DEVSModel>( Arrays.asList(NQ2, NQ3) ), 			NQ2);
		/**********************************************************| CONFLICTING SET |****|SELECT|***********/
		/****************************************************************************************************/
		
	}

}
