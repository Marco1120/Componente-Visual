### Integrantes del Equipo.
Jimenez Juarez Marco Antonio

Gonzalez Valentin Adrian

### Equipo 7

# BARRA DE PROGRESO (Componente Visual Personalizado)

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

### Proyecto de Prueba.
En esta clase se probó el componete de la barra animada a continuacion se mostraran las caracteristicas de esta misma.

### Importacion de las librerias.
import barradeprogreso.BarradeProgreso;: Importa la clase personalizada de barra de progreso del paquete barradeprogreso
import javax.swing.JOptionPane;: Importa la clase para mostrar cuadros de diálogo (mensajes de error o de confirmación)
import javax.swing.SwingUtilities;: Importa utilidades de Swing para el manejo seguro de threads en GUI

### Captura de datos del formulario.
Correo = txtCorreo.getText();:

Obtiene el texto ingresado en el campo de correo (txtCorreo)
getText() devuelve el contenido como String

Contra = new String(txtContra.getPassword());:

txtContra.getPassword() devuelve un array de caracteres 
new String() convierte el array de caracteres a String

Correo.equals("adrian.valentin@gmail.com"): Compara el correo ingresado con el correo válido
Contra.equals("123456"): Compara la contraseña ingresada con la contraseña válida

### Barra de Progreso
barradeProgreso1.setVisible(true);: Hace visible la barra de progreso en la interfaz
barradeProgreso1.iniciar();: Ejecuta el método iniciar() de la clase BarradeProgreso para comenzar la animación

new Thread(): Crea un nuevo hilo de ejecución
() -> { ... }: Expresión en lambda que define el código a ejecutar en el hilo
.start(): Inicia la ejecución del hilo
Propósito: Evitar bloquear la interfaz gráfica durante la espera

while (barradeProgreso1.getProgreso() < 100): Bucle que continúa mientras el progreso sea menor a 100%
Thread.sleep(100);: Pausa el hilo durante 100 milisegundos
try-catch: Manejo de excepciones para InterruptedException
e.printStackTrace();: Imprime el stack trace del error si ocurre una interrupción

SwingUtilities.invokeLater(): Ejecuta el código en el hilo de eventos de Swing (EDT)
IngresoalSistema sistema = new IngresoalSistema();: Crea una nueva instancia de la ventana principal
sistema.setVisible(true);: Hace visible la nueva ventana
this.dispose();: Cierra y libera recursos de la ventana actual

else: Se ejecuta cuando las credenciales no coinciden
JOptionPane.showMessageDialog(): Muestra un cuadro de diálogo modal
this: Referencia a la ventana padre del diálogo
"Correo o contraseña incorrectos.": Mensaje de error para el usuario

new Thread(() -> {
    // Código no bloqueante
}).start();

 Mantiene la interfaz responsiva durante operaciones largas y así el usuario puede ver la animación mientras se procesa

 ### Importacion de la Barra a la paleta de componentes
 Para poder tener nuestra barra de progreso en la paleta de componentes de netbeans lo que debemos hacer será:
 dar click derecho en algunas de las partes de la paleta y seleccionar la opcion de "Palette Manager" y dar click en "Add fromm JAR" y seleccionar el archivo ".Jar" del componente
 en donde el usuario lo haya guardado.
 
<img width="1920" height="1080" alt="Captura de pantalla (30)" src="https://github.com/user-attachments/assets/14fdf5fa-c5ce-47f5-bce7-9644d5eed2b9" />

<img width="1920" height="1080" alt="Captura de pantalla (31)" src="https://github.com/user-attachments/assets/f893ffa5-1435-41bc-b99a-57beedd9a387" />

<img width="1920" height="1080" alt="Captura de pantalla (32)" src="https://github.com/user-attachments/assets/2436ce41-ec73-4fd3-bdfa-1cded90ba71c" />

despues de eso se selecciona el nombre del componente en nuestro caso "BarradeProgreso" y le damos a next, despues de eso se selecciona en que carpeta de los componetes de la paleta quiere que
se coloque el componente eso esta a libertad del usuario nosotros lo pondremos en la carpeta de "Swing Controls" y se da click en finalizar una vez hecho todo eso ya aparecerá el componete en 
la paleta de componentes de netbeans

<img width="1920" height="1080" alt="Captura de pantalla (33)" src="https://github.com/user-attachments/assets/234416e6-a512-462c-a0a3-f690c7479c65" />

<img width="1920" height="1080" alt="Captura de pantalla (34)" src="https://github.com/user-attachments/assets/0f00c16d-eebb-4f92-8ba9-57ebfde7ed88" />

<img width="1920" height="1080" alt="Captura de pantalla (35)" src="https://github.com/user-attachments/assets/15280e2b-600f-4e47-b935-a2847d8b94e9" />

### Propiedades de la Barra de Progreso
Nuestra barra de progreso cuenta con una cierta contidad de propiedades que el diseñador podra cambiar a como él guste.

Cambio de Color a la barra.
Nuestra barra de progreso de forma predeterminada está en un color azul fuerte el cual en las propiedades se pueden cambiar al color que el diseñador quiera.

<img width="1920" height="1080" alt="Captura de pantalla (36)" src="https://github.com/user-attachments/assets/bd564bec-4c62-4e80-bf03-f6f8058a323d" />

<img width="1920" height="1080" alt="Captura de pantalla (37)" src="https://github.com/user-attachments/assets/d42f62a3-467c-4c92-909b-82f3a717a79d" />

Cambio de Color al Texto.
Al igual que como se le puede cambiar el color a la barra de progreso se le puede cambiar el color del texto a como el diseñador guste en las propiedades de la misma barra,
la barra de progreso por defecto viene en un color negro.

<img width="1920" height="1080" alt="Captura de pantalla (38)" src="https://github.com/user-attachments/assets/f653b2bc-7faa-45d1-8edd-ef332c9034f5" />

Cambio de Color al Borde de la Barra.
La barra también cuenta con la propiedad de poder cambiarle el color del borde a como guste el diseñador, la barra por defecto viene sin un borde, esto se logra dandole un borde
y dandole un color al borde de la barra.

<img width="1920" height="1080" alt="Captura de pantalla (39)" src="https://github.com/user-attachments/assets/6996f144-78aa-4f94-b417-d8045a055245" />

<img width="1920" height="1080" alt="Captura de pantalla (40)" src="https://github.com/user-attachments/assets/73cdc304-2b15-406a-b8c5-1bfc92ecceb0" />

Posicion del Texto de la Barra.
La barra cuenta con la propiedad de poder colocar el texto de la barra en cualquiera de las siguientes posiciones de la barra a como guste el diseñador:
1.-ARRIBA

2.-ABAJO

3.-CENTRO

4.-DERECHA

5.-IZQUIERDA

<img width="1920" height="1080" alt="Captura de pantalla (41)" src="https://github.com/user-attachments/assets/10a2a2dd-2d5c-4043-98b3-f6f053ed5807" />

Tamaño del Texto.
También podemos modificar el tamaño del texto que esta dentro de la barra a como guste el diseñador con tan solo escribiendo el número del tamaño que este desee.

<img width="1920" height="1080" alt="Captura de pantalla (42)" src="https://github.com/user-attachments/assets/e7278919-4f10-409c-876e-7505bd1c376f" />

Personalizacion del Texto.
El diseñador también tendra la posibilidad de poder cambiar el texto de la barra, en vez de que lleve el porcentaje que está por defecto con tan solo escribirlo en el espacio correspondiente.

<img width="1920" height="1080" alt="Captura de pantalla (43)" src="https://github.com/user-attachments/assets/3296d8bb-8687-4a1e-aaed-0200e96a707f" />

Demostracion de Uso.
A continuación se mostrará la barra de progreso con las propiedades ya antes menciionadas cambiadas con: Un borde Rojo, Color de la Barra Verde, Color de Texto Rosa, con el texto en la posicion de ARRIBA,
un Tamaño de Texto de 16 y por ultimo con el texto cambiado a "Cargando...".

<img width="1920" height="1080" alt="Captura de pantalla (44)" src="https://github.com/user-attachments/assets/b4613a82-bf4d-4dc1-8da7-1912e0e57cc5" />

Otra propiedad con la que cuenta nuestra Barra de Progreso es la que cuando el texto es personalizado y no es el porcentaje que está por defecto tiene una animacion en el texto en nuestro caso
el texto de "Cargando..." tiene una pequeña animación en los puntos.
### ═══════════════════════════════════════════════════════════════════════

### Video de Youtube
https://youtu.be/3uh3DK9VZhw?si=QMBw2d3lndUZDDOK
