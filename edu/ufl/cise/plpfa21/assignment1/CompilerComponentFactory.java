package edu.ufl.cise.plpfa21.assignment1;

import java.util.Arrays;

import edu.ufl.cise.plpfa21.assignment1.PLPTokenKinds.Kind;

public class CompilerComponentFactory{
	static public class Token implements IPLPToken {
		Kind token_kind;
		int token_pos;
		int token_length;
		int token_line;
		int token_posInLine;
		int num;
		int flag;
		String token_text;
		String errorMessage;
		Token next;
		
		public Token (Kind kind, int pos, int length, int line, int posInLine, String text){
			flag = 0;
			token_kind = kind;
			token_pos = pos;
			token_length = length;
			token_line = line;
			token_posInLine = posInLine;
			token_text = text;
			errorMessage = "";
			next = null;
		}
		
		@Override
		public Kind getKind() {
			return token_kind;
		}


		@Override
		public String getText() {
			return token_text;
		}


		@Override
		public int getLine() {
			return token_line;
		}


		@Override
		public int getCharPositionInLine() {
			return token_posInLine;
		}


		@Override
		public String getStringValue() { 
			token_text = token_text.replaceAll("[\"]", "");  
			return null;
		}


		@Override
		public int getIntValue() {
			num = Integer.parseInt(token_text);
			return num;
		}
	}
	
	static public class Lexer implements IPLPLexer {
		Token head;
		Token current;
		public Lexer (){
			head = null;
			current = null;
		}
		
		public void add (Token t) {
			Token newToken  = new Token(t.token_kind, t.token_pos, t.token_length, t.token_line, t.token_posInLine, t.token_text);
			if(head == null) {
				head = newToken;
				current = head;
				return;
			}
			else {
				current = head;
				while(current.next != null) {
					current = current.next;
				}
					
				current.next = newToken;
			}
			
		}

		@Override
		public IPLPToken nextToken() throws LexicalException {
			if (current.next.token_kind == Kind.ERROR) {
				throw new LexicalException(current.errorMessage, current.token_line,current.token_posInLine);
			}
			else {
				current = current.next;
			}
			return current;
		}
	}
		

	static IPLPLexer getLexer(String input) {
		Lexer result;
		char[] chars; //holds characters with 0 at end
		char EOFchar = 0;
		int startPos = 0;
		int startPosInLine = 0;
		int pos = 0; // position in char array. Starts at zero
		int line = 1; // line number of token in source. Starts at 1
		int posInLine = 0; // position in line of source. Starts at 1
		String txt = ""; //temporary string to hold text of tokens
		int numChars = input.length();
		chars = Arrays.copyOf(input.toCharArray(), numChars + 1); 
		chars[numChars] = EOFchar;
		
		result = new Lexer();
		result.head = new Token(Kind.SEMI, 999, 999, 999, 999, "DUMMYHEAD");
		enum State {START, HAVE_EQUAL, DIGITS, IDENT_PART}
		
		State state = State.START;
		
		while(pos < chars.length) {
			char ch = chars[pos]; //get current char
			switch (state) {
				case START-> {
					startPos = pos;
					startPosInLine = posInLine;
					switch (ch) {
						case ' ', '\t'-> {
							pos++;
							posInLine++;
						}
						case '\n'-> {
							pos++;
							line++;
							posInLine = 0;
						}
						case '('-> {
							txt = "(";
							result.add(new Token(Kind.LPAREN, startPos, 1, line, posInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case ')'-> {
							txt = ")";
							result.add(new Token(Kind.RPAREN, startPos, 1, line, posInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case '['-> {
							txt = "[";
							result.add(new Token(Kind.LSQUARE, startPos, 1, line, posInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case ']'-> {
							txt = "]";
							result.add(new Token(Kind.RSQUARE, startPos, 1, line, posInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case '+'-> {
							txt = "+";
							result.add(new Token(Kind.PLUS, startPos, 1, line, posInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case '-'-> {
							txt = "-";
							result.add(new Token(Kind.MINUS, startPos, 1, line, posInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case '/'-> {
							txt = "/";
							result.add(new Token(Kind.DIV, startPos, 1, line, posInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case '*'-> {
							txt = "*";
							result.add(new Token(Kind.TIMES, startPos, 1, line, posInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case '<'-> {
							txt = "<";
							result.add(new Token(Kind.LT, startPos, 1, line, posInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case '>'-> {
							txt = ">";
							result.add(new Token(Kind.GT, startPos, 1, line, posInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case ':'-> {
							txt = ":";
							result.add(new Token(Kind.COLON, startPos, 1, line, posInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case ';'-> {
							txt = ";";
							result.add(new Token(Kind.SEMI, startPos, 1, line, posInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case ','-> {
							txt = ",";
							result.add(new Token(Kind.COMMA, startPos, 1, line, posInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case '0'-> {
							txt = "0";
							result.add(new Token(Kind.INT_LITERAL, startPos, 1, line, posInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
						}
						case'=' -> {
							txt = "=";
							pos++;  
							posInLine++;  
							state = State.HAVE_EQUAL;
						}
						case'!' -> {
							if(chars[pos+1] == '=') {
								txt = "!";
								pos++;  
								posInLine++;  
								state = State.HAVE_EQUAL;
							}
							
							else {
								txt = "!";
								result.add(new Token(Kind.BANG, startPos, 1, line, posInLine, txt));
								result.current = result.current.next;
								pos++;
								posInLine++;
							}
						}
						case'1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
							txt = "" + ch;
							pos++;
							posInLine++;
							state = State.DIGITS;
						}
						default -> {
							if (Character.isJavaIdentifierStart(ch)) {
								txt = txt + ch;
								pos++;
								posInLine++;
								state = State.IDENT_PART;
								
							}
							else {
								if(ch != EOFchar) {
									txt = "" + ch;
									result.add(new Token(Kind.ERROR, startPos, 1, line, posInLine, txt));
									result.current = result.current.next;
									result.current.errorMessage = ch + " is an unrecognized character for this langauge";
								}
								pos++;
							}
						}
					}	
				}
				case HAVE_EQUAL-> {
					int equalsPos = pos;
					switch (ch) {
						case '=' -> {
							if(txt.equals("!")) {
								txt = "!=";
								result.add(new Token(Kind.NOT_EQUALS, equalsPos, 2, line, startPosInLine, txt));
								result.current = result.current.next;
								pos++;
								posInLine++;
								txt = "";
								state = State.START;
							}
							else if(txt.equals("=")) {
								txt = "==";
								result.add(new Token(Kind.EQUALS, equalsPos, 2, line, startPosInLine, txt));
								result.current = result.current.next;
								pos++;
								posInLine++;
								txt = "";
								state = State.START;
							}
						}
						default -> {
							txt = "=";
							result.add(new Token(Kind.ASSIGN, equalsPos, 1, line, startPosInLine, txt));
							result.current = result.current.next;
							pos++;
							posInLine++;
							txt = "";
							state = State.START;
							
						}
					}
				}

				case DIGITS-> {
					switch (ch) {
						case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
							txt = txt + ch;
							pos++;
							posInLine++;
						}
						default -> {
							try {
							    int i = Integer.parseInt(txt);
							    result.add(new Token(Kind.INT_LITERAL, startPos, pos - startPos, line, startPosInLine, txt));
								result.current = result.current.next;
								txt = "";
								state = State.START; 
							}
							catch (NumberFormatException e) {
								result.add(new Token(Kind.ERROR, startPos, pos - startPos, line, startPosInLine, txt));
								txt = "";
								result.current = result.current.next;
								result.current.errorMessage = ch + " is an unrecognized character for this langauge";
								state = State.START;
							}
							 		
						}
					}
				}
					
				case IDENT_PART-> {
					switch (ch) {
					case 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '$', '_' -> {
						txt = txt + ch;
						pos++;
						posInLine++;
					}
					default -> {
						result.add(new Token(Kind.IDENTIFIER, startPos, pos - startPos, line, startPosInLine, txt));
						result.current = result.current.next;
						txt = "";
						state = State.START; 
						//DO NOT INCREMENT pos  
						
					}
				}
				}
			}
		}
		
		//add EOF token
		result.add(new Token(Kind.EOF, pos, 0, line, posInLine, ""));
		result.current = result.head;
		
		
		return result;

	}





}
