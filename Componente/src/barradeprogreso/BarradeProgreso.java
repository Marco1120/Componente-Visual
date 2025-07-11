/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package barradeprogreso;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Clase que implementa una barra de progreso personalizable con animación de texto
 * Esta clase extiende JPanel y permite crear barras de progreso con diferentes
 * opciones de personalización como colores, posición de texto y animaciones
 */
public class BarradeProgreso extends JPanel {

    /**
     * Timer principal que controla el incremento del progreso
     * Se ejecuta cada 100ms para actualizar la barra
     */
    private Timer timer;
    
    /**
     * Variable que almacena el progreso actual (0-100)
     * Representa el porcentaje completado de la tarea
     */
    private int progreso = 0;

    /**
     * Etiqueta que muestra el texto del progreso (porcentaje o texto personalizado)
     */
    private JLabel lblPorcentaje;
    
    /**
     * Panel contenedor que aloja la etiqueta y maneja el dibujo de la barra
     */
    private JPanel contenedor;
    
    /**
     * Color de la barra de progreso
     * Por defecto está en azul 
     */
    private Color colorBarra = new Color(0, 122, 255);
    
    /**
     * Color del texto que se muestra en la barra
     * Por defecto está en negro
     */
    private Color colorTexto = Color.BLACK;
    
    /**
     * Tamaño de la fuente del texto
     * Por defecto está en un tamaño de 14
     */
    private int tamanoTexto = 14;
    
    /**
     * Texto personalizado que puede reemplazar el porcentaje
     * Si es null, se muestra el porcentaje normal
     */
    private String textoPersonalizado = null;

    /**
     * Posición donde se mostrará el texto en la barra
     * Por defecto es CENTRO
     */
    private PosicionTexto posicionTexto = PosicionTexto.CENTRO;
    
    /**
     * Timer secundario que controla la animación de puntos en el texto personalizado
     * Se ejecuta cada 500ms para crear el efecto de "cargando..."
     */
    private Timer timerAnimacionTexto;
    
    /**
     * Contador que controla el ciclo de animación de los puntos
     * Va de 0 a 3 y se reinicia automáticamente
     */
    private int contadorAnimacion = 0;
    
    /**
     * Array que contiene los diferentes estados de los puntos de animación
     * "", ".", "..", "..." para crear el efecto visual
     */
    private String[] puntosAnimacion = {"", ".", "..", "..."};

    /**
     * Enumeración que define las posiciones posibles del texto en la barra
     * Permite ubicar el texto en diferentes lugares según la necesidad del usuario
     */
    public enum PosicionTexto {
        /** Texto centrado en la barra */
        CENTRO, 
        /** Texto en la parte superior */
        ARRIBA, 
        /** Texto en la parte inferior */
        ABAJO, 
        /** Texto alineado a la izquierda */
        IZQUIERDA, 
        /** Texto alineado a la derecha */
        DERECHA
    }

    /**
     * Constructor de la clase BarradeProgreso
     * Inicializa todos los componentes gráficos y configura los timers
     * Establece valores por defecto y no requiere parámetros
     */
    public BarradeProgreso() {
        // Configurar el layout principal del panel
        setLayout(new BorderLayout());

        // Crear etiqueta para mostrar el progreso (inicialmente 0%)
        lblPorcentaje = new JLabel("0%", SwingConstants.CENTER);
        lblPorcentaje.setFont(new Font("Arial", Font.BOLD, tamanoTexto));
        lblPorcentaje.setForeground(colorTexto);

        // Crear panel personalizado que dibuja la barra de progreso
        contenedor = new JPanel(null) {
            /**
             * Método sobrescrito para dibujar la barra de progreso
             * Dibuja un fondo gris claro y la barra de progreso con el color configurado
             * @param g Graphics object para dibujar
             */
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                // Dibujar fondo gris claro (barra vacía)
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(0, 0, getWidth(), getHeight());
                
                // Dibujar la barra de progreso con el color configurado
                g.setColor(colorBarra);
                // Calcular el ancho de la barra según el progreso
                g.fillRect(0, 0, (int) (getWidth() * (progreso / 100.0)), getHeight());
            }
        };

        // Configurar el layout del contenedor para poner sobre los elementos
        contenedor.setLayout(new OverlayLayout(contenedor));
        contenedor.add(lblPorcentaje);
        add(contenedor, BorderLayout.CENTER);

        // Aplicar la posición inicial del texto
        aplicarPosicionTexto();

        // Timer que incrementa el progreso cada 100 milisegundos
        timer = new Timer(100, new ActionListener() {
            /**
             * Método que se ejecuta cada vez que el timer se dispara
             * Incrementa el progreso y hace que se actualice la interfaz
             * @param evt ActionEvent del timer
             */
            public void actionPerformed(ActionEvent evt) {
                // Incrementar el progreso
                progreso++;
                
                // Verificar si se completó el progreso
                if (progreso > 100) {
                    progreso = 100;
                    timer.stop(); // Detener el timer principal
                    
                    // Detener animación cuando termine el progreso
                    if (timerAnimacionTexto != null && timerAnimacionTexto.isRunning()) {
                        timerAnimacionTexto.stop();
                        // Mostrar texto final sin puntos de animación
                        if (textoPersonalizado != null && !textoPersonalizado.isEmpty()) {
                            lblPorcentaje.setText(textoPersonalizado);
                        }
                    }
                }

                // Actualizar texto según si hay texto personalizado o no
                if (textoPersonalizado != null && !textoPersonalizado.isEmpty()) {
                    // Si hay texto personalizado, iniciar o continuar animación
                    if (timerAnimacionTexto == null || !timerAnimacionTexto.isRunning()) {
                        iniciarAnimacionTexto();
                    }
                } else {
                    // Si no hay texto personalizado, mostrar porcentaje normal
                    lblPorcentaje.setText(progreso + "%");
                }

                // Repintar el componente para mostrar cambios
                repaint();
            }
        });

        // Timer que maneja la animación de puntos cada 500 milisegundos
        timerAnimacionTexto = new Timer(500, new ActionListener() {
            /**
             * Método que se ejecuta para animar los puntos del texto
             * Cicla entre diferentes estados de puntos (., .., ...)
             * @param evt ActionEvent del timer de animación
             */
            public void actionPerformed(ActionEvent evt) {
                // Solo animar si hay texto personalizado
                if (textoPersonalizado != null && !textoPersonalizado.isEmpty()) {
                    // Incrementar contador de animación de forma circular
                    contadorAnimacion = (contadorAnimacion + 1) % puntosAnimacion.length;
                    // Actualizar el texto con los puntos animados
                    lblPorcentaje.setText(textoPersonalizado + puntosAnimacion[contadorAnimacion]);
                }
            }
        });
    }

    /**
     * Inicia la animación de la barra de progreso
     * Reinicia el progreso a 0 y comienza la animación
     * Si hay texto personalizado, también inicia la animación de texto
     */
    public void iniciar() {
        progreso = 0;
        timer.start();
    }

    /**
     * Detiene completamente la animación de la barra de progreso
     * Pausa tanto el progreso como la animación de texto
     */
    public void detener() {
        timer.stop();
        if (timerAnimacionTexto != null) {
            timerAnimacionTexto.stop();
        }
    }

    /**
     * Reinicia la barra de progreso al estado inicial
     * Detiene todos los timers y resetea el progreso a 0
     */
    public void reiniciar() {
        detener();
        progreso = 0;
        contadorAnimacion = 0;
        // Mostrar texto inicial (personalizado o 0%)
        lblPorcentaje.setText((textoPersonalizado != null && !textoPersonalizado.isEmpty()) 
                ? textoPersonalizado : "0%");
        repaint();
    }

    /**
     * Obtiene el valor actual del progreso
     * @return int valor del progreso actual (0-100)
     */
    public int getProgreso() {
        return progreso;
    }

    /**
     * Método privado que inicia la animación de puntos para el texto personalizado
     * Solo se ejecuta cuando hay texto personalizado configurado
     */
    private void iniciarAnimacionTexto() {
        if (textoPersonalizado != null && !textoPersonalizado.isEmpty()) {
            contadorAnimacion = 0;
            timerAnimacionTexto.start();
        }
    }

    /**
     * Obtiene el color actual de la barra de progreso
     * @return Color objeto Color que representa el color de la barra
     */
    public Color getColorBarra() {
        return colorBarra;
    }

    /**
     * Establece un nuevo color para la barra de progreso
     * @param colorBarra Color nuevo color para la barra
     */
    public void setColorBarra(Color colorBarra) {
        this.colorBarra = colorBarra;
        repaint(); // Repintar para mostrar el cambio
    }

    /**
     * Obtiene el color actual del texto mostrado en la barra
     * @return Color objeto Color que representa el color del texto
     */
    public Color getColorTexto() {
        return colorTexto;
    }

    /**
     * Establece un nuevo color para el texto de la barra
     * @param colorTexto Color nuevo color para el texto
     */
    public void setColorTexto(Color colorTexto) {
        this.colorTexto = colorTexto;
        lblPorcentaje.setForeground(colorTexto);
    }

    /**
     * Obtiene el tamaño actual de la fuente del texto
     * @return int tamaño de la fuente en puntos
     */
    public int getTamanoTexto() {
        return tamanoTexto;
    }

    /**
     * Establece un nuevo tamaño para la fuente del texto
     * @param tamanoTexto int nuevo tamaño de fuente en puntos
     */
    public void setTamanoTexto(int tamanoTexto) {
        this.tamanoTexto = tamanoTexto;
        lblPorcentaje.setFont(new Font("Arial", Font.BOLD, tamanoTexto));
    }

    /**
     * Obtiene el texto personalizado actual
     * @return String texto personalizado o null si no hay ninguno
     */
    public String getTextoPersonalizado() {
        return textoPersonalizado;
    }

    /**
     * Establece un texto personalizado para mostrar en lugar del porcentaje
     * Si se proporciona un texto, se iniciará la animación de puntos durante el progreso
     * @param textoPersonalizado String texto a mostrar (puede ser null para volver al porcentaje)
     */
    public void setTextoPersonalizado(String textoPersonalizado) {
        // Detener animación anterior si existe
        if (timerAnimacionTexto != null && timerAnimacionTexto.isRunning()) {
            timerAnimacionTexto.stop();
        }
        
        this.textoPersonalizado = textoPersonalizado;
        contadorAnimacion = 0;
        
        if (textoPersonalizado != null && !textoPersonalizado.isEmpty()) {
            // Si la barra está en progreso, iniciar animación
            if (timer.isRunning()) {
                iniciarAnimacionTexto();
            } else {
                // Si no está en progreso, mostrar el texto sin animación
                lblPorcentaje.setText(textoPersonalizado);
            }
        } else {
            // Si se quita el texto personalizado, volverá a mostrar el porcentaje
            lblPorcentaje.setText(progreso + "%");
        }
    }

    /**
     * Obtiene la posición actual del texto en la barra
     * @return PosicionTexto enumeración que indica la posición del texto
     */
    public PosicionTexto getPosicionTexto() {
        return posicionTexto;
    }

    /**
     * Establece una nueva posición para el texto en la barra
     * @param posicionTexto PosicionTexto nueva posición del texto
     */
    public void setPosicionTexto(PosicionTexto posicionTexto) {
        this.posicionTexto = posicionTexto;
        aplicarPosicionTexto();
    }

    /**
     * Método privado que aplica la posición del texto según la configuración del usuario 
     * Utiliza el sistema de alineación de Swing para posicionar el texto
     */
    private void aplicarPosicionTexto() {
        switch (posicionTexto) {
            case CENTRO:
                // Centrar tanto horizontal como verticalmente
                lblPorcentaje.setAlignmentX(0.5f);
                lblPorcentaje.setAlignmentY(0.5f);
                break;
            case ARRIBA:
                // Centrar horizontalmente, alinear arriba
                lblPorcentaje.setAlignmentX(0.5f);
                lblPorcentaje.setAlignmentY(0.0f);
                break;
            case ABAJO:
                // Centrar horizontalmente, alinear abajo
                lblPorcentaje.setAlignmentX(0.5f);
                lblPorcentaje.setAlignmentY(1.0f);
                break;
            case IZQUIERDA:
                // Alinear a la izquierda, centrar verticalmente
                lblPorcentaje.setAlignmentX(0.0f);
                lblPorcentaje.setAlignmentY(0.5f);
                break;
            case DERECHA:
                // Alinear a la derecha, centrar verticalmente
                lblPorcentaje.setAlignmentX(1.0f);
                lblPorcentaje.setAlignmentY(0.5f);
                break;
        }
        // Revalidar y repintar el contenedor para aplicar los cambios
        contenedor.revalidate();
        contenedor.repaint();
    }
}