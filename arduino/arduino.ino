// Dependencias
// Librería Adafruit NeoPiel para controlar el LED
// Liberría WiFi del ESP8266 para tener acceso a la red
#include <Adafruit_NeoPixel.h>

// Tiempo en ms entre cada peticion
const unsigned long sleepTime = 60000; 

// Declaramos los pines que vamos a utilizar
const int PIXEL_PIN = 2;

// Configuración la librería de Neopixel
//
// Parámetro 1 = Número de píxeles en la tira
// Parámetro 2 = Pin en el que está conectado el DI de la tira
// Parámetro 3 = Tipo de píxel
//   NEO_KHZ800  800 KHz bitstream (most NeoPixel products w/WS2812 LEDs)
//   NEO_KHZ400  400 KHz (classic 'v1' (not v2) FLORA pixels, WS2811 drivers)
//   NEO_GRB     Pixels are wired for GRB bitstream (most NeoPixel products)
//   NEO_RGB     Pixels are wired for RGB bitstream (v1 FLORA pixels, not v2)
Adafruit_NeoPixel strip = Adafruit_NeoPixel(1, PIXEL_PIN, NEO_GRB + NEO_KHZ800);

// Definición de colores que vamos a utilizar en la aplicación, usando el método
// Color() no es necesario usar GRB para definir los colores
const uint32_t amarillo = strip.Color(0xF7, 0xFF, 0x00);
const uint32_t rojo = strip.Color(0xFF, 0x00, 0x00);
const uint32_t naranja = strip.Color(0xFF, 0x9D, 0x00);
const uint32_t verde = strip.Color(0x00, 0xFF, 0x11);
const uint32_t azul = strip.Color(0x00, 0xBF, 0xFF);
const uint32_t gris = strip.Color(0x70, 0x70, 0x70);
const uint32_t blanco = strip.Color(0xFF, 0xFF, 0xFF);
const uint32_t morado = strip.Color(0xC3, 0x90, 0xD4);

// setup() se ejecuta una única vez, se utiliza para realizar la configuración 
// inicial de la aplicación
void setup() {
  initHardware();
}

// El código dentro de loop() se ejecuta en bucle durante la duració
void loop() {
  // Prueba todos los colores 
  cycleColors();
}

void initHardware() {
  // Abrimos una conexión al puerto de serie
  Serial.begin(9600);
  
  // Inicializar la tira y dejar el píxel apagado
  strip.begin();
  strip.setBrightness(255);
  strip.show();
}

// Cambia el pixel entre todos los colores disponibles
void cycleColors() {
  int delayTime = 4000;
  
  strip.setPixelColor(0, amarillo);
  strip.show();
  delay(delayTime);

  strip.setPixelColor(0, rojo);
  strip.show();
  delay(delayTime);

  strip.setPixelColor(0, naranja);
  strip.show();
  delay(delayTime);

  strip.setPixelColor(0, verde);
  strip.show();
  delay(delayTime);

  strip.setPixelColor(0, azul);
  strip.show();
  delay(delayTime);

  strip.setPixelColor(0, gris);
  strip.show();
  delay(delayTime);

  strip.setPixelColor(0, morado);
  strip.show();
  delay(delayTime);
}
