class Operators {
    private static final String[] AND = {"AND", "&", "∧", "·"};
    private static final String[] OR = {"OR", "||", "∨", "+"};
    private static final String[] NOT = {"NOT", "¬", "~", "!"};
    private static final String[] XOR = {"XOR", "^", "⨁"};
    private static final String[] XNOR = {"XNOR", "⨀"};
    private static final String[] NAND = {"NAND", "!&", "~&", "¬&"};
    private static final String[] NOR = {"NOR", "!|", "~|", "¬|"};
    private static final String[] IMPLIES = {"IMPLIES", "⇒", "→", "⊃"};
    private static final String[] CONVERSE = {"CONVERSE", "⟸", "←"};
    private static final String[] IFF = {"IFF", "⇔", "↔", "≡"};

    public static boolean NOT ( boolean p ) {
        return !p;
    }

    public static boolean OR ( boolean... p ) {
        for ( boolean value : p )
            if (value)
                return true;
        return false;
    }

    public static boolean AND ( boolean... p ) {
        for ( boolean value : p )
            if (!value)
                return false;
        return true;
    }

    public static boolean NAND ( boolean... p ) {
        return !AND( p );
    }

    public static boolean NOR ( boolean... p ) {
        return !OR( p );
    }

    public static boolean XOR ( boolean... p ) {
        boolean _p = false;
        for ( boolean value : p )
            _p ^= value;
        return _p;
    }

    public static boolean XNOR ( boolean... p ) {
        return !XOR( p );
    }

    public static boolean IMPLIES ( boolean p, boolean q ) {
        return !p || q;
    }

    public static boolean CONVERSE ( boolean p, boolean q ) {
        return !q || p;
    }

    public static boolean IFF ( boolean p, boolean q ) {
        return p == q;
    }

    public static boolean isOperator ( String token ) {
        return isAND( token ) || isOR( token ) || isNOT( token ) ||
                isXOR( token ) || isXNOR( token ) || isNAND( token ) ||
                isNOR( token ) || isIMPLIES( token ) || isCONVERSE( token ) || isIFF( token );
    }

    public static boolean isAND ( String token ) {
        for ( String op : AND )
            if (op.equals( token ))
                return true;
        return false;
    }

    public static boolean isOR ( String token ) {
        for ( String op : OR )
            if (op.equals( token ))
                return true;
        return false;
    }

    public static boolean isNOT ( String token ) {
        for ( String op : NOT )
            if (op.equals( token ))
                return true;
        return false;
    }

    public static boolean isXOR ( String token ) {
        for ( String op : XOR )
            if (op.equals( token ))
                return true;
        return false;
    }

    public static boolean isXNOR ( String token ) {
        for ( String op : XNOR )
            if (op.equals( token ))
                return true;
        return false;
    }

    public static boolean isNAND ( String token ) {
        for ( String op : NAND )
            if (op.equals( token ))
                return true;
        return false;
    }

    public static boolean isNOR ( String token ) {
        for ( String op : NOR )
            if (op.equals( token ))
                return true;
        return false;
    }

    public static boolean isIMPLIES ( String token ) {
        for ( String op : IMPLIES )
            if (op.equals( token ))
                return true;
        return false;
    }

    public static boolean isIFF ( String token ) {
        for ( String op : IFF )
            if (op.equals( token ))
                return true;
        return false;
    }

    public static boolean isCONVERSE ( String token ) {
        for ( String op : CONVERSE )
            if (op.equals( token ))
                return true;
        return false;
    }

    public static int precedence ( String op ) {
        if (isNOT( op ))
            return 7;
        else if (isAND( op ))
            return 6;
        else if (isNAND( op ))
            return 5;
        else if (isOR( op ))
            return 4;
        else if (isNOR( op ))
            return 3;
        else if (isXOR( op ) || isXNOR( op ))
            return 2;
        else if (isIMPLIES( op ) || isCONVERSE( op ))
            return 1;
        else if (isIFF( op ))
            return 0;
        else
            return -99;
    }

    private static void printTable ( String[]... arrays ) {
        System.out.println();// Print the contents of the arrays
        for ( String[] array : arrays ) {
            for ( String s : array )
                System.out.printf( "%-15s", s );
            System.out.println();
        }
    }

    public static void print ( ) {
        printTable( AND, OR, NOT, XOR, XNOR, NAND, NOR, IMPLIES, CONVERSE, IFF );
    }
}   