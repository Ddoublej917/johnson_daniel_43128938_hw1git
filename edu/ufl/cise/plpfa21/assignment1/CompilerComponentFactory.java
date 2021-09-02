package edu.ufl.cise.plpfa21.assignment1;

public class CompilerComponentFactory {
	
	public class Token implements IPLPToken(
			Kind kind,
			int pos,
			int length,
			int line,
			int posInLine
			) {
		//required methods here
	}
	
	private final ArrayList<IPLPToken> tokens;
	private final char[] chars; //holds characters with 0 at the end.
	private int nextTokenPos = 0;
	static final char EOFchar = 0;

	static IPLPLexer getLexer(String input) {
		//TODO  create and return a Lexer instance to parse the given input.
		int numChars = inputString.length();
		this.chars = Arrays.copyOf(input.toCharArray(), numChars + 1); // input char array terminated with EOFchar for convenience
		chars[numChars] = EOFchar;
		tokens = new ArrayList<>();
		
		private enum State {START, HAVE_EQUAL, DIGITS, IDENT_PART}
		
		State state = START;
		
		while(pos < chars.length) {
			char ch = chars[pos]; //get current char
			switch (state) {
				case START-> {
					startPos = pos;
					switch (ch) {
						case '', '\t'-> {
							pos++;
							posInLine++;
						}
						case '\n'-> {
							pos++;
							line++;
							posInLine = 1;
						}
						case '0'-> {
							tokens.add(new Token(Kind.NUMLIT, startPos, 1, line, startPosInLine));
							pos++;
							posInLine++;
						}
						case'=' -> {
							pos++;  
							posInLine++;  
							state = State.HAVE_EQUAL;
						}
						case'1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
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
							pos++;
							posInLine++;
						}
						default -> {
							tokens.add(new Token(Kind.NUMLIT, startPos, pos - startPos, line, startPosInLine));
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
		tokens.add(new Token(Kind.EOF, pos, 0, line, posInLine));
		
		
		
		return null;
	}
	

}

