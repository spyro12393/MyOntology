package MyJenaProject;

public class ProjectDataReader {
	private String projectName = "";

	private String[] className;
	private int classNumber;
	private String[] superclassName;
	private String[] interfaceclassName;

	private String[] attributeName;
	private String[] attributeType;
	private String[] attributeIsPrimitiveType;
	private String[] attributeVisibility;

	private String[] methodName;
	private String[] methodType;
	private String[] methodIsPrimitiveType;
	private String[] methodVisibility;
	
	private String[] methodParameters;
	private String[] methodParameterType;
	private String[] methodParameterIsPrimitiveType;
	private String[] invokeMethod;
	private String[] invokeAttribute;

	public ProjectDataReader(String[] content) {
		projectName = content[0];
		for(int i = 1; i<= 2; i++){
			content[i] = content[i].substring(1, content[i].length() - 1);
		}		
		for (int i = 3; i <= 11; i++) {
			content[i] = content[i].substring(1, content[i].length() - 2);
		}
		for (int i = 12; i <= 16; i++) {
			content[i] = content[i].substring(1, content[i].length() - 3);
		}
		className = content[1].split(", ");
		classNumber = className.length;
		superclassName = content[2].split(", ");
		
		interfaceclassName = content[3].split("], ");
		attributeName = content[4].split("], ");
		attributeType = content[5].split("], ");
		attributeIsPrimitiveType = content[6].split("], ");
		attributeVisibility = content[7].split("], ");
		methodName = content[8].split("], ");
		methodType = content[9].split("], ");
		methodIsPrimitiveType = content[10].split("], ");
		methodVisibility = content[11].split("], ");
		
		methodParameters = content[12].split("]], ");
		methodParameterType = content[13].split("]], ");
		methodParameterIsPrimitiveType = content[14].split("]], ");
		invokeMethod = content[15].split("]], ");
		invokeAttribute = content[16].split("]], ");

	}
	public String getPackageName(){
		return projectName;
	}

	public String getClassName(int n) {
		return className[n];
	}
	public int getClassNumber(){
		return classNumber;
	}
	public String getSuperClassName(int n){
		return superclassName[n];
	}
	
	
	public String getInterfaceClassName(int n){
		return interfaceclassName[n];
	}
	public String getAttributeName(int n){
		return attributeName[n];
	}
	public String getAttributeType(int n){
		return attributeType[n];
	}
	public String getAttributeIsPrimitiveType(int n){
		return attributeIsPrimitiveType[n];
	}
	public String getAttributeVisibility(int n){
		return attributeVisibility[n];
	}
	public String getMethodName(int n){
		return methodName[n];
	}
	public String getMethodType(int n){
		return methodType[n];
	}
	public String getMethodIsPrimitiveType(int n){
		return methodIsPrimitiveType[n];
	}
	public String getMethodVisibility(int n){
		return methodVisibility[n];
	}
	
	public String getMethodParameters(int n){
		return methodParameters[n];
	}
	public String getMethodParameterType(int n){
		return methodParameterType[n];
	}
	public String getMethodParameterIsPrimitiveType(int n){
		return methodParameterIsPrimitiveType[n];
	}
	public String getInvokeMethod(int n){
		return invokeMethod[n];
	}
	public String getInvokeAttribute(int n){
		return invokeAttribute[n];
	}
	
}
