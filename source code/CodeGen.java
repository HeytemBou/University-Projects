import java.util.*;
public class CodeGen {
	
	private ArrayList<Object> fonctions = new ArrayList<Object>();	
	
	
	
	private boolean funcHosted = false ;
	
	
	//reserved for body code
	public boolean isFuncHosted() {
		return funcHosted;
	}

	public void setFuncHosted(boolean funcHosted) {
		this.funcHosted = funcHosted;
	}

	private String codeGen="";
	
	
	
	private int funcBodyTaille =0 ;
	private int funHeadTaille =0 ;
	
	

	public int getFunHeadTaille() {
		return funHeadTaille;
	}

	public void setFunHeadTaille(int funHeadTaille) {
		this.funHeadTaille = funHeadTaille;
	}

	public ArrayList<Object> getFonctions() {
		return fonctions;
	}

	public void setFonctions(ArrayList<Object> fonctions) {
		this.fonctions = fonctions;
	}

	public void increaseFuncBodyTaille(int taille) {
		this.funcBodyTaille+=taille;
	}
	public int getFuncBodyTaille() {
		return funcBodyTaille;
	}

	public void setFuncBodyTaille(int funcBodyTaille) {
		this.funcBodyTaille = funcBodyTaille;
	}

	//reserved for variables declarations
	private String codeHead="" ;
	
	//reserved for functions declarations
	private String codeFonctions="";
	
	//reserved for lambdas
	private String codeLambda="" ;
	
	//reserved for arguments declaration ;
	private String codeArguments="" ;
	
	//size of declarations section
	private int codeArgumentsTaille=0;
	
	public int getCodeArgumentsTaille() {
		return codeArgumentsTaille;
	}

	public void setCodeArgumentsTaille(int codeArgumentsTaille) {
		this.codeArgumentsTaille = codeArgumentsTaille;
	}

	private int taille;
	
	public CodeGen() {
		this.codeGen = "";
		this.taille = 0;
	}
	
	public String getCodeGen() {
		return codeGen;
	}
	
	public void setCodeGen(String codeGen) {
		this.codeGen = codeGen;
	}
	
	public int getTaille() {
		return taille;
	}
	
	public void setTaille(int taille) {
		this.taille = taille;
	}
	
	public void appendCode(String code) {
		this.codeGen = this.codeGen+code;
	}
	
	public void increaseTaille(int increase) {
		this.taille+=increase;
	}
	
	public void appendCode(String code, int increase) {
		this.appendCode(code);
		this.increaseTaille(increase);
	}
	public void appendCodeHead(String code ) {
		this.codeHead = this.codeHead + code;
	}
	
	public void appendCodeFonctions(String code) {
		this.codeFonctions = this.codeFonctions + code ;
	}
	public void appendCodeArguments(String code) {
		this.codeArguments = this.codeArguments+code  ;
	}
	
	public String getCodeHead() {
		return codeHead;
	}

	public void setCodeHead(String codeHead) {
		this.codeHead = codeHead;
	}

	public String getCodeFonctions() {
		return codeFonctions;
	}

	public void setCodeFonctions(String codeFonctions) {
		this.codeFonctions = codeFonctions;
	}

	public String getCodeLambda() {
		return codeLambda;
	}

	public void setCodeLambda(String codeLambda) {
		this.codeLambda = codeLambda;
	}

	public String getCodeArguments() {
		return codeArguments;
	}

	public void setCodeArguments(String codeArguments) {
		this.codeArguments = codeArguments;
	}

	public void appendCode(CodeGen codeObj) {
		this.appendCode(codeObj.getCodeGen());
		this.increaseTaille(codeObj.getTaille());
	}
	public String getAllGenCode() {
		return this.codeHead+this.codeLambda+this.codeArguments+this.codeGen+this.codeFonctions;
	}
	
	
	private String functionHead="" ;
	private String functionBody="" ;
	private int functionTaille ;
	
	public String getFunctionHead() {
		return functionHead;
	}

	public void setFunctionHead(String functionHead) {
		this.functionHead = functionHead;
	}

	public String getFunctionBody() {
		return functionBody;
	}

	public void setFunctionBody(String functionBody) {
		this.functionBody = functionBody;
	}

	public int getFunctionTaille() {
		return functionTaille;
	}

	public void setFunctionTaille(int functionTaille) {
		this.functionTaille = functionTaille;
	}

	public String generateLambdaExpression() {
		int lambdaCount = this.fonctions.size()/3;
		
		return "" ;
	}
	public void makeANewFunction(String functionHead , String functionBody , int functionTaille,int funHeadTaille) {
		this.fonctions.add(functionHead + functionBody);
		this.fonctions.add(functionTaille);
		this.fonctions.add(funHeadTaille);
	}
	public void appendBodyFunction(String code , int bodyFunTaille) {
		this.functionBody = this.functionBody+code ;
		this.increaseFuncBodyTaille(bodyFunTaille);
	}
	public void appendHeadFunction(String code,int headFunTaille) {
		this.functionHead= this.functionHead+code ;
		this.funHeadTaille = this.funHeadTaille + headFunTaille ;
	}
	public String getFunctionsCode() {
		String Str ="";
		for(int i=0;i<fonctions.size();i=i+3) {
			    if(fonctions.get(i)==null)break;
			    else
				Str = Str + (String)fonctions.get(i);
			
		}
		return Str ;
	}
	
	public String printCode() {
		String Str =this.codeHead+this.codeArguments+this.codeGen+this.getFunctionsCode()+"Halt \n";
		System.out.println(Str);
		return Str ;
	}
	
	
	
	public static int funCounter = 0 ;
	public static int headOffSet = 0 ;
	public boolean lambdaIndicator = false ;
	public static int argumentsCount = 0 ;
	
	
	
	
	public void incrementArg() {
		argumentsCount++;
	}
	public static int getArgumentsCount() {
		return argumentsCount;
	}

	public static void setArgumentsCount(int argumentsCount) {
		CodeGen.argumentsCount = argumentsCount;
	}

	public boolean isLambdaIndicator() {
		return lambdaIndicator;
	}
	
	

	public void setLambdaIndicator(boolean lambdaIndicator) {
		this.lambdaIndicator = lambdaIndicator;
	}

	public void incrementFuncCounter() {
		funCounter++;
	}
	
	public static int getHeadOffSet() {
		return headOffSet;
	}

	public static void setHeadOffSet(int headOffSet) {
		CodeGen.headOffSet = headOffSet;
	}

	public static int getFunCounter() {
		return funCounter;
	}

	public static void setFunCounter(int funDecCounter) {
		CodeGen.funCounter = funDecCounter;
	}

	
}
