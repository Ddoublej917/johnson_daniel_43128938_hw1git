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
		Token next;
		
		public Token (Kind kind, int pos, int length, int line, int posInLine, String text){
			flag = 0;
			token_kind = kind;
			token_pos = pos;
			token_length = length;
			token_line = line;
			token_posInLine = posInLine;
			token_text = text;
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
		String errorMessage;
		int errorLine;
		int errorPosInLine;
		public Lexer (){
			head = null;
			current = null;
			errorMessage = "";
			errorLine = 0;
			errorPosInLine = 0;
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
			if (!errorMessage.equals("")) {
				throw new LexicalException(errorMessage, errorLine,errorPosInLine);
			}
			else {
				current = current.next;
			}
			errorMessage = "";
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
		int posInLine = 1; // position in line of source. Starts at 1
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
					switch (ch) {
						case ' ', '\t'-> {
							pos++;
							posInLine++;
						}
						case '\n'-> {
							pos++;
							line++;
							posInLine = 1;
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
							txt = "";
							pos++;
							posInLine++;
							state = State.DIGITS;
						}
						default -> {
							if (Character.isJavaIdentifierStart(ch)) {
								pos++;
								posInLine++;
								state = State.IDENT_PART;
								
							}
							else {
								if(ch != EOFchar) {
									result.errorMessage  = ch + " is an unrecognized character for this langauge";
									try {
										result.nextToken();
									} catch (LexicalException e) {
										state = State.START; 
										e.printStackTrace();
									}
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
							if(txt == "!") {
								txt = "!=";
								result.add(new Token(Kind.NOT_EQUALS, equalsPos, 2, line, posInLine, txt));
								result.current = result.current.next;
								pos++;
								posInLine++;
								state = State.START;
							}
							else if(txt == "=") {
								txt = "==";
								result.add(new Token(Kind.EQUALS, equalsPos, 2, line, posInLine, txt));
								result.current = result.current.next;
								pos++;
								posInLine++;
								state = State.START;
							}
						}
						default -> {
							result.errorMessage  = "Lexical error involving an equals sign";
							try {
								result.nextToken();
							} catch (LexicalException e) {
								state = State.START; 
								e.printStackTrace();
							}
							
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
							result.add(new Token(Kind.INT_LITERAL, startPos, pos - startPos, line, startPosInLine, txt));
							result.current = result.current.next;
							state = State.START; 
							//DO NOT INCREMENT pos  
							
						}
					}
				}
					
				case IDENT_PART-> {
					
				}
			}
		}
		
		//add EOF token
		result.add(new Token(Kind.EOF, pos, 0, line, posInLine, ""));
		result.current = result.head;
		
		
		return result;

	}





}
