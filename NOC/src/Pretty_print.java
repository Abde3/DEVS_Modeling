public class Pretty_print {

	private static final Object syncObj = new Object();

	public static void trace(String src, String info)
	{

		boolean print = true;
		
		if (print) {

			synchronized( syncObj )
			{
				String src_padded = String.format(" %s", src).replace(' ', ' ');
				String info_padded = String.format(" %s", info).replace(' ', ' ');
				
				System.out.println(compute_space(src) + src_padded + " : " + info_padded);
			}
			
		}
	}


	private static String compute_space(String src) {
		String[] split = src.split("-");
		String position = split[0];
		Integer level	= Integer.parseInt(split[1]);

		String output = new String();
		
		switch (position) {
		case "queue":
		{
			output += "| Q  |";
			break;
		}

		case "switch":
		{
			output += "| SW |   |";
			break;
		}

		case "PE":
		{
			output += "| PE |   |   |";
			break;
		}

		default:
			break;
		}
		
		for (int i = 0; i < level; i++) {
			output += "   |   |   |   |";
		}
		
		
		return output;
	}


	public static void trace_err(String src, String info) 
	{
		synchronized( syncObj )
		{
			String src_padded = String.format("%8s", src).replace(' ', ' ');
			String info_padded = String.format("%8s", info).replace(' ', ' ');

			System.err.println(compute_space(src) + src_padded + " : " + info_padded);
		}

	}


}
