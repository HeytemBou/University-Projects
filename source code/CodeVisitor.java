
public class CodeVisitor implements Fragment2Visitor{

	@Override
	public Object visit(SimpleNode node, Object data) {
		
		node.childrenAccept(this, data);
		return data;
	}

	

	

	@Override
	public Object visit(ASTsisinon node, Object data) {
		CodeGen dataCode = (CodeGen) data;
		if(!dataCode.isFuncHosted()) {
		//this is an if-else in the main program
		int tailleIncrease = 2;
		CodeGen expr = new CodeGen();
		CodeGen codeVrai = new CodeGen();
		CodeGen codeFaux = new CodeGen();
		
		
		
		node.jjtGetChild(0).jjtAccept(this, expr);
		node.jjtGetChild(1).jjtAccept(this, codeVrai);
		node.jjtGetChild(2).jjtAccept(this, codeFaux);
		
		String codeConJump = String.format("ConJump %d \n", codeVrai.getTaille()+1);
		String codeJump = String.format("Jump %d \n", codeFaux.getTaille());
		
		
		dataCode.appendCode(expr.getCodeGen(), expr.getTaille());
		dataCode.appendCode(codeConJump);
		dataCode.appendCode(codeVrai.getCodeGen(),codeVrai.getTaille());
		dataCode.appendCodeHead(codeVrai.getCodeHead());
		dataCode.appendCode(codeJump);
		dataCode.appendCode(codeFaux.getCodeGen(), codeFaux.getTaille());
		dataCode.appendCodeHead(codeFaux.getCodeHead());
		dataCode.increaseTaille(tailleIncrease);
		}else {
			//this is an if-else inside of a function
			int tailleIncrease = 2;
			CodeGen expr = new CodeGen();
			CodeGen codeVrai = new CodeGen();
			CodeGen codeFaux = new CodeGen();
			expr.setFuncHosted(true);
			codeVrai.setFuncHosted(true);
			codeFaux.setFuncHosted(true);
			
			node.jjtGetChild(0).jjtAccept(this, expr);
			node.jjtGetChild(1).jjtAccept(this, codeVrai);
			node.jjtGetChild(2).jjtAccept(this, codeFaux);
			
			String codeConJump = String.format("ConJump %d \n", codeVrai.getFuncBodyTaille()+1);
			String codeJump = String.format("Jump %d \n", codeFaux.getFuncBodyTaille());
			dataCode.appendBodyFunction(expr.getFunctionBody(), expr.getFuncBodyTaille());
			dataCode.appendBodyFunction(codeConJump,1);
			dataCode.appendBodyFunction(codeVrai.getFunctionBody(), codeVrai.getFuncBodyTaille());
			dataCode.appendHeadFunction(codeVrai.getFunctionHead(),codeVrai.getFunHeadTaille());
			dataCode.appendBodyFunction(codeJump, 1);
			dataCode.appendBodyFunction(codeFaux.getFunctionBody(), codeFaux.getFuncBodyTaille());
			dataCode.appendHeadFunction(codeFaux.getFunctionHead(),codeFaux.getFunHeadTaille() );
			dataCode.increaseFuncBodyTaille(tailleIncrease);
			
			
		}
		
		return data;
	}

	@Override
	public Object visit(ASTfaire node, Object data) {
		CodeGen dataCode = (CodeGen) data;
		if(!dataCode.isFuncHosted()) {
			//code not hosted
		int tailleIncrease = 1;
		
		CodeGen codeCommande = new CodeGen();
		CodeGen codeExpression = new CodeGen();
		
		
		
		node.jjtGetChild(0).jjtAccept(this, codeCommande);
		node.jjtGetChild(1).jjtAccept(this, codeExpression);
		
		String codeConJump = String.format("Conjump %d \n", 
				-(codeCommande.getTaille()+codeExpression.getTaille()+1));
		
		dataCode.appendCode(codeCommande);
		dataCode.appendCodeHead(codeCommande.getCodeHead());
		dataCode.appendCode(codeExpression);
		dataCode.appendCode(codeConJump, tailleIncrease);
		}else {
			//code hosted inside a function
			int tailleIncrease = 1 ;
			CodeGen codeCommande = new CodeGen();
			CodeGen codeExpression = new CodeGen();
			codeCommande.setFuncHosted(true);
			codeExpression.setFuncHosted(true);
			node.jjtGetChild(0).jjtAccept(this, codeCommande);
			node.jjtGetChild(1).jjtAccept(this, codeExpression);
			
			String codeConJump = String.format("Conjump %d \n", 
					-(codeCommande.getFuncBodyTaille()+codeExpression.getFuncBodyTaille()+1));
			dataCode.appendBodyFunction(codeCommande.getFunctionBody(), codeCommande.getFuncBodyTaille());
			
			dataCode.appendHeadFunction(codeCommande.getFunctionHead(), codeCommande.getFunHeadTaille());
			dataCode.appendBodyFunction(codeExpression.getFunctionBody(), codeExpression.getFuncBodyTaille());
			dataCode.appendBodyFunction(codeConJump, 1);
			
			
		}
		
		return data;
	}

	@Override
	public Object visit(ASTtantque node, Object data) {
		int tailleIncrease = 2;
		CodeGen dataCode = (CodeGen) data;
		if(dataCode.isFuncHosted()) {
			CodeGen codeCommande = new CodeGen();
			CodeGen codeExpression = new CodeGen();
			
			codeCommande.setFuncHosted(true);
			codeExpression.setFuncHosted(true);
			
			node.jjtGetChild(0).jjtAccept(this, codeExpression);
			node.jjtGetChild(1).jjtAccept(this, codeCommande);
			
			String codeJumpEnd = String.format("Jump %d \n", 
					-(codeCommande.getFuncBodyTaille()+codeExpression.getFuncBodyTaille()+2));
			
			String codeConJumpStart = String.format("ConJump %d\n", 
					codeCommande.getFuncBodyTaille());
			
			dataCode.appendBodyFunction(codeExpression.getFunctionBody(), codeExpression.getFuncBodyTaille());
			dataCode.appendBodyFunction(codeConJumpStart, 1);
			dataCode.appendBodyFunction(codeCommande.getFunctionBody(), codeCommande.getFuncBodyTaille());
			dataCode.appendHeadFunction(codeCommande.getFunctionHead(), codeCommande.getFunHeadTaille());
			dataCode.appendBodyFunction(codeJumpEnd, 1);
			
			
			
			
			
		}else {
			CodeGen codeCommande = new CodeGen();
			CodeGen codeExpression = new CodeGen();
			
			
			
			node.jjtGetChild(0).jjtAccept(this, codeExpression);
			node.jjtGetChild(1).jjtAccept(this, codeCommande);
			
			String codeJumpEnd = String.format("Jump %d \n", 
					-(codeCommande.getTaille()+codeExpression.getTaille()+2));
			
			String codeConJumpStart = String.format("ConJump %d\n", 
					codeCommande.getTaille()+1);
			
			
			dataCode.appendCode(codeExpression);
			dataCode.appendCode(codeConJumpStart);
			dataCode.appendCode(codeCommande);
			dataCode.appendCodeHead(codeCommande.getCodeHead());
			dataCode.appendCode(codeJumpEnd);
			
			dataCode.increaseTaille(tailleIncrease);
		}
		
		
		return data;
	}

	@Override
	public Object visit(ASTpour node, Object data) {
		int tailleIncrease = 2;
		CodeGen codeData = (CodeGen) data;
		if(codeData.isFuncHosted()) {
			CodeGen codeInit = new CodeGen();
			CodeGen codeCondition = new CodeGen();
			CodeGen codeRecc = new CodeGen();
			CodeGen codeCommande = new CodeGen();
			
			codeInit.setFuncHosted(true);
			codeCondition.setFuncHosted(true);
			codeRecc.setFuncHosted(true);
			codeCommande.setFuncHosted(true);
			
			node.jjtGetChild(0).jjtAccept(this, codeInit);
			node.jjtGetChild(1).jjtAccept(this, codeCondition);
			node.jjtGetChild(2).jjtAccept(this, codeRecc);
			node.jjtGetChild(3).jjtAccept(this, codeCommande);
			
			String codeConJumpStart = String.format("ConJump %d\n", 
					codeRecc.getFuncBodyTaille()+codeCommande.getFuncBodyTaille()+1);
			
			String codeJumpEnd = String.format("Jump %d\n", 
					-(codeCommande.getFuncBodyTaille()+codeRecc.getFuncBodyTaille()+codeCondition.getFuncBodyTaille()+2));
			codeData.appendBodyFunction(codeInit.getFunctionBody(), codeInit.getFuncBodyTaille());
			codeData.appendHeadFunction(codeInit.getFunctionHead(), codeInit.getFunHeadTaille());
			
			codeData.appendBodyFunction(codeCondition.getFunctionBody(), codeCondition.getFuncBodyTaille());
			codeData.appendBodyFunction(codeConJumpStart, 1);
			
			codeData.appendBodyFunction(codeCommande.getFunctionBody(), codeCommande.getFuncBodyTaille());
			codeData.appendHeadFunction(codeCommande.getFunctionHead(), codeCommande.getFunHeadTaille());
			
			codeData.appendBodyFunction(codeRecc.getFunctionBody(),codeRecc.getFuncBodyTaille());
			
			codeData.appendBodyFunction(codeJumpEnd, 1);
			
			
			
		}else {
			CodeGen codeInit = new CodeGen();
			CodeGen codeCondition = new CodeGen();
			CodeGen codeRecc = new CodeGen();
			CodeGen codeCommande = new CodeGen();
			node.jjtGetChild(0).jjtAccept(this, codeInit);
			node.jjtGetChild(1).jjtAccept(this, codeCondition);
			node.jjtGetChild(2).jjtAccept(this, codeRecc);
			node.jjtGetChild(3).jjtAccept(this, codeCommande);
			
			
			String codeConJumpStart = String.format("ConJump %d\n", 
					codeRecc.getTaille()+codeCommande.getTaille()+1);
			
			String codeJumpEnd = String.format("Jump %d\n", 
					-(codeCommande.getTaille()+codeRecc.getTaille()+codeCondition.getTaille()+2));
			
			codeData.appendCode(codeInit);
			codeData.appendCodeHead(codeInit.getCodeHead());
			codeData.appendCode(codeCondition);
			codeData.appendCode(codeConJumpStart);
			codeData.appendCode(codeCommande);
			codeData.appendCode(codeRecc);
			codeData.appendCodeHead(codeCommande.getCodeHead());
			codeData.appendCode(codeJumpEnd);
			
			codeData.increaseTaille(tailleIncrease);
		}
		
		return data;
	}

	

	@Override
	public Object visit(ASTcond_v node, Object data) {
		int tailleIncrease = 1;
		CodeGen fullCode = (CodeGen)data;
		if(fullCode.isFuncHosted()) {
			CodeGen codeVrai = new CodeGen();
			codeVrai.setFuncHosted(true);
			data = node.childrenAccept(this, codeVrai);
		    String codeConJump = String.format("ConJump %d \n", codeVrai.getFuncBodyTaille()+1);
		    fullCode.appendBodyFunction(codeConJump,0);
		    fullCode.appendBodyFunction(codeVrai.getFunctionBody(),tailleIncrease);
		    			
		}else {
			CodeGen codeVrai = new CodeGen();
			data = node.childrenAccept(this, codeVrai);
			
			String codeConJump = String.format("ConJump %d \n", codeVrai.getTaille()+1);
			fullCode.appendCode(codeConJump);
			fullCode.appendCode(codeVrai.getCodeGen());
			
			
			fullCode.increaseTaille(tailleIncrease);
			
		}
		
		    
		    return data;
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object visit(ASTcond_f node, Object data) {
		int tailleIncrease = 1;
		
		
		CodeGen fullCode = (CodeGen) data;
		if(fullCode.isFuncHosted()) {
			CodeGen codeFaux = new CodeGen();
			codeFaux.setFuncHosted(true);
			data = node.childrenAccept(this, codeFaux);
			String codeJump = String.format("Jump %d \n", codeFaux.getFuncBodyTaille());
			fullCode.appendBodyFunction(codeJump,0);
			fullCode.appendBodyFunction(codeFaux.getFunctionBody(), tailleIncrease);
			
			
			
		}else {
			CodeGen codeFaux = new CodeGen();
		    data = node.childrenAccept(this, codeFaux);
		    String codeJump = String.format("Jump %d \n", codeFaux.getTaille());
		    
		    fullCode.appendCode(codeJump);
	        fullCode.appendCode(codeFaux.getCodeGen());
	        
	        fullCode.increaseTaille(tailleIncrease);
		}
		
        
	    return data;
	}

	@Override
	public Object visit(ASTop_ou node, Object data) {
		node.childrenAccept(this,data);
        
		int tailleIncrease = 7;
		String code = "ConJump 2\n"
				+ "CstBo True\n"
				+ "Jump 4\n"
				+ "ConJump 2\n"
				+ "CstBo True\n"
				+ "Jump 1\n"
				+ "CstBo False\n";
		CodeGen dataCode = (CodeGen) data;
		if(dataCode.isFuncHosted()) {
			dataCode.appendBodyFunction(code, tailleIncrease);
		}else {
			dataCode.appendCode(dataCode);
		}
		dataCode.appendCode(code, tailleIncrease);
		return data;
	}

	@Override
	public Object visit(ASTop_et node, Object data) {
		node.childrenAccept(this,data);

		int tailleIncrease = 5;
		String code = "ConJump 3\n"
				+ "ConJump 2\n"
				+ "CstBo True\n"
				+ "Jump 1\n"
				+ "CstBo False\n";
		CodeGen dataCode = (CodeGen) data;
		if(dataCode.isFuncHosted()) {
			dataCode.appendBodyFunction(code, tailleIncrease);
		}else {
			dataCode.appendCode(dataCode);
		}
		dataCode.appendCode(code, tailleIncrease);
		return data;
	}

	@Override
	public Object visit(ASTteste_egalite node, Object data) {
		node.childrenAccept(this,data);

		int tailleIncrease = 1;
		String code = "Equal \n";
		CodeGen dataCode = (CodeGen) data;
		if(dataCode.isFuncHosted()) {
			dataCode.appendBodyFunction(code, tailleIncrease);
		}else {
			dataCode.appendCode(dataCode);
		}
		dataCode.appendCode(code, tailleIncrease);
		return data;
	}

	@Override
	public Object visit(ASTteste_diff node, Object data) {
		node.childrenAccept(this,data);

		int tailleIncrease = 1;
		String code = "NotEq \n";
		CodeGen dataCode = (CodeGen) data;
		if(dataCode.isFuncHosted()) {
			dataCode.appendBodyFunction(code, tailleIncrease);
		}else {
			dataCode.appendCode(code);
		}
		dataCode.appendCode(code, tailleIncrease);
		return data;
	}

	@Override
	public Object visit(ASTteste_sup node, Object data) {
		node.childrenAccept(this,data);

		int tailleIncrease = 1;
		String code = "GreStR \n";
		CodeGen dataCode = (CodeGen) data;
		if(dataCode.isFuncHosted()) {
			dataCode.appendBodyFunction(code, tailleIncrease);
		}else {
			dataCode.appendCode(dataCode);
		}
		dataCode.appendCode(code, tailleIncrease);
		return data;
	}

	@Override
	public Object visit(ASTteste_inf node, Object data) {
		node.childrenAccept(this,data);

		int tailleIncrease = 1;
		String code = "LowStR \n";
		CodeGen dataCode = (CodeGen) data;
		if(dataCode.isFuncHosted()) {
			dataCode.appendBodyFunction(code, tailleIncrease);
		}else {
			dataCode.appendCode(dataCode);
		}
		dataCode.appendCode(code, tailleIncrease);
		return data;
	}

	@Override
	public Object visit(ASTop_add node, Object data) {
		node.childrenAccept(this,data);

		int tailleIncrease = 1;
		String code = "AddRe \n";
		CodeGen dataCode = (CodeGen) data;
		if(dataCode.isFuncHosted()) {
			dataCode.appendBodyFunction(code, tailleIncrease);
		}else {
			dataCode.appendCode(code, tailleIncrease);
		}
		
		return data;
	}

	@Override
	public Object visit(ASTop_moins node, Object data) {
		node.childrenAccept(this,data);

		int tailleIncrease = 1;
		String code = "SubiRe \n";
		CodeGen dataCode = (CodeGen) data;
		if(dataCode.isFuncHosted()) {
			dataCode.appendBodyFunction(code, tailleIncrease);
		}else {
			dataCode.appendCode(code, tailleIncrease);
		}
		
		return data;
	}

	@Override
	public Object visit(ASTop_mult node, Object data) {
		node.childrenAccept(this,data);

		int tailleIncrease = 1;
		String code = "MultiRe \n";
		CodeGen dataCode = (CodeGen) data;
		if(dataCode.isFuncHosted()) {
			dataCode.appendBodyFunction(code, tailleIncrease);
			
		}else {
			dataCode.appendCode(code, tailleIncrease);
		}
		
		return data;
	}

	@Override
	public Object visit(ASTop_divi node, Object data) {
		node.childrenAccept(this,data);

		int tailleIncrease = 1;
		String code = "DiviRe \n";
		CodeGen dataCode = (CodeGen) data;
		if(dataCode.isFuncHosted()) {
			dataCode.appendBodyFunction(code, tailleIncrease);
		}else{
			
			dataCode.appendCode(code, tailleIncrease);
		}
		
		return data;
	}

	@Override
	public Object visit(ASTop_mod node, Object data) {
		node.childrenAccept(this,data);

		int tailleIncrease = 1;
		String code = "ModRe \n";
		CodeGen dataCode = (CodeGen) data;
		if(dataCode.isFuncHosted()) {
			dataCode.appendBodyFunction(code, tailleIncrease);
		}else {
			dataCode.appendCode(code, tailleIncrease);
		}
		
		return data;
	}

	@Override
	public Object visit(ASTneg node, Object data) {
		node.childrenAccept(this,data);

		int tailleIncrease = 1;
		String code = "NegRe \n";
		CodeGen dataCode = (CodeGen) data;
		if(dataCode.isFuncHosted()) {
			dataCode.appendBodyFunction(code, tailleIncrease);
		}else {
			dataCode.appendCode(code, tailleIncrease);
		}
		
		return data;
	}

	@Override
	public Object visit(ASTid node, Object data) {
		node.childrenAccept(this,data);

		int tailleIncrease = 1;
		String value = (String)node.value;
		String code = "GetVar "+value +"\n";
		CodeGen dataCode = (CodeGen) data;
		if(dataCode.isFuncHosted()) {
			dataCode.appendBodyFunction(code, tailleIncrease);
		}else {
			dataCode.appendCode(code);
			dataCode.increaseTaille(tailleIncrease);
		}
		
		
		return data;
	}

	@Override
	public Object visit(ASTnombre node, Object data) {
		
		node.childrenAccept(this,data);

		int tailleIncrease = 1;
		String value = (String)node.value;
		String code = "CstRe "+value +"\n";
		CodeGen dataCode = (CodeGen) data;
		if(dataCode.isFuncHosted()) {
			dataCode.appendBodyFunction(code, tailleIncrease);
			
		}else {
			dataCode.appendCode(code);
			dataCode.increaseTaille(tailleIncrease);
			
		}
		
		return data;
	}

	@Override
	public Object visit(ASTbooleen node, Object data) {
		node.childrenAccept(this,data);

		int tailleIncrease = 1;
		String codeValue ="";
		String value = (String)node.value;
		
		if(value.equals("Vrai"))
			codeValue = "True";
		else if(value.equals("Faux"))
			codeValue = "False";
		
		String code = "CstBo "+codeValue +"\n";
		CodeGen dataCode = (CodeGen) data;
		if(dataCode.isFuncHosted()) {
			dataCode.appendBodyFunction(code, tailleIncrease);
		}else {
			dataCode.appendCode(code);
			dataCode.increaseTaille(tailleIncrease);
		}
		
		
		return data;
	}

	@Override
	public Object visit(ASTnegbool node, Object data) {
		node.childrenAccept(this,data);

		int tailleIncrease = 1;
		String code = "Not \n";
		CodeGen dataCode = (CodeGen) data;
		if(dataCode.isFuncHosted()) {
			dataCode.appendBodyFunction(code, tailleIncrease);
		}else {
			dataCode.appendCode(code,tailleIncrease);
		}
		
		dataCode.appendCode(code, tailleIncrease);
		return data;
	}

	@Override
	public Object visit(ASTtypeof node, Object data) {
		node.childrenAccept(this,data);
		
		int tailleIncrease = 1;
		String code = "Typeof \n";
		CodeGen dataCode = (CodeGen) data;
		if(dataCode.isFuncHosted()) {
			dataCode.appendBodyFunction(code, tailleIncrease);
		}else {
			dataCode.appendCode(code, tailleIncrease);
		}
		
		return data;
	}

	@Override
	public Object visit(ASTassigner node, Object data) {
		node.childrenAccept(this,data);
		
		int tailleIncrease = 2;
		String code = String.format("SetVar %s \nGetVar %s \n", 
				node.jjtGetValue(), node.jjtGetValue());
		CodeGen dataCode = (CodeGen) data;
		if(dataCode.isFuncHosted()) {
			dataCode.appendBodyFunction(code, tailleIncrease);
		}else {
		dataCode.appendCode(code, tailleIncrease);
		}
		return data;
	}

	@Override
	public Object visit(ASTecrire node, Object data) {
		node.childrenAccept(this, data);
		
		CodeGen dataCode = (CodeGen) data;
		int tailleIncrease = 1;
		String code = "Print\n";
		if(dataCode.isFuncHosted()) {
			dataCode.appendBodyFunction(code, tailleIncrease);
			
		}else {
			dataCode.appendCode(code, tailleIncrease);
		}
		
		
		
		return data;
	}

	@Override
	public Object visit(ASTteste_inf_ega node, Object data) {
		node.childrenAccept(this,data);

		int tailleIncrease = 1;
		String code = "LowEqR \n";
		CodeGen dataCode = (CodeGen) data;
		if(!dataCode.isFuncHosted()) {
			dataCode.appendCode(code,tailleIncrease);
		}else {
		 	dataCode.appendBodyFunction(code, tailleIncrease);
		}
		
		
		dataCode.appendCode(code, tailleIncrease);
		return data;
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object visit(ASTteste_sup_ega node, Object data) {
		node.childrenAccept(this,data);

		int tailleIncrease = 1;
		String code = "GreEqR  \n";
		CodeGen dataCode = (CodeGen) data;
		
		if(!dataCode.isFuncHosted()) {
		
		dataCode.appendCode(code, tailleIncrease);
		}else {
			dataCode.appendBodyFunction(code, tailleIncrease);
		}
		return data;
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object visit(ASTStart node, Object data) {
		node.childrenAccept(this, data);
		return data;
	}


	@Override
	public Object visit(ASTteste_double_ega node, Object data) {
		node.childrenAccept(this, data);
		int tailleIncrease = 1 ;
		String code ="";
		return data;
	}


	@Override
	public Object visit(ASTstring node, Object data) {
		int tailleIncrease = 1 ;
		node.childrenAccept(this, data);
		String code ="CstStr "+node.jjtGetValue()+" \n";
		CodeGen dataCode = (CodeGen)data;
		if(!dataCode.isFuncHosted()) {
		dataCode.appendCode(code, tailleIncrease);
		}else {
			dataCode.appendBodyFunction(code, tailleIncrease);
			
		}
		return data;
	}





	@Override
	public Object visit(ASTfonction_declaration node, Object data) {
		
		CodeGen argCode = new CodeGen();
		CodeGen dataCode =(CodeGen) data ;
		
		CodeGen bodyCode = new CodeGen();
		bodyCode.setFuncHosted(true);
		
		node.jjtGetChild(1).jjtAccept(this, bodyCode);
		node.jjtGetChild(0).jjtAccept(this, argCode);
		dataCode.incrementFuncCounter();
		String nomFonction = "DclVar "+node.jjtGetValue()+" \n"+"Lambda \n";
		String assFonction = "SetVar "+node.jjtGetValue()+" \n";
		//append the function name and arguments to the head of the program code
		//dataCode.appendCodeArguments(argCode.getCodeArguments());
		dataCode.appendCodeHead(nomFonction);
		dataCode.appendCodeHead(argCode.getCodeArguments());
		dataCode.appendCodeHead(assFonction);
		
		
		//make new function instance
		dataCode.makeANewFunction(bodyCode.getFunctionHead(),bodyCode.getFunctionBody(),bodyCode.getFunctionTaille(),bodyCode.getFunHeadTaille());
		return data;
	}
	

	@Override
	public Object visit(ASTarguments_list node, Object data) {
		node.childrenAccept(this, data);
		return data ;
	}

	@Override
	public Object visit(ASTarguments node, Object data) {
		node.childrenAccept(this, data);
		return data ;
	}
	@Override
	public Object visit(ASTargument node, Object data) {
		
		CodeGen dataCode = (CodeGen)data ;
		String code = "DclArg "+node.jjtGetValue()+" \n";
		dataCode.appendCodeArguments(code);
		node.childrenAccept(this, data);
		return data;
	}
	
	


	@Override
	public Object visit(ASTexpression node, Object data) {
		node.childrenAccept(this, data);
		return data;
	}





	@Override
	public Object visit(ASTdeclaration_init node, Object data) {
		
		int Increasetaille = 2 ;
		node.childrenAccept(this, data);
		CodeGen dataCode = (CodeGen)data ;
		String code = "DclVar "+node.jjtGetValue()+" \n";
		String init = "SetVar "+node.jjtGetValue()+" \n"+"GetVar "+node.jjtGetValue()+" \n";
		if(dataCode.isFuncHosted())	{
			dataCode.appendHeadFunction(code, 1);
			dataCode.appendBodyFunction(init, Increasetaille);
			}
		
		else {
			dataCode.appendCodeHead(code);
			dataCode.appendCode(init,Increasetaille);
			}
	
		
		return data;
	}





	@Override
	public Object visit(ASTdeclaration node, Object data) {
		
		
		node.childrenAccept(this, data);
		int increaseTaille= 1;
		CodeGen dataCode = (CodeGen)data;
		
		String code = "DclVar "+node.jjtGetValue()+" \n";
		if(!dataCode.isFuncHosted()) {
		dataCode.appendCodeHead(code);
		}else {
			dataCode.appendHeadFunction(code, 1);
			
		}
		
		return data;
	}





	@Override
	public Object visit(ASTappel node, Object data) {
		String s1 ="GetVar "+node.jjtGetValue()+" \n"+"StCall \n";
		String s2 ="Call \n";
		int tailleIncrease_part1 = 2 ;
		int tailleIncrease_part2 = 1 ;
		CodeGen dataCode =(CodeGen)data;
		if(!dataCode.isFuncHosted()) {
			//code in the main program
	    dataCode.appendCode(s1,tailleIncrease_part1);
		node.childrenAccept(this, data);
		dataCode.appendCode(s2, tailleIncrease_part2);
		}else {
			//code nested inside a function
			dataCode.appendBodyFunction(s1, tailleIncrease_part1);
			node.childrenAccept(this, dataCode);
			dataCode.appendBodyFunction(s2, tailleIncrease_part2);
			
			
		}
		
		return data;
	}


	@Override
	public Object visit(ASTargs_app node, Object data) {
		
		//node.childrenAccept(this, data);
		int increaseTaille = 1 ;
		String code = "SetArg \n";
		CodeGen dataCode = (CodeGen)data;
		if(dataCode.isFuncHosted()) {
			node.childrenAccept(this, data);
			dataCode.appendBodyFunction(code, increaseTaille);
			
		}else {
			
			node.childrenAccept(this, data);
		    dataCode.appendCode(code, increaseTaille);
		
		}
		
		return data;
	}


	@Override
	public Object visit(ASTcommand node, Object data) {
		node.childrenAccept(this, data);
		return data;
	}





	@Override
	public Object visit(ASTprogramme node, Object data) {
		node.childrenAccept(this, data);
		return data;
	}





	@Override
	public Object visit(ASTretourner node, Object data) {
		node.childrenAccept(this, data);
		CodeGen codeGen = (CodeGen)data;
		int tailleIncrease = 1 ;
		String code = "Return \n";
		if(codeGen.isFuncHosted()) {
			codeGen.appendBodyFunction(code, tailleIncrease);
		}else {
			codeGen.appendCode(code);
			codeGen.increaseTaille(tailleIncrease);
		}
		return data;
	}











	

	
}
