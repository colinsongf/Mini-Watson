/*
 * This file contains methods defined for every rule and their semantic attachments behavior.
 */

package nlpProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.stanford.nlp.trees.Tree;
import stanfordCoreNlp.StanfordParser;

public class SemanticAttachment {

	private StanfordParser parser;
	private HashMap<String, Integer> ruleId;
	String SELECT;
	String FROM;
	String WHERE;

	public interface SqlBuilder {
		String buildSql (String x, String y);
	}

	public SemanticAttachment() {
		parser = new StanfordParser();
		ruleId = new HashMap<String, Integer>();
		populateRulesInMap();
	}
	
	
	/*
	 * List of rules in the grammar.
	 */
	
	public void populateRulesInMap() {
		ruleId.put("SQ -> VBZ NP NP .", 1);
		ruleId.put("NP -> NNP", 2);
		ruleId.put("NP -> DT NN", 3);
		ruleId.put("SQ -> VBZ NP ADVP NP .", 4);
		ruleId.put("ADVP -> IN", 5);
		ruleId.put("SQ -> VBD NP VP .", 6);
		ruleId.put("VP -> VBN PP", 7);
		ruleId.put("PP -> IN NP", 8);
		ruleId.put("SQ -> VBD NP PP .", 9);
		ruleId.put("NP -> DT JJS NN", 10);
		ruleId.put("PP -> NP PP", 11);
		ruleId.put("NP -> CD", 12);
		ruleId.put("NP -> NP NN", 13);
		ruleId.put("NP -> NNP POS", 14);
		ruleId.put("VP -> VB PP", 15);
		ruleId.put("VP -> VB NP", 16);
		ruleId.put("NP -> NP PP", 17);
		ruleId.put("NP -> DT JJ NN", 18);
		ruleId.put("S -> S VP .", 19);
		ruleId.put("S -> VP", 20);
		ruleId.put("VP -> VBP NP", 21);
		ruleId.put("VP -> VBD NP PP", 22);
		ruleId.put("NP -> JJS NN", 23);
		ruleId.put("SBARQ -> WHNP SQ .", 24);
		ruleId.put("SQ -> VP", 25);
		ruleId.put("WHNP -> WP", 26);
		ruleId.put("VP -> VBD NP", 27);
		ruleId.put("VP -> VBD NP PP PP", 28);
		ruleId.put("WHNP -> WDT NN", 29);
		ruleId.put("SBARQ -> WHADVP SQ .", 30);
		ruleId.put("WHADVP -> WRB", 31);
		ruleId.put("SQ -> VBD NP VP", 32);
	}
	
	/*
	 * This method generates all the rules for the given question by parsing the tree.
	 */
	
	public void generateGrammarFromParseTree(String s) {

		int nodeNum = 0;

		String rule="";
		Tree t = parser.parse(s).get(0);

		Queue<Tree> treeQueue= new LinkedList<Tree>();
		Queue<String> prodQueue = new LinkedList<String>();


		List<String> rulesList = new ArrayList<String>();
		Tree[] child = t.children();

		treeQueue.add(child[0]);
		nodeNum++;
		prodQueue.add(child[0].label()+""+nodeNum+"");


		while(!treeQueue.isEmpty()) {
			rule = "";
			Tree tree = treeQueue.remove();
			Tree[] children = tree.children();
			rule = rule + prodQueue.remove()+" -> ";
			for(int i=0;i<children.length;i++) {
				if(!tree.isPreTerminal()) {
					treeQueue.add(children[i]);

					nodeNum++;
					prodQueue.add(children[i].label()+""+nodeNum);
				}
				if(!tree.isPreTerminal()) {
					rule = rule + children[i].label()+""+nodeNum+" ";
				}
				else {
					rule = rule + children[i].yieldHasWord().get(0).word();
				}


			}
			System.out.println(rule);
		}
	}
	
	/*
	 * Utility function to retrieve children for a given Node. 
	 */
	public String UtilFunction(Tree tree){
		String s = "";
		
		Tree child [] = tree.children();
		for(int i = 0; i < child.length; i++){
			if(!child[i].isPreTerminal()){
				s += attachSemantics(getRule(child[i]), child[i]);
			}else{
				s += "Reached Terminal ->";
				s += child[i].yieldHasWord().get(0).word() +"\n";				
			}
		}
		return s;
	}
	
	/*
	 * Methods created for every rule in the grammar.
	 */
	
	public String Sq_Vbz_Np_Np(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Sq_Vbz_Np_Advp_Np(Tree tree) {
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Sq_Vbd_Np_Vp(Tree tree) {
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	public String Sq_Vbd_Np_Pp(Tree tree) {
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String S_S_Vp(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String S_Vp(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Sbarq_Whnp_Sq(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Sbarq_Whadvp_Sq(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Sq_Vp(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Sq_Vbd_Np_Vp_1(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Whnp_Wp(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Np_Nnp(Tree tree) {
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Np_Dt_Nn(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Advp(Tree tree) {
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Vp_Vbn_Pp(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	public String Pp_In_Np(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	public String Np_Dt_Jjs_Nn(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Pp_Np_Pp(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Np_Np_Nn(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	public String Np_Cd(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Np_Nnp_Pos(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Vp_Vb_Pp(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Vp_Vb_Np(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}

	public String Np_Np_Pp(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Np_Dt_Jj_Nn(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Vp_Vbp_Np(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Vp_Vbd_Np_Pp(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Np_Jjs_Nn(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Vp_Vbd_Np(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Vp_Vbd_Np_Pp_Pp(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Whnp_Wdt_Nn(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String Whadvp_Wrb(Tree tree){
		String s = "";
		s = UtilFunction(tree);
		return s;
	}
	
	public String attachSemantics(int n, Tree tree){
		
		String s = "";
		switch(n){
		case 1: 
				s = Sq_Vbz_Np_Np(tree);
				System.out.println(s);
				break;
				
		case 2:
				s = Np_Nnp(tree);
				break;
		case 3: 
				s = Np_Dt_Nn(tree);
				break;
		case 4: 
				s = Sq_Vbz_Np_Advp_Np(tree);
				System.out.println(s);
				break;
		case 5: 
				s = Advp(tree);
				break;
		case 6: 
				s = Sq_Vbd_Np_Vp(tree);
				System.out.println(s);
				break;
		case 7:
				s = Vp_Vbn_Pp(tree);
				break;
		case 8:
				s = Pp_In_Np(tree);
				break;
		case 9:
				s = Sq_Vbd_Np_Pp(tree);
				System.out.println(s);
				break;
		case 10: 
				s = Np_Dt_Jjs_Nn(tree);
				break;
		case 11:
				s = Pp_Np_Pp(tree);
				break;
		case 12:
				s = Np_Cd(tree);
				break;
		case 13:
				s = Np_Np_Nn(tree);
				break;
		case 14: 
				s = Np_Nnp_Pos(tree);
				break;
		case 15:
				s = Vp_Vb_Pp(tree);
				break;
		case 16:
				s = Vp_Vb_Np(tree);
				break;
		case 17:
				s = Np_Np_Pp(tree);
				break;
		case 18:
				s = Np_Dt_Jj_Nn(tree);
				break;
		case 19:
				s = S_S_Vp(tree);
				System.out.println(s);
				break;
		case 20:
				s = S_Vp(tree);
				break;
		case 21:
				s = Vp_Vbp_Np(tree);
				break;
		case 22:
				s = Vp_Vbd_Np_Pp(tree);
				break;
		case 23:
				s = Np_Jjs_Nn(tree);
				break;
		case 24:
				s = Sbarq_Whnp_Sq(tree);
				System.out.println(s);
				break;
		case 25:
				s = Sq_Vp(tree);
				break;
		case 26:
				s = Whnp_Wp(tree);
				break;
		case 27:
				s = Vp_Vbd_Np(tree);
				break;
		case 28:
				s = Vp_Vbd_Np_Pp_Pp(tree);
				break;
		case 29:
				s = Whnp_Wdt_Nn(tree);
				break;
		case 30:
				s = Sbarq_Whadvp_Sq(tree);
				System.out.println(s);
				break;
		case 31:
				s = Whadvp_Wrb(tree);
				break;
		case 32:
				s = Sq_Vbd_Np_Vp_1(tree);
				break;
		 
		}
		
		return s;
	}
	
	
	public void parseTree(String s){
		Tree t = parser.parse(s).get(0);	
		attachSemantics(getRule(t.getChild(0)), t.getChild(0));
	}
	
	
	public int getRule(Tree t){
		String rule = t.label().toString();
		rule = rule + " -> ";
		Tree[] children = t.children();
		for(int i=0;i<children.length;i++) {
			rule = rule + children[i].label() + " ";
		}
		rule = rule.trim();
		
		return ruleId.get(rule);
	}
}