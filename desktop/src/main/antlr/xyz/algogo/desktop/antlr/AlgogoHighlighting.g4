lexer grammar AlgogoHighlighting;

options {
    language = Java;
}

channels {EXTRA}

@header {
package xyz.algogo.desktop.antlr;
}

NEWLINE: ('\r' '\n' | '\n' | '\r');
WS: [ \t]+ -> channel(EXTRA);

OR: '||';
AND: '&&';
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
NOT: '!';
COMMA: ',';

ASSIGN: '<-';
OPAR: '(';
CPAR: ')';
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
BlockComment: BLOCKCOMMENT_START .* BLOCKCOMMENT_END;

UNMATCHED: .  -> channel(EXTRA);