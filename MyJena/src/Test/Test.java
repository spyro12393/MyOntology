package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import MyJenaProject.MyJena;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] class_name;
		int classNumber = 0;

		String[][] attribute_name;
		String[][] attribute_type;
		String[][] attribute_isprimitivetype;
		String[][] attribute_visibility;

		String[][] method_name;
		String[][] method_return_type;
		String[][] method_return_isprimitivetype;
		String[][] method_vsibility;
		String[][][] method_parameters;
		String[][][] method_invocations;

		String fileName = "src/ATM.txt";
		String[] content = new String[11];
		
		MyJena jena_test = new MyJena();

		String[] tmp;
		String[] tmp2;
		String[] tmp3;
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

		content[0] = content[0].substring(1, content[0].length() - 1);
		System.out.println(content[0]);
		tmp = content[0].split(", ");
		// classNumber = tmp.length;
		class_name = new String[tmp.length];

		for (int i = 0; i < tmp.length; i++) {
			class_name[i] = tmp[i];
		}
		
		jena_test.setClassName(class_name);

		for (int i = 1; i <= 8; i++) {
			content[i] = content[i].substring(1, content[i].length() - 2);
		}
		tmp = content[1].split("], ");
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i].length() > 1)
				tmp[i] = tmp[i].substring(1, tmp[i].length());
			tmp2 = tmp[i].split(", ");
			attribute_name = new String[tmp.length][tmp2.length];
			for (int j = 0; j < tmp2.length; j++) {
				if (tmp2[j].length() > 1) {
					attribute_name[i][j] = tmp2[j];

				} else {
					attribute_name[i][j] = "null";
				}
				System.out.println(attribute_name[i][j]);
				
			}
			jena_test.setAttributeName(attribute_name);
		}
		tmp = content[2].split("], ");
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i].length() > 1)
				tmp[i] = tmp[i].substring(1, tmp[i].length());
			tmp2 = tmp[i].split(", ");
			attribute_type = new String[tmp.length][tmp2.length];
			for (int j = 0; j < tmp2.length; j++) {
				if (tmp2[j].length() > 1) {
					attribute_type[i][j] = tmp2[j];

				} else {
					attribute_type[i][j] = "null";
				}
				System.out.println(attribute_type[i][j]);
				jena_test.setAttributeType(attribute_type);
			}
		}
		tmp = content[3].split("], ");
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i].length() > 1)
				tmp[i] = tmp[i].substring(1, tmp[i].length());
			tmp2 = tmp[i].split(", ");
			attribute_isprimitivetype = new String[tmp.length][tmp2.length];
			for (int j = 0; j < tmp2.length; j++) {
				if (tmp2[j].length() > 1) {
					attribute_isprimitivetype[i][j] = tmp2[j];

				}
				System.out.println(attribute_isprimitivetype[i][j]);
				jena_test.setAttributeTypeisPri(attribute_isprimitivetype);
			}
		}
		tmp = content[4].split("], ");
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i].length() > 1)
				tmp[i] = tmp[i].substring(1, tmp[i].length());
			tmp2 = tmp[i].split(", ");
			attribute_visibility = new String[tmp.length][tmp2.length];
			for (int j = 0; j < tmp2.length; j++) {
				if (tmp2[j].length() > 1) {
					attribute_visibility[i][j] = tmp2[j];

				} else {
					attribute_visibility[i][j] = "null";
				}
				System.out.println(attribute_visibility[i][j]);
				jena_test.setAttributeVisibility(attribute_visibility);
			}
		}
		tmp = content[5].split("], ");
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i].length() > 1)
				tmp[i] = tmp[i].substring(1, tmp[i].length());
			tmp2 = tmp[i].split(", ");
			method_name = new String[tmp.length][tmp2.length];
			for (int j = 0; j < tmp2.length; j++) {
				if (tmp2[j].length() > 1) {
					method_name[i][j] = tmp2[j];

				} else {
					method_name[i][j] = "null";
				}
				System.out.println(method_name[i][j]);
				
			}
			jena_test.setMethodName(method_name);
		}
		tmp = content[6].split("], ");
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i].length() > 1)
				tmp[i] = tmp[i].substring(1, tmp[i].length());
			tmp2 = tmp[i].split(", ");
			method_return_type = new String[tmp.length][tmp2.length];
			for (int j = 0; j < tmp2.length; j++) {
				if (tmp2[j].length() > 1) {
					method_return_type[i][j] = tmp2[j];

				} else {
					method_return_type[i][j] = "null";
				}
				System.out.println(method_return_type[i][j]);
				jena_test.setMethodReturnType(method_return_type);
			}
		}
		tmp = content[7].split("], ");
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i].length() > 1)
				tmp[i] = tmp[i].substring(1, tmp[i].length());
			tmp2 = tmp[i].split(", ");
			method_return_isprimitivetype = new String[tmp.length][tmp2.length];
			for (int j = 0; j < tmp2.length; j++) {
				if (tmp2[j].length() > 1) {
					method_return_isprimitivetype[i][j] = tmp2[j];

				}
				System.out.println(method_return_isprimitivetype[i][j]);
				jena_test.setMethodReturnTypeisPri(method_return_isprimitivetype);
			}
		}
		tmp = content[8].split("], ");
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i].length() > 1)
				tmp[i] = tmp[i].substring(1, tmp[i].length());
			tmp2 = tmp[i].split(", ");
			method_vsibility = new String[tmp.length][tmp2.length];
			for (int j = 0; j < tmp2.length; j++) {
				if (tmp2[j].length() > 1) {
					method_vsibility[i][j] = tmp2[j];

				} else {
					method_vsibility[i][j] = "null";
				}
				System.out.println(method_vsibility[i][j]);
				jena_test.setMethodVisibility(method_vsibility);
			}
		}

		for (int i = 9; i <= 10; i++) {
			content[i] = content[i].substring(1, content[i].length() - 3);
		}

		tmp = content[9].split("]], ");
		for (int i = 0; i < tmp.length; i++) {

			// System.out.println(tmp[i]);
			if (tmp[i].length() > 1)
				tmp[i] = tmp[i].substring(1, tmp[i].length());
			tmp2 = tmp[i].split("], ");
			for (int j = 0; j < tmp2.length; j++) {
				// System.out.println(tmp2[j]);
				if (tmp2[j].length() > 1)
					tmp2[j] = tmp2[j].substring(1, tmp2[j].length());
				tmp3 = tmp2[j].split(", ");
				method_parameters = new String[tmp.length][tmp2.length][tmp3.length];
				for (int k = 0; k < tmp3.length; k++) {

					if (tmp3[k].length() > 1)
						method_parameters[i][j][k] = tmp3[k];
					else
						method_parameters[i][j][k] = "null";
					System.out.println(method_parameters[i][j][k]);
					jena_test.setParameters(method_parameters);
				}

			}

		}
		
		tmp = content[10].split("]], ");
		for (int i = 0; i < tmp.length; i++) {

			// System.out.println(tmp[i]);
			if (tmp[i].length() > 1)
				tmp[i] = tmp[i].substring(1, tmp[i].length());
			tmp2 = tmp[i].split("], ");
			for (int j = 0; j < tmp2.length; j++) {
				if (tmp2[j].length() > 1){
					tmp2[j] = tmp2[j].substring(1, tmp2[j].length());
				}
				tmp3 = tmp2[j].split(", ");
				method_invocations = new String[tmp.length][tmp2.length][tmp3.length];
				for (int k = 0; k < tmp3.length; k++) {

					if (tmp3[k].length() > 1)
						method_invocations[i][j][k] = tmp3[k];
					else
						method_invocations[i][j][k] = "null";
					System.out.println(method_invocations[i][j][k]);
					jena_test.setInvocations(method_invocations);
				}

			}

		}
		
		jena_test.createOntology();
		//jena_test.createOntology(class_name, attribute_name, attribute_type, attribute_isprimitivetype, attribute_visibility, method_name, method_return_type, method_return_isprimitivetype, method_vsibility, method_parameters, method_invocations);
	}
}
