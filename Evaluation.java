import java.util.Map;
import java.util.Stack;

class Evaluation {
    public static boolean eval ( String exp, Map<String, Boolean> vars ) throws Exception {
        return evalRPN( toRPN( exp ), vars );
    }

    private static String toRPN ( String exp ) throws Exception {
        StringBuilder sb = new StringBuilder();
        Stack<String> ops = new Stack<>();
        String[] tokens = exp.split( " " );

        for ( String token : tokens )
            if ( isVariable( token ) )
                sb.append( token ).append( ' ' );
            else if ( token.equals( "(" ) )
                ops.push( token );
            else if ( token.equals( ")" ) ) {
                while ( !ops.isEmpty() && !ops.peek().equals( "(" ) )
                    sb.append( ops.pop() ).append( ' ' );
                if ( ops.isEmpty() ) throw new Exception( "Mismatched parentheses" );
                ops.pop();
            } else if ( Operators.isOperator( token ) ) {
                while ( !ops.isEmpty() && Operators.precedence( ops.peek() ) >= Operators.precedence( token ) )
                    sb.append( ops.pop() ).append( ' ' );
                ops.push( token );
            } else throw new Exception( "Invalid token: " + token );
        while ( !ops.isEmpty() ) sb.append( ops.pop() ).append( ' ' );

        return sb.toString();
    }

    private static boolean evalRPN ( String rpn, Map<String, Boolean> vars ) throws Exception {
        Stack<Boolean> stack = new Stack<>();
        String[] tokens = rpn.split( " " );
        for ( String token : tokens ) {
            if ( token.isEmpty() )
                continue;
            if ( Operators.isOperator( token ) )
                if ( Operators.isNOT( token ) ) {
                    boolean a = stack.pop();
                    stack.push( Operators.NOT( a ) );
                } else {
                    boolean b = stack.pop();
                    boolean a = stack.pop();
                    stack.push( applyOperator( token, a, b ) );
                }
            else
                stack.push( vars.get( token ) );
        }
        if ( stack.size() != 1 ) throw new Exception( "Invalid RPN expression" );
        return stack.pop();
    }

    public static boolean isVariable ( String token ) {
        return token.matches( "[A-Z]" );
    }

    private static boolean applyOperator ( String operator, boolean a, boolean b ) throws Exception {
        if ( Operators.isAND( operator ) )
            return Operators.AND( a, b );
        else if ( Operators.isOR( operator ) )
            return Operators.OR( a, b );
        else if ( Operators.isXOR( operator ) )
            return Operators.XOR( a, b );
        else if ( Operators.isXNOR( operator ) )
            return Operators.XNOR( a, b );
        else if ( Operators.isNAND( operator ) )
            return Operators.NAND( a, b );
        else if ( Operators.isNOR( operator ) )
            return Operators.NOR( a, b );
        else if ( Operators.isIMPLIES( operator ) )
            return Operators.IMPLIES( a, b );
        else if ( Operators.isCONVERSE( operator ) )
            return Operators.CONVERSE( a, b );
        else if ( Operators.isIFF( operator ) )
            return Operators.IFF( a, b );
        else
            throw new Exception( "Invalid operator: " + operator );
    }
}