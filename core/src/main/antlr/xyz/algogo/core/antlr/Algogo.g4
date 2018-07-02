grammar Algogo;

options {
    language = Java;
}

tokens {INDENT, DEDENT}

@header {
package xyz.algogo.core.antlr;
}

@lexer::members {
    private boolean pendingDent = true;   // Setting this to true means we start out in `pendingDent` state and any whitespace at the beginning of the file will trigger an INDENT, which will probably be a syntax error, as it is in Python.
    private int indentCount = 0;
    private java.util.LinkedList<Token> tokenQueue = new java.util.LinkedList<>();
    private java.util.Stack<Integer> indentStack = new java.util.Stack<>();
    private Token initialIndentToken = null;
    private int getSavedIndent() { return indentStack.isEmpty() ? 0 : indentStack.peek(); }

    private CommonToken createToken(int type, String text, Token next) {
        CommonToken token = new CommonToken(type, text);
        if(null != initialIndentToken) {
            token.setStartIndex(initialIndentToken.getStartIndex());
            token.setLine(initialIndentToken.getLine());
            token.setCharPositionInLine(initialIndentToken.getCharPositionInLine());
            token.setStopIndex(next.getStartIndex()-1);
        }
        return token;
    }

    @Override
    public Token nextToken() {
        // Return tokens from the queue if it is not empty.
        if(!tokenQueue.isEmpty()) { return tokenQueue.poll(); }

        // Grab the next token and if nothing special is needed, simply return it.
        Token next = super.nextToken();
        //NOTE: This would be the appropriate spot to count whitespace or deal with NEWLINES, but it is already handled with custom actions down in the lexer rules.
        if(pendingDent && null == initialIndentToken && NEWLINE != next.getType()) { initialIndentToken = next; }
        if(null == next || HIDDEN == next.getChannel() || NEWLINE == next.getType()) { return next; }

        // Handle EOF; in particular, handle an abrupt EOF that comes without an immediately preceding NEWLINE.
        if(next.getType() == EOF) {
            indentCount = 0;
            // EOF outside of `pendingDent` state means we did not have a final NEWLINE before the end of file.
            if(!pendingDent) {
                initialIndentToken = next;
                tokenQueue.offer(createToken(NEWLINE, "NEWLINE", next));
            }
        }

        // Before exiting `pendingDent` state we need to queue up proper INDENTS and DEDENTS.
        while(indentCount != getSavedIndent()) {
            if(indentCount > getSavedIndent()) {
                indentStack.push(indentCount);
                tokenQueue.offer(createToken(AlgogoParser.INDENT, "INDENT" + indentCount, next));
            }
            else {
                indentStack.pop();
                tokenQueue.offer(createToken(AlgogoParser.DEDENT, "DEDENT" + getSavedIndent(), next));
            }
        }
        pendingDent = false;
        tokenQueue.offer(next);
        return tokenQueue.poll();
    }

}

script: header=comment? (rootStatement | NEWLINE)* EOF;

rootStatement
    : variablesBlockRootStatement
    | beginningBlockRootStatement
    | endBlockRootStatement
    | comment
    ;

variablesBlockRootStatement: VARIABLES NEWLINE+ (INDENT (createVariableStatement | NEWLINE)* DEDENT)?;
beginningBlockRootStatement: BEGINNING NEWLINE+ (INDENT (statement | NEWLINE)* DEDENT)?;
endBlockRootStatement: END;

createVariableStatement
    : ID DECLARE type=(TYPE_NUMBER | TYPE_STRING) NEWLINE+
    | comment
    ;

statement
    : promptStatement
    | printStatement
    | printVariableStatement
    | ifBlockStatement
    | whileBlockStatement
    | forBlockStatement
    | assignStatement
    | comment
    ;

promptStatement: PROMPT ID STRING? NEWLINE+;
printStatement: PRINT STRING NO_LINE_BREAK? NEWLINE+;
printVariableStatement: PRINT_VARIABLE ID STRING? NO_LINE_BREAK? NEWLINE+;
ifBlockStatement: IF expression THEN NEWLINE+ (INDENT (statement | NEWLINE)* DEDENT)? elseBlock?;
whileBlockStatement: WHILE expression DO NEWLINE+ (INDENT (statement | NEWLINE)* DEDENT)?;
forBlockStatement: FOR ID FROM start=expression TO end=expression DO NEWLINE+ (INDENT (statement | NEWLINE)* DEDENT)?;
assignStatement: ID ASSIGN expression NEWLINE+;

elseBlock: ELSE NEWLINE+ (INDENT (statement | NEWLINE)* DEDENT)?;

comment: (LineComment | BlockComment) NEWLINE+;

expression
    : <assoc=right> expression POW expression               #powExpression
    | MINUS expression                                      #unaryMinusExpression
    | NOT expression                                        #notExpression
    | expression op=(MULT | DIV | MOD) expression           #multiplicationExpression
    | expression op=(PLUS | MINUS) expression               #additiveExpression
    | expression op=(LTEQ | GTEQ | LT | GT) expression      #relationalExpression
    | expression op=(EQ | NEQ) expression                   #equalityExpression
    | expression AND expression                             #andExpression
    | expression OR expression                              #orExpression
    | ID OPEN_PARENTHESIS functionParams CLOSED_PARENTHESIS #functionExpression
    | OPEN_PARENTHESIS expression CLOSED_PARENTHESIS        #parenthesisExpression
    | VERTICAL_BAR expression VERTICAL_BAR                  #absoluteValueExpression
    | atom                                                  #atomExpression
    ;

functionParams: (expression (COMMA expression)*)?;

atom
    : numberAtom
    | booleanAtom
    | identifierAtom
    | stringAtom
    ;

numberAtom: number=(INT | FLOAT);
booleanAtom: bool=(TRUE | FALSE);
identifierAtom: ID;
stringAtom: STRING;

NEWLINE: ('\r' '\n' | '\n' | '\r') { if (pendingDent) { setChannel(HIDDEN); } pendingDent = true; indentCount = 0; initialIndentToken = null; };
WS: [ \t]+ { setChannel(HIDDEN); if (pendingDent) { indentCount += getText().length(); } };    

OR: '||' | 'OR';
AND: '&&' | 'AND';
EQ: '==';
NEQ: '!=';
GT: '>';
LT: '<';
GTEQ: '>=';
LTEQ: '<=';
PLUS: '+';
MINUS: '-';
MULT: '*';
DIV: '/';
MOD: '%';
POW: '^';
NOT: '!' | 'NOT';
COMMA: ',';

ASSIGN: '<-';
OPEN_PARENTHESIS: '(';
CLOSED_PARENTHESIS: ')';
VERTICAL_BAR: '|';

TRUE: 'true';
FALSE: 'false';

VARIABLES: 'VARIABLES';
BEGINNING: 'BEGINNING';
END: 'END';

DECLARE: ':';
TYPE_STRING: 'STRING';
TYPE_NUMBER: 'NUMBER';

IF: 'IF';
THEN: 'THEN';
ELSE: 'ELSE';

WHILE: 'WHILE';
DO: 'DO';

FOR: 'FOR';
FROM: 'FROM';
TO: 'TO';

PRINT: 'PRINT';
PRINT_VARIABLE: 'PRINT_VARIABLE';
PROMPT: 'PROMPT';
NO_LINE_BREAK: 'NLB';

LINECOMMENT_START: '//';
BLOCKCOMMENT_START: '/*';
BLOCKCOMMENT_END: '*/';

ID: [a-zA-Z_][a-zA-Z_0-9]*;
INT: [0-9]+;
FLOAT
    : [0-9]+ '.' [0-9]* 
    | '.' [0-9]+
    ;
STRING: '"' (~["\r\n] | '""')* '"';

LineComment: LINECOMMENT_START ~[\r\n]*;
BlockComment: BLOCKCOMMENT_START .*? BLOCKCOMMENT_END;