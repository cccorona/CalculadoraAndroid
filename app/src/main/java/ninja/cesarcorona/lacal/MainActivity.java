package ninja.cesarcorona.lacal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {

    private final static String PLUS_OPERATOR = "+";
    private final static String MINUS_OPERATOR = "-";
    private final static String MULTIPLY_OPERATOR = "*";
    private final static String DIVISION_OPERATOR = "÷";
    private final static String SQRT_OPERATOR = "SQRT(";
    private final static String LEFT_PARENTHESIS = "(";
    private final static String RIGHT_PARENTHESIS = ")";
    private final static String POINT_OPERATOR = ".";
    private final static String POW_OPERATOR = "^";

    private final static String SQRT_VACIA = "SQRT()";

    public final static String OPERACION_ACTUAL = "operacionActual";
    public final static String ES_SQRT = "esSqrt";

    public HashMap<String, ArrayList<String>> precedencia;
    public ArrayList<String> niveles;
    public boolean isClosed = false;
    public boolean sqrtWasPressed = false;
    public static final String FISRT_LEVEL = "PRIMERO";
    public static final String SECOND_LEVEL = "SEGUNDO";
    public static final String THIRD_LEVEL = "TERCERO";
    public static final String FOUTH_LEVEL = "CUARTO";

    public static final String SQRT_VIEW = "SQRT";

    public static final int MULTPLICACION_PRIMERO = 1;
    public static final int DIVICION_PRIMERO = 2;
    public static final int SUMA_PRIMERO = 3;
    public static final int RESTA_PRIMERO = 4;

    private final static String SPLIT_PLUS_OPERATOR = "\\+";
    private final static String SPLIT_MINUS_OPERATOR = "\\-";
    private final static String SPLIT_MULTIPLY_OPERATOR = "\\*";
    private final static String SPLIT_DIVISION_OPERATOR = "÷";
    private final static String SPLIT_SQRT_OPERATOR = "SQRT(";
    private final static String SPLIT_LEFT_PARENTHESIS = "\\(";
    private final static String SPLIT_RIGHT_PARENTHESIS = "\\)";
    private final static String SPLIT_POINT_OPERATOR = "\\.";
    private final static String SPLIT_POW_OPERATOR = "\\^";


    private final int UNO = 1;
    private final int DOS = 2;
    private final int TRES = 3;
    private final int CUATRO = 4;
    private final int CINCO = 5;
    private final int SEIS = 6;
    private final int SIETE = 7;
    private final int OCHO = 8;
    private final int NUEVE = 9;
    private final int CERO = 0;

    private double result;
    private String operation = "";
    private String lastCharacter;

    public void init() {

        niveles = new ArrayList();
        niveles.add(FISRT_LEVEL);
        niveles.add(SECOND_LEVEL);
        niveles.add(THIRD_LEVEL);
        niveles.add(FOUTH_LEVEL);
        precedencia = new HashMap();
        ArrayList<String> temp = new ArrayList();
        temp.add(RIGHT_PARENTHESIS);
        precedencia.put(FISRT_LEVEL, temp);
        temp = new ArrayList();
        temp.add(POW_OPERATOR);
        precedencia.put(SECOND_LEVEL, temp);
        temp = new ArrayList();
        temp.add(MULTIPLY_OPERATOR);
        temp.add(DIVISION_OPERATOR);
        precedencia.put(THIRD_LEVEL, temp);
        temp = new ArrayList();
        temp.add(PLUS_OPERATOR);
        temp.add(MINUS_OPERATOR);
        precedencia.put(FOUTH_LEVEL, temp);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView opeTextView = (TextView) findViewById(R.id.text_view_result);
        if (savedInstanceState != null) {
            operation = savedInstanceState.getString(OPERACION_ACTUAL).trim();
            sqrtWasPressed = savedInstanceState.getBoolean(ES_SQRT);
            opeTextView.setText(operation);

        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current operation
        savedInstanceState.putString(OPERACION_ACTUAL, operation);
        savedInstanceState.putBoolean(ES_SQRT, sqrtWasPressed);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.opcion_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
        if (id == R.id.opcion_rate) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void calculatorOnClick(View view) {

        switch (view.getId()) {
            case R.id.button_one:
                addNumber(UNO);
                break;
            case R.id.button_two:
                addNumber(DOS);
                break;
            case R.id.button_three:
                addNumber(TRES);
                break;
            case R.id.button_four:
                addNumber(CUATRO);
                break;
            case R.id.button_five:
                addNumber(CINCO);
                break;
            case R.id.button_six:
                addNumber(SEIS);
                break;
            case R.id.button_seven:
                addNumber(SIETE);
                break;
            case R.id.button_eight:
                addNumber(OCHO);
                break;
            case R.id.button_nine:
                addNumber(NUEVE);
                break;
            case R.id.button_zero:
                addNumber(CERO);
                break;
            case R.id.button_plus:
                lastCharacter = getLastOperationCharacter();
                if (lastCharacter != null) {
                    if (isNumber(lastCharacter)) {
                        addCharacter(PLUS_OPERATOR);
                    }
                    if (isOperator(lastCharacter)) {
                        replaceLastOperator(PLUS_OPERATOR);
                    }
                }
                break;
            case R.id.button_minus:
                lastCharacter = getLastOperationCharacter();
                if (lastCharacter != null) {
                    if (isNumber(lastCharacter)) {
                        addCharacter(MINUS_OPERATOR);
                    }
                    if (isOperator(lastCharacter)) {
                        replaceLastOperator(MINUS_OPERATOR);
                    }
                }
                break;
            case R.id.button_multiply:
                lastCharacter = getLastOperationCharacter();
                if (lastCharacter != null) {
                    if (isNumber(lastCharacter)) {
                        addCharacter(MULTIPLY_OPERATOR);
                    }
                    if (isOperator(lastCharacter)) {
                        replaceLastOperator(MULTIPLY_OPERATOR);
                    }
                }
                break;
            case R.id.button_division:
                lastCharacter = getLastOperationCharacter();
                if (lastCharacter != null) {
                    if (isNumber(lastCharacter)) {
                        addCharacter(DIVISION_OPERATOR);
                    }
                    if (isOperator(lastCharacter)) {
                        replaceLastOperator(DIVISION_OPERATOR);
                    }
                }
                break;
            case R.id.button_pow:
                lastCharacter = getLastOperationCharacter();
                if (lastCharacter != null) {
                    if (isNumber(lastCharacter)) {
                        addCharacter(POW_OPERATOR);
                    }
                    if (isOperator(lastCharacter)) {
                        replaceLastOperator(POW_OPERATOR);
                    }
                }
                break;
            case R.id.button_root:
                if (sqrtWasPressed) {

                    sqrtWasPressed = false;
                    addCharacter(RIGHT_PARENTHESIS);
                    restoreSqrtView();
                } else {
                    if (operation.isEmpty()) {
                        addCharacter(SQRT_OPERATOR);
                        changeSqrtView();
                        sqrtWasPressed = true;
                    } else {
                        lastCharacter = getLastOperationCharacter();

                        if (lastCharacter != null) {
                            if (isNumber(lastCharacter)) {
                                //addCharacter(SQRT_OPERATOR);
                                //changeSqrtView();

                            }
                            if (isOperator(lastCharacter)) {
                                addCharacter(SQRT_OPERATOR);
                                changeSqrtView();
                                sqrtWasPressed = true;
                            }
                        }
                        // sqrtWasPressed = true;
                    }

                }


                break;
            case R.id.button_point:
                if (operation.isEmpty()) {
                    addCharacter(POINT_OPERATOR);

                } else {
                    String conPunto = getLast(operation);
                    if (conPunto.contains(POINT_OPERATOR)) {
                        break;
                    }
                    lastCharacter = getLastOperationCharacter();
                    if (lastCharacter != null) {
                        if (isNumber(lastCharacter)) {
                            addCharacter(POINT_OPERATOR);
                        }
                        if (isOperator(lastCharacter)) {
                            addCharacter(POINT_OPERATOR);
                        }
                    }

                }
                break;
            case R.id.button_clear:
                operation = "";
                HorizontalScrollView scroll = (HorizontalScrollView) findViewById(R.id.scroll_view_operation);
                TextView operView = (TextView) findViewById(R.id.text_view_result);
                //LinearLayout wrapper = (LinearLayout)findViewById(R.id.main_id_view);
                //operView.setWidth(wrapper.getWidth());
                scroll.setScrollX(0);
                sqrtWasPressed = false;
                restoreSqrtView();

                break;
            case R.id.button_back:
                deleteLastCharacter();
                break;
            case R.id.button_equals:
                operation = calculateResult(operation);

                formatResult();
                break;

            default:
                Log.v(view.toString(), "Button pressed");
                break;

        }
        printOperation();
    }

    public void changeSqrtView() {
        Button elBotton = (Button) findViewById(R.id.button_root);
        elBotton.setText(RIGHT_PARENTHESIS);
    }

    public void restoreSqrtView() {
        Button elBotton = (Button) findViewById(R.id.button_root);
        elBotton.setText(SQRT_VIEW);
    }

    public String calculateResult(String cadenaACulcular) {
        //SQRT
        double valorCalculado = 0.0;
        double valorAcumulado = 0.0;
        String calculo;
        String acumulado;

        while (cadenaACulcular.contains(SQRT_OPERATOR)) {

            int inicioSqrt = cadenaACulcular.indexOf(SQRT_OPERATOR);
            int cierreSqrt = inicioSqrt;
            int longitud = cadenaACulcular.length();
            int index = inicioSqrt + 4;
            String valorSQRT = "";
            String cadenaSqrt;
            String contenidoParentesis;
            int abiertos = 0;
            while (index < longitud) {
                String temp = String.valueOf(cadenaACulcular.charAt(index));
                if (LEFT_PARENTHESIS.equalsIgnoreCase(temp)) {
                    abiertos++;
                }
                if (RIGHT_PARENTHESIS.equalsIgnoreCase(temp)) {
                    abiertos--;
                }

                if (abiertos == 0) {
                    break;
                }
                index++;
            }
            if (abiertos > 0) {
                return operation;
            }
            cierreSqrt = index;
            cadenaSqrt = cadenaACulcular.substring(inicioSqrt, cierreSqrt + 1);
            if (SQRT_VACIA.equalsIgnoreCase(cadenaSqrt)) {
                operation = "";
                cadenaACulcular = "";
                printOperation();
                break;
            }
            Log.d(null, cadenaSqrt);
            int parentesisI = cadenaSqrt.indexOf(LEFT_PARENTHESIS);
            int parentesisD = cadenaSqrt.lastIndexOf(RIGHT_PARENTHESIS);
            contenidoParentesis = cadenaSqrt.substring(parentesisI + 1, parentesisD);
            if (contenidoParentesis.contains(SQRT_OPERATOR) ||
                    contenidoParentesis.contains(PLUS_OPERATOR) || contenidoParentesis.contains(MINUS_OPERATOR)
                    || contenidoParentesis.contains(POW_OPERATOR) || contenidoParentesis.contains(DIVISION_OPERATOR)
                    || contenidoParentesis.contains(MINUS_OPERATOR) || contenidoParentesis.contains(MULTIPLY_OPERATOR)) {
                valorSQRT = calculateResult(contenidoParentesis);
                cadenaACulcular = cadenaACulcular.replace(contenidoParentesis, valorSQRT);
            } else {
                valorSQRT = String.valueOf(Math.sqrt(Double.valueOf(contenidoParentesis)));
                cadenaACulcular = cadenaACulcular.replace(cadenaSqrt, valorSQRT);
            }


        }

        while (cadenaACulcular.contains(POW_OPERATOR)) {
            String[] operandos = cadenaACulcular.split(SPLIT_POW_OPERATOR, 2);
            double izquierdo;
            double derecho;
            double resultado;
            String operandoIzquierdo = operandos[0];
            String operadoDerecho = operandos[1];
            if (operadoDerecho.isEmpty()) {
                operation = "";
                return operation;
            }
            izquierdo = Double.parseDouble(getLast(operandoIzquierdo));
            derecho = Double.parseDouble(getFirstString(operadoDerecho));
            resultado = Math.pow(izquierdo, derecho);
            StringBuilder nuevaCadenaACalcular = new StringBuilder();
            nuevaCadenaACalcular.append(getleftOperator(operandoIzquierdo));
            nuevaCadenaACalcular.append(String.valueOf(resultado));
            nuevaCadenaACalcular.append(getRighOperator(operadoDerecho));
            cadenaACulcular = nuevaCadenaACalcular.toString().trim();

        }


        while (cadenaACulcular.contains(MULTIPLY_OPERATOR) || cadenaACulcular.contains(DIVISION_OPERATOR)) {
            int multiIndex = cadenaACulcular.indexOf(MULTIPLY_OPERATOR);
            int diviIndex = cadenaACulcular.indexOf(DIVISION_OPERATOR);
            double izquierdo;
            double derecho;
            double resultado;
            String[] operandos;
            String operandoIzquierdo;
            String operadoDerecho;

            StringBuilder nuevaCadenaACalcular;
            switch (getMultiDivicionPrimero(multiIndex, diviIndex)) {

                case MULTPLICACION_PRIMERO:
                    operandos = cadenaACulcular.split(SPLIT_MULTIPLY_OPERATOR, 2);

                    operandoIzquierdo = operandos[0];
                    operadoDerecho = operandos[1];
                    if (operadoDerecho.isEmpty()) {
                        operation = "";
                        return operation;
                    }
                    izquierdo = Double.parseDouble(getLast(operandoIzquierdo));
                    derecho = Double.parseDouble(getFirstString(operadoDerecho));
                    resultado = izquierdo * derecho;
                    nuevaCadenaACalcular = new StringBuilder();
                    nuevaCadenaACalcular.append(getleftOperator(operandoIzquierdo));
                    nuevaCadenaACalcular.append(String.valueOf(resultado));
                    nuevaCadenaACalcular.append(getRighOperator(operadoDerecho));
                    cadenaACulcular = nuevaCadenaACalcular.toString().trim();
                    break;
                case DIVICION_PRIMERO:
                    operandos = cadenaACulcular.split(SPLIT_DIVISION_OPERATOR, 2);

                    operandoIzquierdo = operandos[0];
                    operadoDerecho = operandos[1];
                    if (operadoDerecho.isEmpty()) {
                        operation = "";
                        return operation;
                    }
                    izquierdo = Double.parseDouble(getLast(operandoIzquierdo));
                    derecho = Double.parseDouble(getFirstString(operadoDerecho));
                    resultado = izquierdo / derecho;
                    nuevaCadenaACalcular = new StringBuilder();
                    nuevaCadenaACalcular.append(getleftOperator(operandoIzquierdo));
                    nuevaCadenaACalcular.append(String.valueOf(resultado));
                    nuevaCadenaACalcular.append(getRighOperator(operadoDerecho));
                    cadenaACulcular = nuevaCadenaACalcular.toString().trim();
                    break;
            }

        }

        while (cadenaACulcular.contains(PLUS_OPERATOR) || cadenaACulcular.contains(MINUS_OPERATOR)) {
            int plusIndex = cadenaACulcular.indexOf(PLUS_OPERATOR);
            int minusIndex = cadenaACulcular.indexOf(MINUS_OPERATOR);
            int nextOperator = 0;
            boolean numeroNegativo = false;
            if (minusIndex == 0 && hayMasOperaciones(cadenaACulcular.substring(1))) {
                numeroNegativo = true;
            }
            if (minusIndex == 0 && !hayMasOperaciones(cadenaACulcular.substring(1))) {
                break;
            }

            double izquierdo;
            double derecho;
            double resultado;
            String[] operandos;
            String operandoIzquierdo;
            String operadoDerecho;
            StringBuilder nuevaCadenaACalcular;
            switch (getPlusMinusPrimero(plusIndex, minusIndex)) {

                case SUMA_PRIMERO:
                    operandos = cadenaACulcular.split(SPLIT_PLUS_OPERATOR, 2);

                    operandoIzquierdo = operandos[0];
                    operadoDerecho = operandos[1];
                    if (operadoDerecho.isEmpty()) {
                        operation = "";
                        return operation;
                    }
                    izquierdo = Double.parseDouble(getLast(operandoIzquierdo));
                    derecho = Double.parseDouble(getFirstString(operadoDerecho));
                    resultado = izquierdo + derecho;
                    nuevaCadenaACalcular = new StringBuilder();
                    nuevaCadenaACalcular.append(getleftOperator(operandoIzquierdo));
                    nuevaCadenaACalcular.append(String.valueOf(resultado));
                    nuevaCadenaACalcular.append(getRighOperator(operadoDerecho));
                    cadenaACulcular = nuevaCadenaACalcular.toString().trim();
                    break;
                case RESTA_PRIMERO:
                    operandos = cadenaACulcular.split(SPLIT_MINUS_OPERATOR, 2);
                    operandoIzquierdo = operandos[0];
                    operadoDerecho = operandos[1];
                    if (operadoDerecho.isEmpty()) {
                        operation = "";
                        return operation;
                    }
                    boolean esSuma = false;
                    boolean esResta = false;

                    if (numeroNegativo) {
                        int sumaI = cadenaACulcular.substring(1).indexOf(PLUS_OPERATOR);
                        int RestaI = cadenaACulcular.substring(1).indexOf(MINUS_OPERATOR);
                        int cualEs = getPlusMinusPrimero(sumaI, RestaI);
                        if (cualEs == SUMA_PRIMERO) {
                            nextOperator = cadenaACulcular.substring(1).indexOf(PLUS_OPERATOR);
                            esSuma = true;
                        }
                        if (cualEs == RESTA_PRIMERO) {
                            nextOperator = cadenaACulcular.substring(1).indexOf(MINUS_OPERATOR);
                            esResta = true;
                        }

                        operandos[0] = cadenaACulcular.substring(minusIndex, nextOperator + 1);
                        operandos[1] = cadenaACulcular.substring(nextOperator + 2);
                        operandoIzquierdo = operandos[0].substring(1);
                        operadoDerecho = operandos[1];
                        if (operadoDerecho.isEmpty()) {
                            operation = "0";
                            return operation;
                        }
                    }


                    izquierdo = Double.parseDouble(getLast(operandoIzquierdo));
                    if (numeroNegativo) {
                        izquierdo = izquierdo * (-1);
                    }
                    derecho = Double.parseDouble(getFirstString(operadoDerecho));
                    resultado = izquierdo - derecho;
                    if (esResta) {
                        resultado = izquierdo - derecho;

                    }
                    if (esSuma) {
                        resultado = izquierdo + derecho;
                    }
                    nuevaCadenaACalcular = new StringBuilder();
                    nuevaCadenaACalcular.append(getleftOperator(operandoIzquierdo));
                    nuevaCadenaACalcular.append(String.valueOf(resultado));
                    nuevaCadenaACalcular.append(getRighOperator(operadoDerecho));
                    cadenaACulcular = nuevaCadenaACalcular.toString().trim();
                    break;
            }

        }

        return cadenaACulcular;
    }


    public boolean hayMasOperaciones(String masOperaciones) {
        boolean hayMas = false;
        if (masOperaciones.contains(PLUS_OPERATOR) ||
                masOperaciones.contains(MINUS_OPERATOR) ||
                masOperaciones.contains(MULTIPLY_OPERATOR) ||
                masOperaciones.contains(DIVISION_OPERATOR) ||
                masOperaciones.contains(POW_OPERATOR) ||
                masOperaciones.contains(SQRT_OPERATOR)) {
            hayMas = true;
        }

        return hayMas;
    }

    public int getMultiDivicionPrimero(int multi, int divi) {

        if (multi == -1) {
            return DIVICION_PRIMERO;
        }
        if (divi == -1) {
            return MULTPLICACION_PRIMERO;
        }
        if (multi < divi) {
            return MULTPLICACION_PRIMERO;
        }
        if (divi < multi) {
            return DIVICION_PRIMERO;
        }
        return -1;
    }

    public int getPlusMinusPrimero(int plus, int minus) {

        if (plus == -1) {
            return RESTA_PRIMERO;
        }
        if (minus == -1) {
            return SUMA_PRIMERO;
        }
        if (plus < minus) {
            return SUMA_PRIMERO;
        }
        if (minus < plus) {
            return RESTA_PRIMERO;
        }
        return -1;
    }


    public String getleftOperator(String oldChain) {
        String restoOperador = "";
        if (oldChain.length() == 1) {
            return "";
        }

        if (oldChain.length() > 1) {
            if (oldChain.contains(PLUS_OPERATOR) || oldChain.contains(MINUS_OPERATOR)
                    || oldChain.contains(MULTIPLY_OPERATOR) || oldChain.contains(DIVISION_OPERATOR)
                    || oldChain.contains(POW_OPERATOR)) {
                int len = oldChain.length();
                int index = 1;
                while (index < len) {
                    String temp = String.valueOf(oldChain.charAt(len - index));
                    if (isOperatorNoPoint(temp)) {
                        break;
                    }
                    index++;
                }
                restoOperador = oldChain.substring(0, len + 1 - index);
            } else {
                restoOperador = "";
            }
        }

        return restoOperador;

    }

    public String getRighOperator(String oldChain) {
        String restoOperador = "";
        if (oldChain.length() == 1) {
            return "";
        }

        if (oldChain.length() > 1) {
            if (oldChain.contains(PLUS_OPERATOR) || oldChain.contains(MINUS_OPERATOR)
                    || oldChain.contains(MULTIPLY_OPERATOR) || oldChain.contains(DIVISION_OPERATOR)
                    || oldChain.contains(POW_OPERATOR)) {
                int len = oldChain.length();
                int index = 0;
                while (index < len) {
                    String temp = String.valueOf(oldChain.charAt(index));
                    if (isOperatorNoPoint(temp)) {
                        break;
                    }
                    index++;
                }
                restoOperador = oldChain.substring(index);
            } else {
                restoOperador = "";
            }
        }

        return restoOperador;
    }

    public String getFirstString(String cadena) {

        String restoOperador = "";

        if (cadena != null && cadena.length() == 1) {
            return cadena.trim();
        }

        if (cadena != null && cadena.length() > 1) {
            if (cadena.contains(MINUS_OPERATOR) || cadena.contains(PLUS_OPERATOR) ||
                    cadena.contains(MULTIPLY_OPERATOR) || cadena.contains(DIVISION_OPERATOR) ||
                    cadena.contains(POW_OPERATOR)) {
                int len = cadena.length();
                int index = 0;
                while (index < len) {
                    String temp = String.valueOf(cadena.charAt(index));
                    if (isOperatorNoPoint(temp)) {
                        break;
                    }
                    index++;
                }
                restoOperador = cadena.substring(0, index);
            } else {
                restoOperador = cadena;
            }

        }

        return restoOperador;
    }

    public String getLast(String cadena) {

        String restoOperador = "";

        if (cadena != null && cadena.length() == 1) {
            return cadena.trim();
        }

        if (cadena != null && cadena.length() > 1) {
            if (cadena.contains(MINUS_OPERATOR) || cadena.contains(PLUS_OPERATOR) ||
                    cadena.contains(MULTIPLY_OPERATOR) || cadena.contains(DIVISION_OPERATOR) ||
                    cadena.contains(POW_OPERATOR)) {
                int len = cadena.length();
                int index = 1;
                while (index <= len) {
                    String temp = String.valueOf(cadena.charAt(len - index));
                    if (isOperatorNoPoint(temp)) {
                        index--;
                        break;

                    }
                    index++;
                }
                restoOperador = cadena.substring(len - index);
            } else {
                restoOperador = cadena;
            }

        }

        return restoOperador;
    }

    public void printOperation() {
        TextView scrrenTextView = (TextView) findViewById(R.id.text_view_result);
        scrrenTextView.setText(operation);
    }

    public void formatResult() {
        TextView scrrenTextView = (TextView) findViewById(R.id.text_view_result);

        try {
            if(operation.contains(POINT_OPERATOR)){
                BigDecimal entero = new BigDecimal(operation);
                String []enteroFlotante = operation.split(SPLIT_POINT_OPERATOR);
                if(isDouble(enteroFlotante[1])){

                }else{
                    operation = entero.toBigIntegerExact().toString();
                }
            }




        }catch (Exception e){
            Log.v("Error imprimiendo ",e.toString());
        }




    }

    public void deleteLastCharacter() {
        if (operation.length() == 1) {
            operation = "";
        }
        if (operation.length() > 1) {
            String lastChar = operation.substring(operation.length() - 1);
            if (RIGHT_PARENTHESIS.equalsIgnoreCase(lastChar)) {
                sqrtWasPressed = true;
                changeSqrtView();
            }
            if (LEFT_PARENTHESIS.equalsIgnoreCase(lastChar)) {
                operation = operation.substring(0, operation.length() - SQRT_OPERATOR.length());
                restoreSqrtView();
                sqrtWasPressed = false;
            } else {
                operation = operation.substring(0, operation.length() - 1);
            }

        }

    }

    public void addNumber(int numero) {
        StringBuilder operacionActual = new StringBuilder(operation);
        StringBuilder temp = new StringBuilder(String.valueOf(numero));
        operacionActual.append(temp);
        operation = operacionActual.toString();


    }

    public boolean isPoint(String character) {
        if (POINT_OPERATOR.equalsIgnoreCase(character)) {
            return true;
        } else {
            return false;
        }
    }

    public void replaceLastOperator(String operacion) {
        if (!LEFT_PARENTHESIS.equalsIgnoreCase(operation.substring(operation.length() - 1))) {
            StringBuilder sinOperacion = new StringBuilder(operation.substring(0, operation.length() - 1));
            sinOperacion.append(operacion);
            operation = sinOperacion.toString();
        }


    }

    public void addCharacter(String character) {
        StringBuilder operacionActual = new StringBuilder(operation);
        StringBuilder temp = new StringBuilder(character);
        operacionActual.append(temp);
        operation = operacionActual.toString();
    }


    public String getLastOperationCharacter() {
        if (!operation.isEmpty()) {
            return operation.substring(operation.length() - 1);
        }
        return null;
    }

    public boolean isNumber(String character) {
        boolean isNumero = false;
        if (!isOperator(character)) {
            if (RIGHT_PARENTHESIS.equalsIgnoreCase(character)) {
                return true;
            }
            if (LEFT_PARENTHESIS.equalsIgnoreCase(character)) {
                return false;
            }
            int numero = Integer.parseInt(character.trim());
            switch (numero) {
                case UNO:
                case DOS:
                case TRES:
                case CUATRO:
                case CINCO:
                case SEIS:
                case SIETE:
                case OCHO:
                case NUEVE:
                case CERO:
                    isNumero = true;
                    break;
            }
        }


        return isNumero;
    }

    public boolean isDouble(String character) {
        boolean isNumero = false;

        if(!character.isEmpty()){
            int flotante= Integer.parseInt(character);
            if(flotante > 0){
                isNumero = true;
            }
        }


        return isNumero;
    }





    public boolean isOperator(String character) {
        String c = character.trim();
        boolean esOperadorValido = false;
        switch (c) {
            case DIVISION_OPERATOR:
            case PLUS_OPERATOR:
            case MINUS_OPERATOR:
            case POW_OPERATOR:
            case POINT_OPERATOR:
            case MULTIPLY_OPERATOR:
            case SQRT_OPERATOR:
                esOperadorValido = true;
                break;
            case RIGHT_PARENTHESIS:
                esOperadorValido = false;
        }
        return esOperadorValido;
    }

    public boolean isOperatorNoPoint(String character) {
        String c = character.trim();
        boolean esOperadorValido = false;
        switch (c) {
            case DIVISION_OPERATOR:
            case PLUS_OPERATOR:
            case MINUS_OPERATOR:
            case POW_OPERATOR:
            case MULTIPLY_OPERATOR:
                esOperadorValido = true;
                break;
        }
        return esOperadorValido;
    }


}
