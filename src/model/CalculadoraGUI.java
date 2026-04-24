package model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

public class CalculadoraGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private final Calculadora calculadora;
    private final Caretaker caretaker;

    private final JLabel lineaSuperior;
    private final JLabel lineaPrincipal;

    private String operacionResuelta;
    private boolean resultadoMostrado;

    public CalculadoraGUI() {
        this.calculadora = new Calculadora();
        this.caretaker = new Caretaker();
        this.operacionResuelta = "";
        this.resultadoMostrado = false;

        setTitle("Calculadora Command + Memento");
        setSize(420, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(12, 12, 12));

        JPanel panelPantalla = new JPanel(new GridLayout(2, 1));
        panelPantalla.setBackground(new Color(12, 12, 12));
        panelPantalla.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        lineaSuperior = new JLabel("");
        lineaSuperior.setHorizontalAlignment(SwingConstants.RIGHT);
        lineaSuperior.setForeground(new Color(180, 180, 180));
        lineaSuperior.setFont(new Font("Arial", Font.PLAIN, 22));

        lineaPrincipal = new JLabel("0");
        lineaPrincipal.setHorizontalAlignment(SwingConstants.RIGHT);
        lineaPrincipal.setForeground(Color.WHITE);
        lineaPrincipal.setFont(new Font("Arial", Font.BOLD, 52));

        panelPantalla.add(lineaSuperior);
        panelPantalla.add(lineaPrincipal);

        JPanel panelBotones = new JPanel(new GridLayout(5, 4, 8, 8));
        panelBotones.setBackground(new Color(12, 12, 12));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        Color colorControl = new Color(60, 73, 95);
        Color colorNumero = new Color(45, 45, 45);
        Color colorOperador = new Color(48, 102, 225);
        Color colorIgual = new Color(96, 155, 232);
        Color colorBorrarUno = new Color(191, 96, 0);

        agregarBoton(panelBotones, "Undo", colorControl, Color.WHITE);
        agregarBoton(panelBotones, "Redo", colorControl, Color.WHITE);
        agregarBoton(panelBotones, "C", colorControl, Color.WHITE);
        agregarBoton(panelBotones, "←", colorBorrarUno, Color.WHITE);

        agregarBoton(panelBotones, "7", colorNumero, Color.WHITE);
        agregarBoton(panelBotones, "8", colorNumero, Color.WHITE);
        agregarBoton(panelBotones, "9", colorNumero, Color.WHITE);
        agregarBoton(panelBotones, "/", colorOperador, Color.WHITE);

        agregarBoton(panelBotones, "4", colorNumero, Color.WHITE);
        agregarBoton(panelBotones, "5", colorNumero, Color.WHITE);
        agregarBoton(panelBotones, "6", colorNumero, Color.WHITE);
        agregarBoton(panelBotones, "*", colorOperador, Color.WHITE);

        agregarBoton(panelBotones, "1", colorNumero, Color.WHITE);
        agregarBoton(panelBotones, "2", colorNumero, Color.WHITE);
        agregarBoton(panelBotones, "3", colorNumero, Color.WHITE);
        agregarBoton(panelBotones, "-", colorOperador, Color.WHITE);

        agregarBoton(panelBotones, "0", colorNumero, Color.WHITE);
        agregarBoton(panelBotones, ".", colorNumero, Color.WHITE);
        agregarBoton(panelBotones, "=", colorIgual, Color.BLACK);
        agregarBoton(panelBotones, "+", colorOperador, Color.WHITE);

        add(panelPantalla, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);

        instalarAtajosTeclado();
        actualizarPantalla();
    }

    private void agregarBoton(JPanel panel, String texto, Color fondo, Color textoColor) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 24));
        boton.setBackground(fondo);
        boton.setForeground(textoColor);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(true);
        boton.setOpaque(true);
        boton.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        try {
            boton.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        } catch (Exception e) {
            // no hacer nada
        }

        boton.addActionListener(e -> manejarAccion(texto));
        panel.add(boton);
    }

    private void instalarAtajosTeclado() {
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap();

        // Números de la fila superior
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_0, 0), "DIG0", "0");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_1, 0), "DIG1", "1");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_2, 0), "DIG2", "2");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_3, 0), "DIG3", "3");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_4, 0), "DIG4", "4");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_5, 0), "DIG5", "5");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_6, 0), "DIG6", "6");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_7, 0), "DIG7", "7");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_8, 0), "DIG8", "8");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_9, 0), "DIG9", "9");

        // Numpad
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD0, 0), "NDIG0", "0");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD1, 0), "NDIG1", "1");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2, 0), "NDIG2", "2");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD3, 0), "NDIG3", "3");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0), "NDIG4", "4");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD5, 0), "NDIG5", "5");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6, 0), "NDIG6", "6");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD7, 0), "NDIG7", "7");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD8, 0), "NDIG8", "8");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD9, 0), "NDIG9", "9");

        // Punto
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, 0), "PUNTO", ".");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_DECIMAL, 0), "NPUNTO", ".");

        // Operadores fila superior
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.SHIFT_DOWN_MASK), "SUMA_SHIFT", "+");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), "RESTA", "-");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_8, KeyEvent.SHIFT_DOWN_MASK), "MULT_SHIFT", "*");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, 0), "DIV", "/");

        // Operadores numpad
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0), "NSUMA", "+");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0), "NRESTA", "-");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_MULTIPLY, 0), "NMULT", "*");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_DIVIDE, 0), "NDIV", "/");

        // Igual / Enter
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "ENTER", "=");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, 0), "IGUAL", "=");

        // Borrados
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "BACK", "←");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "CLEAR", "C");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "CLEAR2", "C");

        // Undo / Redo
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), "UNDO", "Undo");
        registrarAtajo(inputMap, actionMap, KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK), "REDO", "Redo");
    }

    private void registrarAtajo(InputMap inputMap, ActionMap actionMap, KeyStroke keyStroke, String nombre, String valor) {
        inputMap.put(keyStroke, nombre);
        actionMap.put(nombre, new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                manejarAccion(valor);
            }
        });
    }

    private void manejarAccion(String texto) {
        switch (texto) {

            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
                if (resultadoMostrado
                        && calculadora.getMemoriaOp().isBlank()
                        && calculadora.getOperacionPend().isBlank()) {
                    calculadora.borrar();
                    operacionResuelta = "";
                    resultadoMostrado = false;
                }
                caretaker.ejecutar(new ComandoDigitar(calculadora, texto));
                break;

            case ".":
                if (resultadoMostrado
                        && calculadora.getMemoriaOp().isBlank()
                        && calculadora.getOperacionPend().isBlank()) {
                    calculadora.borrar();
                    operacionResuelta = "";
                    resultadoMostrado = false;
                }
                calculadora.ingresarPunto();
                break;

            case "+":
                operacionResuelta = "";
                resultadoMostrado = false;
                caretaker.ejecutar(new ComandoSumar(calculadora));
                break;

            case "-":
                operacionResuelta = "";
                resultadoMostrado = false;
                caretaker.ejecutar(new ComandoRestar(calculadora));
                break;

            case "*":
                operacionResuelta = "";
                resultadoMostrado = false;
                caretaker.ejecutar(new ComandoMultiplicar(calculadora));
                break;

            case "/":
                operacionResuelta = "";
                resultadoMostrado = false;
                caretaker.ejecutar(new ComandoDividir(calculadora));
                break;

            case "=":
                String operacionActual = construirOperacionActualSinLimite();

                if (!operacionActual.isBlank()
                        && !calculadora.getOperacionPend().isBlank()
                        && !calculadora.isEsperarOperando()) {
                    caretaker.ejecutar(new ComandoEquivalencia(calculadora));
                    operacionResuelta = operacionActual + " =";
                    resultadoMostrado = true;
                }
                break;

            case "C":
                caretaker.ejecutar(new ComandoBorrar(calculadora));
                operacionResuelta = "";
                resultadoMostrado = false;
                break;

            case "←":
                procesarRetroceso();
                break;

            case "Undo":
                caretaker.deshacer();
                operacionResuelta = "";
                resultadoMostrado = false;
                break;

            case "Redo":
                caretaker.rehacer();
                operacionResuelta = "";
                resultadoMostrado = false;
                break;
        }

        actualizarPantalla();
    }

    private void procesarRetroceso() {
        String expr = calculadora.getExpresion();

        if (expr == null || expr.isBlank()) {
            return;
        }

        if ("Error".equals(expr) || "NaN".equals(expr)) {
            calculadora.borrar();
            operacionResuelta = "";
            resultadoMostrado = false;
            return;
        }

        if (resultadoMostrado
                && calculadora.getMemoriaOp().isBlank()
                && calculadora.getOperacionPend().isBlank()) {
            operacionResuelta = "";
            resultadoMostrado = false;
        }

        calculadora.borrarUltimoDigito();
    }

    private String construirOperacionActualSinLimite() {
        String memoria = calculadora.getMemoriaOp();
        String operacion = calculadora.getOperacionPend();
        String expresion = calculadora.getExpresion();

        if (memoria == null) memoria = "";
        if (operacion == null) operacion = "";
        if (expresion == null) expresion = "0";

        if (!memoria.isBlank() && !operacion.isBlank()) {
            if (calculadora.isEsperarOperando()) {
                return memoria + " " + operacion;
            } else {
                return memoria + " " + operacion + " " + expresion;
            }
        }

        return expresion;
    }

    private void actualizarPantalla() {
        if (resultadoMostrado && !operacionResuelta.isBlank()) {
            lineaSuperior.setText(calculadora.limitarTexto(operacionResuelta));
            lineaPrincipal.setText(calculadora.getExpresion());
        } else {
            lineaSuperior.setText("");
            lineaPrincipal.setText(calculadora.construirOperacionActual());
        }
    }
}