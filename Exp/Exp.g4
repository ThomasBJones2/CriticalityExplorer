grammar Exp;

/* This will be the entry point of our parser. */
eval returns [double value]
    :    exp = additionExp {$value = $exp.value;} 
    ;

/* Addition and subtraction have the lowest precedence. */
additionExp returns [double value]
    :    m1 = multiplyExp {$value = $m1.value;} 
         ( '+' m2 = multiplyExp {$value += $m2.value;}
         | '-' m2 = multiplyExp {$value -= $m2.value;}
         )* 
    ;

/* Multiplication and division have a higher precedence. */
multiplyExp returns [double value]
    :    a1 = atomExp {$value = $a1.value;} 
         ( '*' a2 = atomExp {$value *= $a2.value;}
         | '/' a2 = atomExp {$value /= $a2.value;}
         )* 
    ;

/* An expression atom is the smallest part of an expression: a number. Or 
   when we encounter parenthesis, we're making a recursive call back to the
   rule 'additionExp'. As you can see, an 'atomExp' has the highest precedence. */
atomExp returns [double value]
    :    n = Number {$value = Double.parseDouble($n.text);}
    |    '(' exp = additionExp ')' {$value = $exp.value;}
    ;

/* A number: can be an integer value, or a decimal value */
Number
    :    ('0'..'9')+ ('.' ('0'..'9')+)?
    ;

/* We're going to ignore all white space characters */
WS  
    :   (' ' | '\t' | '\r'| '\n') {skip();}
    ;
