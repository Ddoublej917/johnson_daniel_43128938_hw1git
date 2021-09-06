package edu.ufl.cise.plpfa21.assignment1;

import java.util.ArrayList;
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
		public IPLPToken nextToken() {
			current = current.next;
			return current;
		}
	}
		

	static IPLPLexer getLexer(String input) {
		Lexer result;
		char[] chars; //holds characters with 0 at end
		char EOFchar = 0;
		int startPos = 0;
		int startPosInLine = 0;
		int pos = 0;
		int line = 0;
		int posInLine = 0;
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
							result.nextToken();
							pos++;
							posInLine++;
						}
						case ')'-> {
							txt = ")";
							result.add(new Token(Kind.RPAREN, startPos, 1, line, posInLine, txt));
							result.nextToken();
							pos++;
							posInLine++;
						}
						case '['-> {
							txt = "[";
							result.add(new Token(Kind.LSQUARE, startPos, 1, line, posInLine, txt));
							result.nextToken();
							pos++;
							posInLine++;
						}
						case ']'-> {
							txt = "]";
							result.add(new Token(Kind.RSQUARE, startPos, 1, line, posInLine, txt));
							result.nextToken();
							pos++;
							posInLine++;
						}
						case '+'-> {
							txt = "+";
							result.add(new Token(Kind.PLUS, startPos, 1, line, posInLine, txt));
							result.nextToken();
							pos++;
							posInLine++;
						}
						case '-'-> {
							txt = "-";
							result.add(new Token(Kind.MINUS, startPos, 1, line, posInLine, txt));
							result.nextToken();
							pos++;
							posInLine++;
						}
						case '/'-> {
							txt = "/";
							result.add(new Token(Kind.DIV, startPos, 1, line, posInLine, txt));
							result.nextToken();
							pos++;
							posInLine++;
						}
						case '*'-> {
							txt = "*";
							result.add(new Token(Kind.TIMES, startPos, 1, line, posInLine, txt));
							result.nextToken();
							pos++;
							posInLine++;
						}
						case '<'-> {
							txt = "<";
							result.add(new Token(Kind.LT, startPos, 1, line, posInLine, txt));
							result.nextToken();
							pos++;
							posInLine++;
						}
						case '>'-> {
							txt = ">";
							result.add(new Token(Kind.GT, startPos, 1, line, posInLine, txt));
							result.nextToken();
							pos++;
							posInLine++;
						}
						case ':'-> {
							txt = ":";
							result.add(new Token(Kind.COLON, startPos, 1, line, posInLine, txt));
							result.nextToken();
							pos++;
							posInLine++;
						}
						case ';'-> {
							txt = ";";
							result.add(new Token(Kind.SEMI, startPos, 1, line, posInLine, txt));
							result.nextToken();
							pos++;
							posInLine++;
						}
						case ','-> {
							txt = ",";
							result.add(new Token(Kind.COMMA, startPos, 1, line, posInLine, txt));
							result.nextToken();
							pos++;
							posInLine++;
						}
						case '0'-> {
							txt = "0";
							result.add(new Token(Kind.INT_LITERAL, startPos, 1, line, posInLine, txt));
							result.nextToken();
							pos++;
							posInLine++;
						}
						case'=' -> {
							pos++;  
							posInLine++;  
							state = State.HAVE_EQUAL;
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
									//Handle error
								}
								pos++;
							}
						}
					}	
				}
				case HAVE_EQUAL-> {
					
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
							result.nextToken();
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
