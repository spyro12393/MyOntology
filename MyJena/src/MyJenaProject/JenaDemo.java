package MyJenaProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class JenaDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileName = "C:/Users/QZ_CHUNG/Desktop/alice/StaticData.txt";
		//String fileName2 = "C:/data/¼È¦s/DynamicData.csv";

		String[] content = new String[17];

		ProjectDataReader pdr;
		//ProjectDataReader2 pdr2 = new ProjectDataReader2(fileName2);

		MyOntology MyOnt = new MyOntology();

		try {
			File file = new File(fileName);
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			int i = 0;

			while (br.ready()) {
				content[i] = br.readLine();
				System.out.println(content[i]);
				i++;
			}
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		pdr = new ProjectDataReader(content);
		MyOnt.setPackage(pdr.getPackageName());
		for (int i = 0; i < pdr.getClassNumber(); i++) {
			MyOnt.setClass(pdr.getClassName(i), pdr.getSuperClassName(i), pdr.getInterfaceClassName(i));
			MyOnt.setAttribute(pdr.getAttributeName(i), pdr.getAttributeType(i), pdr.getAttributeIsPrimitiveType(i),
					pdr.getAttributeVisibility(i));
			MyOnt.setMethod(pdr.getMethodName(i), pdr.getMethodType(i), pdr.getMethodIsPrimitiveType(i),
					pdr.getMethodVisibility(i), pdr.getMethodParameters(i), pdr.getMethodParameterType(i),
					pdr.getMethodParameterIsPrimitiveType(i), pdr.getInvokeMethod(i), pdr.getInvokeAttribute(i));
			// String methodName, String methodType, String
			// methodIsPrimitiveType, String methodVisibility,
			// String parameters, String parameterType, String
			// parameterIsPrimitiveType, String invokeMethod,
			// String invokeAttribute
		}

		MyOnt.setGeneralization();
		MyOnt.setDescentMember();
		
		MyOnt.setCallMethod();
		MyOnt.setCallAttribute();

		MyOnt.setReference();		

		MyOnt.setClassCoupling();

		MyOnt.WriteModel();

	}

}
