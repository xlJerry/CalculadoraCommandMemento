package model;

public class Calculadora {

    private static final int MAX_DIGITOS_NUMERO = 15;
    private static final int MAX_CARACTERES_VISIBLES = 25;

    private Float valorActual;
    public String expresion;
    public String memoriaOp;
    public boolean esperarOperando;
    public String operacionPend;

    public Calculadora() {
        borrar();
    }

    public MementoCalculadora Guardar() {
        return new MementoCalculadora(
                this.memoriaOp,
                this.valorActual,
                this.esperarOperando,
                this.expresion,
                this.operacionPend
        );
    }

    public void setEstado(Float valorActual, String expresion, String memoriaOp, boolean esperarOperando,
            String operacionPend) {
        this.valorActual = (valorActual == null) ? 0f : valorActual;
        this.expresion = (expresion == null || expresion.isBlank()) ? "0" : expresion;
        this.memoriaOp = (memoriaOp == null) ? "" : memoriaOp;
        this.esperarOperando = esperarOperando;
        this.operacionPend = (operacionPend == null) ? "" : operacionPend;
    }

    public void ingresarDigito(String digito) {
        if (digito == null || digito.isBlank()) {
            return;
        }

        if (esEstadoError()) {
            this.expresion = digito;
            this.valorActual = parseFloatSeguro(this.expresion);
            this.memoriaOp = "";
            this.operacionPend = "";
            this.esperarOperando = false;
            return;
        }

        if (this.esperarOperando || "0".equals(this.expresion)) {
            this.expresion = digito;
            this.esperarOperando = false;
        } else {
            if (cantidadDigitos(this.expresion) >= MAX_DIGITOS_NUMERO) {
                return;
            }
            this.expresion += digito;
        }

        this.valorActual = parseFloatSeguro(this.expresion);
    }

    public void ingresarPunto() {
        if (esEstadoError()) {
            this.expresion = "0.";
            this.valorActual = 0f;
            this.memoriaOp = "";
            this.operacionPend = "";
            this.esperarOperando = false;
            return;
        }

        if (this.esperarOperando) {
            this.expresion = "0.";
            this.valorActual = 0f;
            this.esperarOperando = false;
            return;
        }

        if (!this.expresion.contains(".")) {
            this.expresion += ".";
        }
    }

    public void seleccionarOperacion(String operacion) {
        if (operacion == null || operacion.isBlank()) {
            return;
        }

        if (esEstadoError()) {
            return;
        }

        normalizarExpresionSiTerminaEnPunto();

        if (this.expresion == null || this.expresion.isBlank()) {
            this.expresion = "0";
        }

        if (!this.memoriaOp.isBlank() && !this.operacionPend.isBlank() && !this.esperarOperando) {
            Float resultado = aplicarOperacion(
                    parseFloatSeguro(this.memoriaOp),
                    parseFloatSeguro(this.expresion),
                    this.operacionPend
            );

            this.valorActual = resultado;
            this.expresion = formatear(resultado);

            if (esEstadoError()) {
                this.memoriaOp = "";
                this.operacionPend = "";
                this.esperarOperando = true;
                return;
            }

            this.memoriaOp = this.expresion;
        } else if (this.memoriaOp.isBlank()) {
            this.memoriaOp = this.expresion;
        }

        this.operacionPend = operacion;
        this.esperarOperando = true;
    }

    public void equivalencia() {
        if (esEstadoError()) {
            return;
        }

        normalizarExpresionSiTerminaEnPunto();

        if (this.operacionPend.isBlank() || this.memoriaOp.isBlank() || this.esperarOperando) {
            return;
        }

        Float resultado = aplicarOperacion(
                parseFloatSeguro(this.memoriaOp),
                parseFloatSeguro(this.expresion),
                this.operacionPend
        );

        this.valorActual = resultado;
        this.expresion = formatear(resultado);

        this.memoriaOp = "";
        this.operacionPend = "";
        this.esperarOperando = true;
    }

    public void borrar() {
        this.valorActual = 0f;
        this.expresion = "0";
        this.memoriaOp = "";
        this.esperarOperando = true;
        this.operacionPend = "";
    }

    public void borrarEntrada() {
        if (this.memoriaOp.isBlank() && this.operacionPend.isBlank()) {
            borrar();
            return;
        }

        this.expresion = "0";
        this.valorActual = 0f;
        this.esperarOperando = true;
    }

    public void borrarUltimoDigito() {
        if (esEstadoError()) {
            borrar();
            return;
        }

        if (this.expresion == null || this.expresion.isBlank()) {
            this.expresion = "0";
            this.valorActual = 0f;
            return;
        }

        if (this.expresion.length() <= 1) {
            this.expresion = "0";
            this.valorActual = 0f;
            return;
        }

        String nuevaExpr = this.expresion.substring(0, this.expresion.length() - 1);

        if (nuevaExpr.endsWith(".")) {
            nuevaExpr = nuevaExpr.substring(0, nuevaExpr.length() - 1);
        }

        if (nuevaExpr.isBlank() || "-".equals(nuevaExpr)) {
            nuevaExpr = "0";
        }

        this.expresion = nuevaExpr;
        this.valorActual = parseFloatSeguro(this.expresion);
        this.esperarOperando = false;
    }

    private void normalizarExpresionSiTerminaEnPunto() {
        if (this.expresion != null && this.expresion.endsWith(".")) {
            this.expresion = this.expresion.substring(0, this.expresion.length() - 1);

            if (this.expresion.isBlank()) {
                this.expresion = "0";
            }

            this.valorActual = parseFloatSeguro(this.expresion);
        }
    }

    private boolean esEstadoError() {
        return "Error".equals(this.expresion) || "NaN".equals(this.expresion);
    }

    private int cantidadDigitos(String texto) {
        if (texto == null) {
            return 0;
        }

        int contador = 0;
        for (char c : texto.toCharArray()) {
            if (Character.isDigit(c)) {
                contador++;
            }
        }
        return contador;
    }

    public String construirOperacionActual() {
        String memoria = (this.memoriaOp == null) ? "" : this.memoriaOp;
        String operacion = (this.operacionPend == null) ? "" : this.operacionPend;
        String expr = (this.expresion == null || this.expresion.isBlank()) ? "0" : this.expresion;

        String texto;
        if (!memoria.isBlank() && !operacion.isBlank()) {
            if (this.esperarOperando) {
                texto = memoria + " " + operacion;
            } else {
                texto = memoria + " " + operacion + " " + expr;
            }
        } else {
            texto = expr;
        }

        return limitarTexto(texto);
    }

    public String limitarTexto(String texto) {
        if (texto == null) {
            return "";
        }

        if (texto.length() <= MAX_CARACTERES_VISIBLES) {
            return texto;
        }

        return "…" + texto.substring(texto.length() - (MAX_CARACTERES_VISIBLES - 1));
    }

    private Float aplicarOperacion(Float a, Float b, String operacion) {
        switch (operacion) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                if (b == 0f) {
                    return Float.NaN;
                }
                return a / b;
            default:
                return b;
        }
    }

    private Float parseFloatSeguro(String texto) {
        try {
            return Float.parseFloat(texto);
        } catch (Exception e) {
            return 0f;
        }
    }

    private String formatear(Float numero) {
        if (numero == null || numero.isNaN() || numero.isInfinite()) {
            return "Error";
        }

        if (numero == numero.longValue()) {
            return String.valueOf(numero.longValue());
        }

        return String.valueOf(numero);
    }

    public Float getValorActual() {
        return valorActual;
    }

    public String getExpresion() {
        return expresion;
    }

    public String getMemoriaOp() {
        return memoriaOp;
    }

    public boolean isEsperarOperando() {
        return esperarOperando;
    }

    public String getOperacionPend() {
        return operacionPend;
    }
}