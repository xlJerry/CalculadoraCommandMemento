package model;

public class Main {

    public static void main(String[] args) {
        Calculadora calculadora = new Calculadora();
        Caretaker caretaker = new Caretaker();

        mostrar("Estado inicial", calculadora);

        caretaker.ejecutar(new ComandoDigitar(calculadora, "1"));
        caretaker.ejecutar(new ComandoDigitar(calculadora, "2"));
        mostrar("Después de digitar 12", calculadora);

        caretaker.ejecutar(new ComandoSumar(calculadora));
        caretaker.ejecutar(new ComandoDigitar(calculadora, "3"));
        caretaker.ejecutar(new ComandoEquivalencia(calculadora));
        mostrar("Después de 12 + 3 =", calculadora);

        caretaker.deshacer();
        mostrar("Después de deshacer", calculadora);

        caretaker.rehacer();
        mostrar("Después de rehacer", calculadora);

        caretaker.ejecutar(new ComandoMultiplicar(calculadora));
        caretaker.ejecutar(new ComandoDigitar(calculadora, "2"));
        caretaker.ejecutar(new ComandoEquivalencia(calculadora));
        mostrar("Después de * 2 =", calculadora);

        caretaker.ejecutar(new ComandoBorrar(calculadora));
        mostrar("Después de borrar", calculadora);

        caretaker.deshacer();
        mostrar("Después de deshacer borrar", calculadora);
    }

    private static void mostrar(String titulo, Calculadora calculadora) {
        System.out.println("---- " + titulo + " ----");
        System.out.println("valorActual     = " + calculadora.getValorActual());
        System.out.println("expresion       = " + calculadora.getExpresion());
        System.out.println("memoriaOp       = " + calculadora.getMemoriaOp());
        System.out.println("esperarOperando = " + calculadora.isEsperarOperando());
        System.out.println("operacionPend   = " + calculadora.getOperacionPend());
        System.out.println();
    }
}