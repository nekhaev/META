// Define a grammar called Hello
grammar bfs;
r  : 'bfs' '{' group+ '}' ;         // match keyword hello followed by an identifier
group: ID '{' ( group | property )* '}' ;
property: ID '=' STRING ;
ID : [a-zA-Z0-9_]+ ;             // match lower-case identifiers
STRING : '"' ('""'|~'"')* '"' ; // quote-quote is an escaped quote
COMMENT : '/*' .* '*/' -> skip ; //comment
WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines
