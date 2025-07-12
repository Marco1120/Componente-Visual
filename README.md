# BARRA DE CARGA (Componente Visual Personalizado)

## Análisis de código

### Declaración de clases y atributos
public class BarradeProgreso extends JPanel {

Declara la clase pública BarradeProgreso que hereda de JPanel. Heredar de JPanel permite que esta clase funcione como un componente gráfico dentro de interfaces gráficas Swing.

private Timer timer;

Atributo privado de tipo Timer que controla la animación principal de la barra de progreso. Por defecto se inicializa como null.

private int progreso = 0;

Variable entera privada que almacena el progreso actual, inicializada en 0%, con rango válido de 0 a 100.

private JLabel lblPorcentaje;

Etiqueta JLabel que mostrará el porcentaje o texto personalizado dentro de la barra.

private JPanel contenedor;

Panel contenedor adicional para dibujar la barra de progreso con personalización de su método paintComponent.

private Color colorBarra = Color.GREEN;

Color de la barra de progreso, inicializado como verde (Color.GREEN).

private Color colorTexto = Color.BLACK;

Color del texto que se mostrará en la barra, inicializado como negro.

private String textoPersonalizado = "";

Texto personalizado opcional, inicializado como cadena vacía. Permite mostrar mensajes como “Cargando…” en lugar de porcentaje.

private PosicionTexto posicionTexto = PosicionTexto.CENTRO;

Define la posición del texto usando una enumeración PosicionTexto, inicializada en el centro.

private int tamanoTexto = 14;

Tamaño de la fuente del texto, inicializado en 14 puntos.

private Timer timerAnimacionTexto;

Segundo Timer que controla la animación del texto (efecto de puntos suspensivos).

private int contadorAnimacion = 0;

Contador para la animación de puntos, se incrementa cíclicamente.

private String[] puntosAnimacion = {"", ".", "..", "..."};

Arreglo con los estados de la animación de puntos para el efecto visual de “Cargando…”.

### Enumeración para posición de texto
public enum PosicionTexto {
    CENTRO, ARRIBA, ABAJO, IZQUIERDA, DERECHA
}

Enumeración PosicionTexto que define las cinco posiciones posibles del texto dentro de la barra. Permite código más legible y seguro que usar enteros.

### Constructor de la clase
public BarradeProgreso() {

Constructor público que inicializa el componente con valores por defecto.

setLayout(new BorderLayout());

Establece el layout manager del panel principal como BorderLayout, que divide el espacio en regiones.

lblPorcentaje = new JLabel("0%", SwingConstants.CENTER);

Crea la etiqueta lblPorcentaje con texto inicial “0%” centrado horizontalmente.

lblPorcentaje.setFont(new Font("Arial", Font.BOLD, tamanoTexto));

Establece la fuente de la etiqueta en Arial, negrita, tamaño definido por tamanoTexto.

lblPorcentaje.setForeground(colorTexto);

Establece el color del texto de la etiqueta.

lblPorcentaje.setOpaque(false);

Hace que la etiqueta sea transparente, permitiendo ver la barra de fondo.

contenedor = new JPanel(null) {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(colorBarra);
        int anchoBarra = (int) (getWidth() * (progreso / 100.0));
        g.fillRect(0, 0, anchoBarra, getHeight());
    }
};

Panel contenedor con layout nulo y método paintComponent sobrescrito para dibujar el fondo gris claro y la barra de progreso con el color configurado.

contenedor.setLayout(new OverlayLayout(contenedor));

Establece un layout de superposición para permitir que la etiqueta se dibuje sobre la barra.

contenedor.add(lblPorcentaje);

Agrega la etiqueta al contenedor.

add(contenedor, BorderLayout.CENTER);

Agrega el contenedor al centro del panel principal.

timer = new Timer(100, new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        progreso++;
        if (progreso > 100) {
            progreso = 100;
            timer.stop();
            if (timerAnimacionTexto != null) {
                timerAnimacionTexto.stop();
            }
        }
        actualizarTexto();
        repaint();
    }
});

Inicializa el Timer principal que incrementa el progreso cada 100 ms y actualiza el texto y la barra. Detiene el timer al llegar a 100%.

setPreferredSize(new Dimension(300, 30));

Define el tamaño preferido de la barra de progreso: 300px de ancho y 30px de alto.

aplicarPosicionTexto();

Aplica la posición inicial del texto según la enumeración configurada.

### Método actualizar texto
private void actualizarTexto() {
    if (textoPersonalizado.isEmpty()) {
        lblPorcentaje.setText(progreso + "%");
    } else {
        if (progreso < 100) {
            lblPorcentaje.setText(textoPersonalizado + puntosAnimacion[contadorAnimacion]);
        } else {
            lblPorcentaje.setText("Completado");
            if (timerAnimacionTexto != null) {
                timerAnimacionTexto.stop();
            }
        }
    }
}

Actualiza el texto de la etiqueta según el progreso o el texto personalizado con su animación de puntos. Muestra “Completado” al finalizar.

### Método de control
public void iniciar() {
    progreso = 0;
    contadorAnimacion = 0;
    timer.start();
    if (!textoPersonalizado.isEmpty()) {
        iniciarAnimacionTexto();
    }
    repaint();
}

Inicia la animación de la barra y la animación de texto si existe texto personalizado.

public void detener() {
    timer.stop();
    if (timerAnimacionTexto != null) {
        timerAnimacionTexto.stop();
    }
}

Detiene ambos timers de animación.

public void reiniciar() {
    detener();
    progreso = 0;
    contadorAnimacion = 0;
    actualizarTexto();
    repaint();
}

Reinicia completamente el progreso y el texto mostrado.

### Métodos Getters y Setters
public int getProgreso() {
    return progreso;
}

Retorna el progreso actual.

public void setProgreso(int progreso) {
    this.progreso = Math.max(0, Math.min(100, progreso));
    actualizarTexto();
    repaint();
}

Establece manualmente el progreso, asegurando que esté entre 0 y 100, y actualiza la barra.

public Color getColorBarra() {
    return colorBarra;
}

Retorna el color actual de la barra.

public void setColorBarra(Color colorBarra) {
    this.colorBarra = colorBarra;
    repaint();
}

Establece un nuevo color para la barra y redibuja el componente.

public void setTextoPersonalizado(String textoPersonalizado) {
    this.textoPersonalizado = textoPersonalizado;
    if (!textoPersonalizado.isEmpty() && timer.isRunning()) {
        iniciarAnimacionTexto();
    } else if (timerAnimacionTexto != null) {
        timerAnimacionTexto.stop();
    }
    actualizarTexto();
}

Establece un texto personalizado y gestiona su animación correspondiente.

### Método para aplicar posición de texto
private void aplicarPosicionTexto() {
    switch (posicionTexto) {
        case CENTRO:
            lblPorcentaje.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblPorcentaje.setAlignmentY(Component.CENTER_ALIGNMENT);
            break;
        case ARRIBA:
            lblPorcentaje.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblPorcentaje.setAlignmentY(Component.TOP_ALIGNMENT);
            break;
        case ABAJO:
            lblPorcentaje.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblPorcentaje.setAlignmentY(Component.BOTTOM_ALIGNMENT);
            break;
        case IZQUIERDA:
            lblPorcentaje.setAlignmentX(Component.LEFT_ALIGNMENT);
            lblPorcentaje.setAlignmentY(Component.CENTER_ALIGNMENT);
            break;
        case DERECHA:
            lblPorcentaje.setAlignmentX(Component.RIGHT_ALIGNMENT);
            lblPorcentaje.setAlignmentY(Component.CENTER_ALIGNMENT);
            break;
    }
}

Alinea la etiqueta de texto en la posición configurada según la enumeración PosicionTexto.

### Método para animación
private void iniciarAnimacionTexto() {
    if (timerAnimacionTexto != null) {
        timerAnimacionTexto.stop();
    }
    timerAnimacionTexto = new Timer(500, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            contadorAnimacion = (contadorAnimacion + 1) % puntosAnimacion.length;
            actualizarTexto();
        }
    });
    timerAnimacionTexto.start();
}

Inicia el timer de animación para mostrar puntos suspensivos animados en el texto personalizado, actualizándolo cada 500 ms.


### ═══════════════════════════════════════════════════════════════════════

### Clase Prueba



### ═══════════════════════════════════════════════════════════════════════

### Video de Youtube
https://youtu.be/3uh3DK9VZhw?si=QMBw2d3lndUZDDOK
