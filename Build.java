import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static de.vandermeer.asciithemes.TA_GridThemes.CC;

public class Build {
    private static final StringBuilder _vars = new StringBuilder( );
    public static void main ( String[] args ) {
        clear( );

        Scanner sc = new Scanner( System.in );
        StringBuilder exp = new StringBuilder( );
        Map<String, Boolean> vars = new HashMap<>( );

        System.out.println( "Build your boolean expression:" );
        System.out.println( "Type 'DONE' when you're finished." );
        while ( true ) {
            System.out.print( "Enter variable [A-Z]: " );
            String in = sc.next( ).toUpperCase( );

            if ( in.equals( "DONE" ) ) break;
            else if ( in.equals( "T" ) || in.equals( "1" ) || in.equals( "F" ) || in.equals( "0" ) ) {
                System.out.println( "You cannot use reserved variables!" );
            } else if ( Evaluation.isVariable( in ) && !vars.containsKey( in ) ) {
                _vars.append( in ).append( " = " );
                Boolean bval = value( in, sc );
                _vars.append( bval ).append( " (" ).append( bval ? 1 : 0 ).append( ")\n" );
                vars.put( in, bval );
            } else System.out.println( "Please enter a valid variable." );
        }

        while ( true ) {
            clear( );
            System.out.println( _vars );
            Operators.print( );
            System.out.println( "Type '#' to delete previous entry." );
            System.out.println( "Type 'DONE' when you're finished." );
            System.out.println( exp );
            String input = sc.next( ).toUpperCase( );

            if ( input.equals( "DONE" ) ) break;
            else if ( input.equals( "#" ) ) back( exp );
            else if ( input.equals( "T" ) || input.equals( "1" ) || input.equals( "F" ) || input.equals( "0" ) ) {
                exp.append( input ).append( ' ' );
                vars.put( (input.equals( "T" ) || input.equals( "1" ) ? "T" : "F"), input.equals( "T" ) || input.equals( "1" ) );
            } else if ( isValidInput( input ) ) {
                if ( Operators.isNOT( input ) && !input.equals( "NOT" ) )
                    exp.append( input );
                else exp.append( input ).append( ' ' );
                if ( Evaluation.isVariable( input ) && !vars.containsKey( input ) ) {
                    System.out.println( "Whoa! A new variable?" );
                    _vars.append( input ).append( " = " );
                    Boolean bval = value( input, sc );
                    _vars.append( bval ).append( " (" ).append( bval ? 1 : 0 ).append( ")\n" );
                    vars.put( input, bval );
                }
            } else
                System.out.println( "Invalid input. Please enter a valid variable, operator, or parenthesis." );
        }

        clear( );
        System.out.println( _vars );
        System.out.println( "Expression: " + exp.toString( ).trim( ) );
        AsciiTable opt = new AsciiTable( );
        opt.addRule( );
        opt.addRow( (Object[])new String[]{"Evaluate Expression"} );
        opt.addRule( );
        opt.addRow( (Object[])new String[]{"Generate Truth Table"} );
        opt.addRule( );
        opt.getContext( ).setWidth( 20 );
        opt.setTextAlignment( TextAlignment.CENTER );
        opt.getContext( ).setGridTheme( CC );
        System.out.println( opt.render( ) );
        System.out.print( "\nChoose an Option: " );
        char c;
        do {
            c = sc.next( ).toLowerCase( ).charAt( 0 );
            if ( c == 'a' || c == '1' || c == 'b' || c == '2' ) break;
            System.out.println( "Invalid choice. Try again!" );
        }
        while ( false );

        switch (c) {
            case 'a', '1' -> {
                try {
                    boolean res = Evaluation.eval( exp.toString( )
                            .replaceAll( "[~!¬]", "NOT " )
                            .replace( "1", "T" )
                            .replace( "0", "F" ), vars );
                    System.out.println( exp + " ≡ " + res );
                } catch ( Exception e ) {
                    System.out.println( "Error evaluating the expression...\n " + e.getMessage( ) );
                }
            }
            case 'b', '2' -> {
                try {
                    tt( exp.toString( ), vars );
                } catch ( Exception e ) {
                    System.out.println( "Critical error encountered...\n" + e.getMessage( ) );
                }
            }
            default -> throw new IllegalStateException( "Unexpected value: " + c );
        }
    }

    private static boolean isValidInput ( String input ) {
        return Evaluation.isVariable( input ) || Operators.isOperator( input.toUpperCase( ) ) || input.equals( "(" ) || input.equals( ")" );
    }

    private static void clear ( ) {
        try {
            String os = System.getProperty( "os.name" ); //Check the current operating system

            if ( os.contains( "Windows" ) ) {
                ProcessBuilder pb = new ProcessBuilder( "cmd", "/c", "cls" );
                Process startProcess = pb.inheritIO( ).start( );
                startProcess.waitFor( );
            } else {
                ProcessBuilder pb = new ProcessBuilder( "clear" );
                Process sp = pb.inheritIO( ).start( );

                sp.waitFor( );
            }
        } catch ( Exception e ) {
            e.printStackTrace( );
        }
    }

    private static void back ( StringBuilder sb ) {
        if ( sb.length( ) == 0 )
            return;

        if ( sb.charAt( sb.length( ) - 1 ) == ' ' ) {
            sb.deleteCharAt( sb.length( ) - 1 );
            int lastSpaceIndex = sb.lastIndexOf( " " );
            if ( lastSpaceIndex != -1 )
                sb.delete( lastSpaceIndex + 1, sb.length( ) );
            else
                sb.setLength( 0 );
        } else
            sb.deleteCharAt( sb.length( ) - 1 );
    }

    private static boolean value ( String in, Scanner sc ) {
        String val;
        do {
            System.out.print( in + " = " );
            val = sc.next( );
            if ( val.equalsIgnoreCase( "true" ) || val.equalsIgnoreCase( "t" ) || val.equalsIgnoreCase( "1" ) )
                return true;
            else if ( val.equalsIgnoreCase( "false" ) || val.equalsIgnoreCase( "f" ) || val.equalsIgnoreCase( "0" ) )
                return false;
            System.out.println( "Invalid input! Variables can only hold boolean/binary values." );
        }
        while ( !(val.equalsIgnoreCase( "true" ) || val.equalsIgnoreCase( "t" ) || val.equalsIgnoreCase( "1" ) ||
                val.equalsIgnoreCase( "false" ) || val.equalsIgnoreCase( "f" ) || val.equalsIgnoreCase( "0" )) );
        return false;
    }

    private static void tt ( String exp, Map<String, Boolean> vars ) throws Exception {
        List<String> varset = new ArrayList<>( vars.keySet( ) );
        int n = varset.size( );
        int nc = 1 << n;

        AsciiTable at = new AsciiTable( );
        at.addRule( );
        List<String> headerRow = new ArrayList<>( varset );
        headerRow.add( "Result" );
        at.addRow( headerRow.toArray( ) );
        at.addRule( );

        for (int i = 0; i < nc; i++) {
            List<String> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                String variable = varset.get(j);
                boolean value;
                if (variable.equals("T"))
                    value = true;
                else if (variable.equals("F"))
                    value = false;
                else
                    value = (i & (1 << (n - 1 - j))) != 0;
                vars.put(variable, value);
                row.add(value ? "1" : "0");
            }
            boolean result = Evaluation.eval(exp, vars);
            row.add(result ? "1" : "0");
            at.addRow(row.toArray());
            at.addRule();
        }

        at.getContext( ).setWidth( n * 10 );
        at.setTextAlignment( TextAlignment.CENTER );
        String rend = at.render( );
        if ( n > 2 ) {
            int id = 100000 + new Random( ).nextInt( 900000 );
            try (FileWriter fw = new FileWriter( n + "-adic_truthtable_" + id + ".txt" )) {
                fw.write( rend );
            }
            catch ( IOException ioe ){ System.out.println("Error writing to file."); }
        }
        System.out.println( rend );
    }
}