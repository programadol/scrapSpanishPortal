# 🏠 scrapSpanishPortal - Inmobiliaria Scraper Bot

¡Hola! Bienvenido/a a **scrapSpanishPortal**, un proyecto diseñado para ayudarte a **monitorizar anuncios de un portal inmobiliario español** y **recibir notificaciones en Telegram** cuando haya nuevos inmuebles disponibles.

## 📋 Descripción

Este bot en **Java** realiza comprobaciones periódicas sobre un portal inmobiliario (no se menciona cuál) y envía un mensaje a través de Telegram cuando detecta un **nuevo anuncio**. Algunas características clave son:

- **No muestra anuncios antiguos**, solo los que se publiquen después de iniciar el bot.
- **Guarda en caché** los anuncios ya notificados, evitando duplicados o spam.
- **Se puede configurar fácilmente** mediante una interfaz visual y formularios.

## 🎯 Funcionalidades

- 🛠️ **Amplia configuración visual** y mediante formulario.
- 🗄️ **Sistema de cacheo** para evitar anuncios repetidos.
- 🌍 **Mapa interactivo** para seleccionar el área geográfica.
- 🏡 **Soporte de todos los filtros** de compra y alquiler del portal.
- 📦 **Importación de ficheros JSON** para manos expertas (recomiendo mejor el formulario).
- ✉️ **Notificaciones en Telegram** con detalles como mapa, precio, superficie y enlace.

## ⚠️ Problemas conocidos

- 🔒 **Timeouts del portal**: El portal podría bloquear tu IP (el uso de un proxy rotativo está en desarrollo, pero no disponible por el momento).
- 🖼️ **Interfaz preliminar**: Al ser una primera versión, pueden surgir fallos y comportamientos inesperados.
- 🐛 **Captura de errores limitada**: Actualmente, algunos errores no se capturan ni se registran correctamente.
- 📝 **Inputs de texto sensibles**: Evita usar caracteres especiales en los campos de entrada (sólo alfanuméricos por el momento).
- 🗺️ **Tecnología del mapa algo obsoleta**: Es posible que algunos comportamientos del mapa no sean los esperados.
- 🔄 **API de Telegram**: Si envías demasiados mensajes en poco tiempo, Telegram podría bloquear temporalmente tu bot.
- 🚫 **Múltiples instancias no probadas**: No se ha probado el uso simultáneo de varios trackers o instancias, pero podría aumentar el riesgo de bloqueo.

## 📂 Archivos importantes
- `C:\Users\{tu_usuario}\AppData\Roaming\scraper`: Localización de los ficheros importantes del BOT.
- `log.txt`: Archivo de log donde se registran errores. Aún no funciona al 100%, por lo que algunos errores podrían no aparecer.
- `configFile.json`: Archivo de configuración maestro. Se recomienda usar el formulario visual para configurarlo y evitar problemas.
- `cached_ads_XXXXXXX.txt`: Lista de ID de anuncios ya cacheados para evitar duplicados.

## 🧑‍💻 Manual de uso

### **Para usuarios no técnicos (Windows)**

1. **Instala JDK 23**: Descárgalo desde [este enlace](https://www.oracle.com/es/java/technologies/downloads/#jdk23-windows).
2. **Descarga el `.BAT`** de la sección de [releases](https://github.com/programadol/scrapSpanishPortal/releases/tag/v1.0.0).
3. **Descarga el `.JAR`** de la misma sección de [releases](https://github.com/programadol/scrapSpanishPortal/releases/tag/v1.0.0).
4. **Ejecuta el `.BAT`** haciendo doble clic sobre él, y el bot se iniciará automáticamente. ¡Voilà! 

### **Para usuarios técnicos y devs**

1. Instala tu **IDE favorito** (por ejemplo, IntelliJ o Eclipse).
2. Clona el proyecto con `git clone`.
3. Ejecuta `mvn install` para instalar las dependencias.
4. Corre la clase `Main` con un click derecho sobre "Run". ¡Listo!

## ⚙️ Manual de configuración detallada

1. **Configuración del bot de Telegram**:
   - **Generar un bot de Telegram**:
     - Habla con [BotFather](https://t.me/BotFather).
     - Usa el comando `/newbot` y sigue las instrucciones.
     - Introduce el nombre del bot (el que prefieras) y el **nombre de usuario** (debe terminar en "bot").
     - El bot te proporcionará un **token** con este formato: `7XXXXXXXXX:AAHXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX`. Guárdalo bien.
   - **Configurar el bot**:
     - Introduce el **Telegram Bot Token** en el campo correspondiente del formulario de configuración.
     - El **Telegram Bot Username** será el nombre que pusiste al crear el bot (en el paso anterior).
     - Para obtener tu **Telegram Notify User ID**:
       - Habla con [@getmy_idbot](https://t.me/getmy_idbot).
       - Ejecuta `/start` y copia el valor de "Your ID", que será un número de 9 cifras.

2. **Configura el intervalo de actualización**:
   - Este valor corresponde al tiempo (en milisegundos) entre cada comprobación del portal. Se recomienda introducir `300000` (5 minutos).

3. **Añadir un Tracker**:
   - Presiona el botón `+Tracker` para crear un nuevo **endpoint**:
     1. **Identificador único**: Introduce un valor único, como "1" o "miidunico1".
     2. **Nombre del endpoint**: Pon un nombre que te ayude a identificarlo.
     3. **Tipo de endpoint**: Este valor es el nombre en minúsculas del portal inmobiliario (se te proporcionará de forma privada).
     4. **Seleccionar área en el mapa**:
        - Usa `CTRL + Click` para arrastrar y seleccionar el área geográfica en el mapa.
        - Usa el **scroll** para ajustar el zoom (recomendado entre 12 y 14).
        - Asegúrate de establecer un área y zoom razonables para evitar fallos.

4. **Configura los filtros** del portal inmobiliario:
   - Puedes ajustar los filtros para que el bot solo notifique anuncios que cumplan con tus criterios (precio, superficie, etc.).
   - **Importante**:
     - No dejes campos vacíos o con ceros.
     - Introduce valores razonables para evitar que el bot falle.

5. **Hablamos con el bot**:
   - Abre una conversación con el bot que creaste y envíale el comando `/start`.

6. **Arranca el bot**:
   - Una vez configurados todos los parámetros, presiona el botón "Guardar & arrancar bot".
   - Si todo ha salido bien, deberías ver el mensaje **"Live running"** en la interfaz y recibir 3 mensajes de prueba en Telegram.

7. **¡Listo!** Ahora solo queda esperar a recibir notificaciones de nuevos pisos o casas.

## 🛠️ Próximos pasos

- 💻 Mejorar la interfaz gráfica para una mejor experiencia de usuario.
- 🔄 Implementar el uso de proxy rotativo para evitar bloqueos de IP.
- 📱 Añadir soporte para más portales inmobiliarios.

---

¡Gracias por interesarte en **scrapSpanishPortal**! Si tienes algún problema o sugerencia, no dudes en **crear un issue** o contribuir mediante un **pull request**. 😊
